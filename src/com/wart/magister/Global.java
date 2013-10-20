package com.wart.magister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

public final class Global {
	public static String MD5Hash = "";
	private static final String TAG = "Global";
	private static Map<String, Object> sharedDictionary = new HashMap<String, Object>();
	public static Context appContext;
	public static Date NODATE;
	public static boolean doMediusCallToServer;
	public static List<HashMap<String, String>> profiles;

	public static class Device {
		public static String Model;
		public static String OSVersion;
		public static String Version;
		public static String HardwareID;
	}

	public static boolean toDBBool(Object obj) {
		if (obj != null && obj != DBNull.Value) return Boolean.parseBoolean(String.valueOf(obj));
		return false;
	}

	public static int toDBInt(Object obj) {
		if (obj != null) {
			String value = String.valueOf(obj);
			if (value.length() > 0) return Integer.parseInt(value);
		}
		return 0;
	}

	public static String toDBString(Object obj) {
		if (obj != null) {
			if (obj.toString().contains("\r\n")) return obj.toString().replaceAll("\r\n", "\n");
			return obj.toString();
		}
		return "";
	}

	public static SharedPreferences getSharedPreferences() {
		if (appContext == null) return null;
		return appContext.getSharedPreferences(Data.APPNAME, Context.MODE_PRIVATE);
	}

	public static boolean isNullOrEmpty(String paramString) {
		return paramString == null || paramString.equals("") || paramString.equalsIgnoreCase("null") || paramString.length() <= 0;
	}

	public static void setSharedPreferenceValues(final String key, final Object value) {
		if (Global.appContext != null) {
			final SharedPreferences getSharedPreferenceValue = getSharedPreferences();
			if (getSharedPreferenceValue != null) {
				final SharedPreferences.Editor edit = getSharedPreferenceValue.edit();
				if (value instanceof String) edit.putString(key, toDBString(value));
				else if (value instanceof Integer) edit.putInt(key, toDBInt(value));
				else if (value instanceof Boolean) edit.putBoolean(key, toDBBool(value));
				else if (value instanceof Date) edit.putString(key, ((Date) value).toGMTString());

				if (edit.commit()) Log.v("Global", String.format("Opslaan %s gelukt", key));
				else Log.w("Global", String.format("Opslaan %s mislukt!", key));
			}
		}
	}

	public static void setSharedValue(final String s, final Object o) {
		Global.sharedDictionary.put(s.toLowerCase(Locale.ENGLISH), o);
	}

	public boolean isOnline() {
		if (appContext == null) return false;
		ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
	}

	// Fake the version like the latest version of Meta
	public static String getVersionFromPackageInfo() {
		return "1.0.21";
	}

	public static void updateCurrentProfile() {
		if (profiles != null) {
			int n = 0;
			int n2 = -1;
			for (int n3 = 0; n3 < Global.profiles.size() && n == 0; ++n3) {
				if (toDBString(Global.profiles.get(n3).get("naam")).equalsIgnoreCase(Data.getString(Data.FULLNAME))) {
					n2 = n3;
					n = 1;
				}
			}
			if (n2 > -1) {
				final HashMap<String, String> hashMap = Global.profiles.get(n2);
				hashMap.remove(Data.MAGISTER_SUITE);
				hashMap.put(Data.MAGISTER_SUITE, Data.getString(Data.MAGISTER_SUITE));
				saveProfile();
			}
		}
	}

	public static void saveProfile() {
		try {
			File file = new File(appContext.getDir("profile", Context.MODE_PRIVATE), "map");
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
			outputStream.writeObject(profiles);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			Log.e(TAG, "Error in SaveProfile", e);
		}
		return;
	}

	public static String getMD5Hash() {
		if (isNullOrEmpty(Global.MD5Hash) && Global.appContext != null) {
			try {
				File file = new File(Global.appContext.getPackageCodePath());
				FileInputStream fis = new FileInputStream(file);
				byte[] array = new byte[(int) file.length()];
				int read;
				for (int i = 0; i < array.length; i += read) {
					read = fis.read(array, i, array.length - i);
					if (read < 0) break;
				}
				fis.close();
				MessageDigest digester = MessageDigest.getInstance("MD5");
				digester.update(array, 0, array.length);
				Global.MD5Hash = new BigInteger(1, digester.digest()).toString(16);
				Log.i(TAG, "MD5: " + Global.MD5Hash);

			} catch (Exception ex) {
				Log.e(TAG, "Exception in getMD5Hash", ex);
			}
		}
		return Global.MD5Hash;
	}

	public static DataTable[] processDataTableResponse(final Serializer serializer, int tableCount) {
		try {
			final ArrayList<DataTable> list = new ArrayList<DataTable>();
			for (int i = 0; serializer.pos < serializer.getBufferLength() && (tableCount == -1 || i < tableCount); i++)
				list.add(serializer.readDataTable());

			final DataTable[] array = new DataTable[list.size()];
			list.toArray(array);
			return array;
		} catch (Exception ex) {
			Log.e(TAG, "processDataTableResponse Error", ex);
		}
		return null;
	}

}