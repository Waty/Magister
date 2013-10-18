package com.wart.magister;

import java.util.HashMap;
import java.util.Iterator;

import android.os.AsyncTask;
import android.util.Log;

public class SyncTask extends AsyncTask<Void, String, Void> {

	private static final String TAG = "SyncTask";

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected Void doInBackground(Void... params) {
		Install();
		return null;
	}

	@Override
	protected void onPostExecute(Void voids) {
	}

	private Long Install() {
		publishProgress("setLicentie");

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
				publishProgress("8;Apparaat registreren");
				Serializer reader = new Serializer(call.response);
				reader.readROBoolean();
				if (reader.readByte() != 1) return null;

				int datalen = reader.readInt32();
				DataTable dt = reader.readDataTable();
				if (Global.profiles != null && Global.profiles.size() > 0 && dt != null && dt.size() > 0) {
					Iterator<HashMap<String, String>> iterator = Global.profiles.iterator();
					while (iterator.hasNext()) {
						HashMap<String, String> hash = iterator.next();
						if (Global.DBString(hash.get("code")).equals(Global.DBString(dt.get(0x0).get("code")))) {
							if (Global.DBString(hash.get("medius")).equals(Data.formatMediusUrl(Data.GetMediusURL()))) {
								Log.e(TAG, "Dit profiel bestaat al. Deinstalleer de applicatie om je profiel opnieuw aan te maken met een nieuwe uitnodiging. Het personaliseren wordt nu afgebroken.");
								return null;
							}
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
							Data.SetUser(Global.DBString(row.get("loginnaam")));
							Data.SetidGebr(Global.DBInt(row.get("idgebr")));
							Data.SetidType(Global.DBInt(row.get("idtype")));
							Data.SetFullName(Global.DBString(row.get("naam_vol")));
							Data.SetDeviceCode(Global.DBString(row.get("devicecode")));
							Data.SetidPers(Global.DBInt(row.get("idpers")));
							Data.SetidLeer(Global.DBInt(row.get("idleer")));
							// TODO: Fix this: Data.SetKey(Data.GetDeviceCode()
							// + "|" + Global.getVersionFromPackageInfo() + "|"
							// + Global.MD5Hash);
							Data.SetBetaald(true);
							Data.SetRol("leerling");
							publishProgress("setdata");
						} else if (tableNamesHash.containsKey(dt.TableName.toLowerCase().trim())) status = tableNamesHash.get(dt.TableName.toLowerCase().trim());
						publishProgress(new String[] {"Downloading: " + status});
						// TODO: Fix this: database.AddTable(dt, true);
						if (reader.pos < datalen) dt = reader.readDataTable();
						else dt = null;
					}
				}
				publishProgress("100;Sending personal device info");
				call = MediusCall.updateDeviceInfo();
				if (call != null) {
					Global.SetSharedValue("startup", Integer.valueOf(0x1));
					reader = new Serializer(call.response);
					reader.readROBoolean();
					reader.SkipROBinary();
					Global.DBInt(reader.readVariant());
					Global.DBBool(reader.readVariant());
					Global.DBBool(reader.readVariant());
					String magisterSuite = Global.DBString(reader.readVariant());
					Global.DBString(reader.readVariant());
					Global.DBString(reader.readVariant());
					DataTable rechtenTable = reader.readDataTable();
					DataTable betaald = reader.readDataTable();
					String foutStr = reader.getLastError();
					if (!Global.IsNullOrEmpty(foutStr)) publishProgress("Fout;" + foutStr);
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
							Global.UpdateCurrentProfile();
						}
					} else {
						Data.SetMagisterSuite(magisterSuite);
						Data.SetKey(Data.GetDeviceCode() + "|" + Global.getVersionFromPackageInfo());
						Global.UpdateCurrentProfile();
					}
				}
				if (Global.IsNullOrEmpty(Data.GetMagisterSuite())) publishProgress("Fout;Er is een fout opgetreden tijdens het installeren. Onbekende Magistersuite versie.");

				try {
					// TODO: Should run on UI thread... Global.SaveProfile();
				} catch (Exception ex) {
					Log.e(TAG, "Fout;Profile not saved!");
				} finally {
					if (isCancelled()) {
						return null;
					}
				}
			} catch (Exception problem) {
				Log.e(TAG, "Error tijdens personalisatie.");
				// TODO:
				// Global.bAuthenticate = false;
				// Global.LoadMenuStructure();
				publishProgress(new String[0x1]);
			}
			if (isCancelled()) {
				return null;
			}
		}
		return null;
	}

}
