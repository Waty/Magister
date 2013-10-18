package com.wart.magister;

import java.util.HashMap;
import java.util.Iterator;

import android.os.AsyncTask;
import android.util.Log;

public class SyncTask extends AsyncTask<Void, Integer, Void> {

	private static final String TAG = "SyncTask";

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onPostExecute(Void voids) {
	}

	@Override
	protected void onProgressUpdate(Integer... strings) {

	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v(TAG, "setLicentie");

		HashMap<String, String> tableNamesHash = new HashMap<String, String>();
		tableNamesHash.put("gebruiker", "Profiel");
		tableNamesHash.put("lestijden", "Schoolstructuur");
		tableNamesHash.put("persoon", "Personeel");
		tableNamesHash.put("leerling", "Leerlingen");
		tableNamesHash.put("agendaitem", "Agenda");
		DataTable settings = new DataTable(new String[] {"os", "hardwareid", "appname", "appversion", "suite", "rol"});
		DataRow srow = settings.newRow();
		srow.put("os", String.format("Android %s(Model: %s)", 0x2));
		srow.put("appname", Data.GetAppFamily());
		srow.put("appversion", Global.Device.Version);
		srow.put("hardwareid", Global.Device.HardwareID);
		srow.put("suite", Data.GetMagisterSuite());
		srow.put("rol", Data.GetRol());
		settings.add(srow);
		int count = 20;
		MediusCall call = MediusCall.RegisterDevice(settings);
		if (call != null) {
			try {
				Log.v(TAG, "8;Apparaat registreren");
				Serializer reader = new Serializer(call.response);
				reader.readROBoolean();
				if (reader.readByte() != 1) return null;

				int datalen = reader.readInt32();
				DataTable dt = reader.readDataTable();
				if (Global.profiles != null && Global.profiles.size() > 0 && dt != null && dt.size() > 0) {
					Iterator<HashMap<String, String>> iterator = Global.profiles.iterator();
					while (iterator.hasNext()) {
						HashMap<String, String> hash = iterator.next();
						if (Global.toDBString(hash.get("code")).equals(Global.toDBString(dt.get(0x0).get("code"))) && Global.toDBString(hash.get("medius")).equals(Data.formatMediusUrl(Data.GetMediusURL()))) {
							Log.e(TAG, "Dit profiel bestaat al. Deinstalleer de applicatie om je profiel opnieuw aan te maken met een nieuwe uitnodiging. Het personaliseren wordt nu afgebroken.");
							return null;

						}
					}
				}
				int prevpos = 0;
				int buffersize = reader.getBuffer().length / 80;
				int percentage = 0;
				while (dt != null && prevpos < reader.pos) {
					prevpos = reader.pos;
					count += 4;
					percentage = prevpos / buffersize;
					String status = "";
					if (!isCancelled()) {
						if (dt.TableName.equalsIgnoreCase("gebruiker")) {
							status = "Profiel";
							DataRow row = dt.get(0);
							Data.SetUser(Global.toDBString(row.get("loginnaam")));
							Data.SetidGebr(Global.toDBInt(row.get("idgebr")));
							Data.SetidType(Global.toDBInt(row.get("idtype")));
							Data.SetFullName(Global.toDBString(row.get("naam_vol")));
							Data.SetDeviceCode(Global.toDBString(row.get("devicecode")));
							Data.SetidPers(Global.toDBInt(row.get("idpers")));
							Data.SetidLeer(Global.toDBInt(row.get("idleer")));
							Data.SetKey(Data.GetDeviceCode() + "|" + Global.getVersionFromPackageInfo() + "|" + Global.getMD5Hash());
							Data.SetBetaald(true);
							Data.SetRol("leerling");
							Log.v(TAG, "setdata");
						} else if (tableNamesHash.containsKey(dt.TableName.toLowerCase().trim())) status = tableNamesHash.get(dt.TableName.toLowerCase().trim());
						Log.v(TAG, "Downloading: " + status);
						// TODO: Fix this: database.AddTable(dt, true);
						if (reader.pos < datalen) dt = reader.readDataTable();
						else dt = null;
					}
				}
				Log.v(TAG, "100;Sending personal device info");
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
					if (!Global.isNullOrEmpty(foutStr)) Log.v(TAG, "Fout;" + foutStr);
					// if(rechtenTable != null) //TODO: Fix this:
					// MaestroRechten.werkRechtenBij(rechtenTable);

					// if(betaald != null && betaald.size() > 0) {
					// if(betaald.get(0x0).get("ondergrens") != null); //TODO:
					// Fix this: database.OpenQuery(betaald.get(0x0) +
					// Global.DBString(betaald.get(0x0).get("ondergrens")) +
					// "\') ");
					// if((DataRow)betaald.get(0x0).get("licentietoken") !=
					// null) Data.SetBetaald(true);//TODO:
					// Boolean.valueOf(Global.IsNullOrEmpty(Global.DBString(betaald.get(0x0).get("licentietoken")))
					// ? 0x0 : 0x1));
					// }
					if (magisterSuite.equalsIgnoreCase(Data.GetMagisterSuite()) || magisterSuite.equalsIgnoreCase("")) {
						if (magisterSuite.equalsIgnoreCase("") && Data.GetMagisterSuite().equalsIgnoreCase("")) {
							Data.SetMagisterSuite("5.3.7");
							Data.SetKey(Data.GetDeviceCode() + "|" + Global.getVersionFromPackageInfo());
							Global.updateCurrentProfile();
						}
					} else {
						Data.SetMagisterSuite(magisterSuite);
						Data.SetKey(Data.GetDeviceCode() + "|" + Global.getVersionFromPackageInfo());
						Global.updateCurrentProfile();
					}
				}
				if (Global.isNullOrEmpty(Data.GetMagisterSuite())) Log.e(TAG, "Er is een fout opgetreden tijdens het installeren. Onbekende Magistersuite versie.");

			} catch (Exception problem) {
				Log.e(TAG, "Error tijdens personalisatie.");
				// TODO:
				// Global.bAuthenticate = false;
				// Global.LoadMenuStructure();
				// publishProgress(new String[0x1]);
			}
		}
		return null;
	}
}
