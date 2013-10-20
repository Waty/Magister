package com.wart.magister.setup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import com.wart.magister.Data;
import com.wart.magister.DataRow;
import com.wart.magister.DataTable;
import com.wart.magister.Global;
import com.wart.magister.MediusCall;
import com.wart.magister.R;
import com.wart.magister.Serializer;

public class RegisterActivity extends Activity {

	enum RegisterStatus {
		RegisteringDevice, DownloaadingProfile
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class RegisterTask extends AsyncTask<Void, String, Void> {

		private static final String TAG = "RegisterTask";
		private static final String ERRROR = "Error";

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Void voids) {
		}

		@Override
		protected void onProgressUpdate(String... strings) {

		}

		@Override
		protected Void doInBackground(Void... params) {
			publishProgress(Data.getLicense());

			HashMap<String, String> tableNamesHash = new HashMap<String, String>();
			tableNamesHash.put("gebruiker", "Profiel");
			tableNamesHash.put("lestijden", "Schoolstructuur");
			tableNamesHash.put("persoon", "Personeel");
			tableNamesHash.put("leerling", "Leerlingen");
			tableNamesHash.put("agendaitem", "Agenda");
			DataTable settings = new DataTable("os", "hardwareid", "appname", "appversion", "suite", "rol");
			DataRow srow = settings.newRow();
			srow.put("os", "Android " + Global.Device.OSVersion + "(Model: " + Global.Device.Model + ")");
			srow.put("appname", Data.getAppName());
			srow.put("appversion", Global.Device.Version);
			srow.put("hardwareid", Global.Device.HardwareID);
			srow.put("suite", Data.getMagisterSuite());
			srow.put("rol", Data.getRol());
			settings.add(srow);
			MediusCall call = MediusCall.RegisterDevice(settings);
			if (call != null) {
				try {
					publishProgress("Registering device...");
					Serializer reader = new Serializer(call.response);
					reader.readROBoolean();
					if (reader.readByte() != 1) return null;

					int datalen = reader.readInt32();
					DataTable dt = reader.readDataTable();
					if (Global.profiles != null && Global.profiles.size() > 0 && dt != null && dt.size() > 0) {
						Iterator<HashMap<String, String>> iterator = Global.profiles.iterator();
						while (iterator.hasNext()) {
							HashMap<String, String> hash = iterator.next();
							if (Global.toDBString(hash.get("code")).equals(Global.toDBString(dt.get(0x0).get("code"))) && Global.toDBString(hash.get("medius")).equals(Data.formatMediusUrl(Data.getMediusURL()))) {
								publishProgress(ERRROR, "Dit profiel bestaat al. Deinstalleer de applicatie om je profiel opnieuw aan te maken met een nieuwe uitnodiging. Het personaliseren wordt nu afgebroken.");
								return null;

							}
						}
					}
					int prevpos = 0;
					while (dt != null && prevpos < reader.pos) {
						prevpos = reader.pos;
						String status = "";
						if (!isCancelled()) {
							if (dt.TableName.equalsIgnoreCase("gebruiker")) {
								status = "Profiel";
								DataRow row = dt.get(0);
								Data.setUsername(Global.toDBString(row.get("loginnaam")));
								Data.setUserId(Global.toDBInt(row.get("idgebr")));
								Data.setIdType(Global.toDBInt(row.get("idtype")));
								Data.setFullName(Global.toDBString(row.get("naam_vol")));
								Data.setDeviceCode(Global.toDBString(row.get("devicecode")));
								Data.setEmployeeId(Global.toDBInt(row.get("idpers")));
								Data.setStudentId(Global.toDBInt(row.get("idleer")));
								Data.setKey(Data.getDeviceCode() + "|" + Global.getVersionFromPackageInfo() + "|" + Global.getMD5Hash());
								Data.setRol("leerling");
							} else if (tableNamesHash.containsKey(dt.TableName.toLowerCase(Locale.ENGLISH).trim())) status = tableNamesHash.get(dt.TableName.toLowerCase().trim());
							publishProgress("Downloading: " + status);
							// TODO: Fix this: database.AddTable(dt, true);
							if (reader.pos < datalen) dt = reader.readDataTable();
							else dt = null;
						}
					}
					publishProgress("Sending personal device info");
					call = MediusCall.updateDeviceInfo();
					if (call != null) {
						Global.setSharedValue("startup", Integer.valueOf(0x1));
						reader = new Serializer(call.response);
						reader.readROBoolean();
						reader.SkipROBinary();
						Global.toDBInt(reader.readVariant());
						Global.toDBBool(reader.readVariant());
						Global.toDBBool(reader.readVariant());
						String magisterSuite = Global.toDBString(reader.readVariant());
						Global.toDBString(reader.readVariant());
						Global.toDBString(reader.readVariant());
						DataTable rechtenTable = reader.readDataTable();
						DataTable betaald = reader.readDataTable();
						String foutStr = reader.getLastError();
						if (!Global.isNullOrEmpty(foutStr)) publishProgress(ERRROR, foutStr);

						// TODO: Implement the rechtenTable DataTable
						if (rechtenTable != null) for (DataRow row : rechtenTable)
							for (Entry<String, Object> e : row.entrySet())
								Log.v(TAG, e.getKey() + "=" + e.getValue());

						// TODO: Implement the betaald DataTable
						if (betaald != null) for (DataRow row : betaald)
							for (Entry<String, Object> e : row.entrySet())
								Log.v(TAG, e.getKey() + "=" + e.getValue());

						if (magisterSuite.equalsIgnoreCase(Data.getMagisterSuite()) || magisterSuite.equalsIgnoreCase("")) {
							if (magisterSuite.equalsIgnoreCase("") && Data.getMagisterSuite().equalsIgnoreCase("")) {
								Data.setMagisterSuite("5.3.7");
								Data.setKey(Data.getDeviceCode() + "|" + Global.getVersionFromPackageInfo());
								Global.updateCurrentProfile();
							}
						} else {
							Data.setMagisterSuite(magisterSuite);
							Data.setKey(Data.getDeviceCode() + "|" + Global.getVersionFromPackageInfo());
							Global.updateCurrentProfile();
						}
					}
					if (Global.isNullOrEmpty(Data.getMagisterSuite())) publishProgress(ERRROR, "Er is een fout opgetreden tijdens het installeren. Onbekende Magistersuite versie.");

				} catch (Exception ex) {
					publishProgress(ERRROR, "Error tijdens personalisatie.");
					Log.e(TAG, "Exception in RegisterActivity", ex);
					// TODO:
					// Global.bAuthenticate = false;
					// Global.LoadMenuStructure();
					// publishProgress(new String[0x1]);
				}
			}
			return null;
		}
	}

}
