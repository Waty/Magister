package com.wart.magister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

public final class Global {
	public static String MD5Hash = "";
	private static final String TAG = "Global";
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

	public static SharedPreferences getSharedPreferenceValue(String paramString) {
		if (AppContext == null) return null;
		return AppContext.getSharedPreferences(paramString, 0);
	}

	public static boolean isNullOrEmpty(String paramString) {
		return paramString == null || paramString.equals("") || paramString.equalsIgnoreCase("null") || paramString.length() <= 0;
	}

	public static void setSharedPreferenceValues(final String s, final String s2, final Object o) {
		if (Global.AppContext != null) {
			final SharedPreferences getSharedPreferenceValue = getSharedPreferenceValue(s);
			if (getSharedPreferenceValue != null) {
				final SharedPreferences.Editor edit = getSharedPreferenceValue.edit();
				if (o instanceof String) {
					edit.putString(s2, toDBString(o));
				} else if (o instanceof Integer) {
					edit.putInt(s2, toDBInt(o));
				} else if (o instanceof Boolean) {
					edit.putBoolean(s2, toDBBool(o));
				} else if (o instanceof Date) {
					edit.putString(s2, ((Date) o).toGMTString());
				}
				if (edit.commit()) Log.v("Global", String.format("Opslaan %s gelukt", s2));
				else Log.w("Global", String.format("Opslaan %s mislukt!", s2));
			}
		}
	}

	public static void setSharedValue(final String s, final Object o) {
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

	public static void updateCurrentProfile() {
		if (Global.profiles != null) {
			int n = 0;
			int n2 = -1;
			for (int n3 = 0; n3 < Global.profiles.size() && n == 0; ++n3) {
				if (toDBString(Global.profiles.get(n3).get("naam")).equalsIgnoreCase(Data.GetFullName())) {
					n2 = n3;
					n = 1;
				}
			}
			if (n2 > -1) {
				final HashMap<String, String> hashMap = Global.profiles.get(n2);
				hashMap.remove("magistersuite");
				hashMap.put("magistersuite", Data.GetMagisterSuite());
				saveProfileXML();
			}
		}
	}

	public static void saveProfileXML() {
		try {
			File file = new File(new StringBuilder(String.valueOf(Data.GetAppFolder())).append("/users.xml").toString());
			if (!file.exists()) file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			XmlSerializer serializer = android.util.Xml.newSerializer();
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.startTag(null, "users");
			if (Global.profiles != null) {
				Iterator<HashMap<String, String>> itter = Global.profiles.iterator();
				while (itter.hasNext()) {
					HashMap<String, String> map = itter.next();
					serializer.startTag(null, "user");
					serializer.startTag(null, "naam");
					serializer.text(Global.toDBString(map.get("naam")));
					serializer.endTag(null, "naam");
					serializer.startTag(null, "code");
					serializer.text(Global.toDBString(map.get("code")));
					serializer.endTag(null, "code");
					serializer.startTag(null, "medius");
					serializer.text(Data.formatMediusUrl(Global.toDBString(map.get("medius"))));
					serializer.endTag(null, "medius");
					serializer.startTag(null, "dbnr");
					serializer.text(Global.toDBString(map.get("dbnr")));
					serializer.endTag(null, "dbnr");
					serializer.startTag(null, "rol");
					serializer.text(Global.toDBString(map.get("rol")));
					serializer.endTag(null, "rol");
					serializer.startTag(null, "magistersuite");
					serializer.text(Global.toDBString(map.get("magistersuite")));
					serializer.endTag(null, "magistersuite");
					serializer.startTag(null, "licentie");
					serializer.text(Global.toDBString(map.get("licentie")));
					serializer.endTag(null, "licentie");
					if (map.containsKey("delete")) {
						serializer.startTag(null, "delete");
						serializer.text(Global.toDBString(map.get("delete")));
						serializer.endTag(null, "delete");
					}
					serializer.endTag(null, "user");
				}
			}
			serializer.endTag(null, "users");
			serializer.endDocument();
			serializer.flush();
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, "Error in SaveProfrileXml", e);
		}
		return;
	}

	public static String getMD5Hash() {
		if (isNullOrEmpty(Global.MD5Hash) && Global.AppContext != null) {
			try {
				File file = new File(Global.AppContext.getPackageCodePath());
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] array = new byte[(int) file.length()];
				int read;
				for (int i = 0; i < array.length; i += read) {
					read = fileInputStream.read(array, i, array.length - i);
					if (read < 0) break;
				}
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
}