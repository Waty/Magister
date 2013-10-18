package com.wart.magister;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

public final class Global {
	private static Map<String, Object> sharedDictionary = new HashMap<String, Object>();
	public static Context AppContext;
	public static Date NODATE;
	public static boolean doMediusCallToServer;
	public static List<HashMap<String, String>> profiles;

	public static class Device {
		public static String Model;
		public static String OSVersion;
		public static String Version;
		public static String HardwareID;
	}

	public static boolean DBBool(Object obj) {
		if (obj != null && obj != DBNull.Value) return Boolean.parseBoolean(String.valueOf(obj));
		return false;
	}

	public static int DBInt(Object obj) {
		if (obj != null) {
			String value = String.valueOf(obj);
			if (value.length() > 0) return Integer.parseInt(value);
		}
		return 0;
	}

	public static String DBString(Object obj) {
		if (obj != null) {
			if (obj.toString().contains("\r\n")) return obj.toString().replaceAll("\r\n", "\n");
			return obj.toString();
		}
		return "";
	}

	public static SharedPreferences GetSharedPreferenceValue(String paramString) {
		if (AppContext == null) return null;
		return AppContext.getSharedPreferences(paramString, 0);
	}

	public static boolean IsNullOrEmpty(String paramString) {
		return paramString == null || paramString.equals("") || paramString.equalsIgnoreCase("null") || paramString.length() <= 0;
	}

	public static void SetSharedPreferenceValues(final String s, final String s2, final Object o) {
		if (Global.AppContext != null) {
			final SharedPreferences getSharedPreferenceValue = GetSharedPreferenceValue(s);
			if (getSharedPreferenceValue != null) {
				final SharedPreferences.Editor edit = getSharedPreferenceValue.edit();
				if (o instanceof String) {
					edit.putString(s2, DBString(o));
				} else if (o instanceof Integer) {
					edit.putInt(s2, DBInt(o));
				} else if (o instanceof Boolean) {
					edit.putBoolean(s2, DBBool(o));
				} else if (o instanceof Date) {
					edit.putString(s2, ((Date) o).toGMTString());
				}
				if (edit.commit()) Log.v("Global", String.format("Opslaan %s gelukt", s2));
				else Log.w("Global", String.format("Opslaan %s mislukt!", s2));
			}
		}
	}

	public static void SetSharedValue(final String s, final Object o) {
		Global.sharedDictionary.put(s.toLowerCase(), o);
	}

	public boolean isOnline() {
		if (AppContext == null) return false;
		ConnectivityManager cm = (ConnectivityManager) AppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
	}

	// Fake the version like the latest version of Meta
	public static String getVersionFromPackageInfo() {
		return "1.0.21";
	}

	public static void UpdateCurrentProfile() {
		if (Global.profiles != null) {
			int n = 0;
			int n2 = -1;
			for (int n3 = 0; n3 < Global.profiles.size() && n == 0; ++n3) {
				if (DBString(Global.profiles.get(n3).get("naam")).equalsIgnoreCase(Data.GetFullName())) {
					n2 = n3;
					n = 1;
				}
			}
			if (n2 > -1) {
				final HashMap<String, String> hashMap = Global.profiles.get(n2);
				hashMap.remove("magistersuite");
				hashMap.put("magistersuite", Data.GetMagisterSuite());
				// TODO: Implement SaveProfile
				// SaveProfileXML();
			}
		}
	}
}