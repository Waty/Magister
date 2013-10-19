package com.wart.magister;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Data {
	public static void Clear() {
		SharedPreferences.Editor editor = Global.getSharedPreferences().edit();
		editor.clear();
		editor.commit();
	}

	public static void initializeApp(Context context) {
		setAppFolder(context.getApplicationInfo().dataDir);
	}

	public static String getAppFolder() {
		return Global.getSharedPreferences().getString("appfolder", "");
	}

	public static String getAppName() {
		return "Meta";
	}

	public static String getApplicationName() {
		return "Meta 2011-2012";
	}

	public static String getDeviceCode() {
		return Global.getSharedPreferences().getString("devicecode", "");
	}

	public static String getFullName() {
		return Global.getSharedPreferences().getString("naam", "");
	}

	public static String getKey() {
		return Global.getSharedPreferences().getString("key", "");
	}

	public static String getLicense() {
		return Global.getSharedPreferences().getString("licentie", "");
	}

	public static String getMagisterSuite() {
		return Global.getSharedPreferences().getString("magistersuite", "");
	}

	protected static String getMediusForwarder() {
		return "/mwp/mobile/meta.medius.axd";
	}

	public static String getMediusURL() {
		return Global.getSharedPreferences().getString("medius", "");
	}

	public static String getMediusVersion() {
		return Global.getSharedPreferences().getString("mediusversion", "");
	}

	public static String getMenuItemName() {
		return Global.getSharedPreferences().getString("menuitemname", "");
	}

	public static boolean getNormalExit() {
		return Global.getSharedPreferences().getBoolean("normalexit", true);
	}

	public static boolean getProfileExists() {
		return Global.getSharedPreferences().getBoolean("profileexists", true);
	}

	public static int getProfilePosition() {
		return Global.getSharedPreferences().getInt("profileposition", -1);
	}

	public static String getRol() {
		return "leerling";
	}

	public static int getSchoolID() {
		return Global.getSharedPreferences().getInt("schoolid", -1);
	}

	public static String getUsername() {
		return Global.getSharedPreferences().getString("code", "");
	}

	public static int getUserId() {
		return Global.getSharedPreferences().getInt("idgebr", -1);
	}

	public static int setStudendId() {
		return Global.getSharedPreferences().getInt("idleer", -1);
	}

	public static int getEmployeeId() {
		return Global.getSharedPreferences().getInt("idpers", -1);
	}

	public static int getIdType() {
		return Global.getSharedPreferences().getInt("idtype", -1);
	}

	public static void setAppFolder(String paramString) {
		Global.setSharedPreferenceValues("appfolder", paramString);
	}

	public static void setDeleteProfile(Boolean paramBoolean) {
		Global.setSharedPreferenceValues("deleteprofile", paramBoolean);
	}

	public static void setDeviceCode(String paramString) {
		Global.setSharedPreferenceValues("devicecode", paramString);
	}

	public static void setFullName(String paramString) {
		Global.setSharedPreferenceValues("naam", paramString);
	}

	public static void setKey(String paramString) {
		Global.setSharedPreferenceValues("key", paramString);
	}

	public static void setLicense(String paramString) {
		Global.setSharedPreferenceValues("licentie", paramString);
	}

	public static void setMagisterSuite(String paramString) {
		Global.setSharedPreferenceValues("magistersuite", paramString);
	}

	public static void setMediusURL(String paramString) {
		Global.setSharedPreferenceValues("medius", buildMediusUrl(paramString));
	}

	public static void setMediusVersion(String paramString) {
		Global.setSharedPreferenceValues("mediusversion", paramString);
	}

	public static void setMenuItemName(String paramString) {
		Global.setSharedPreferenceValues("menuitemname", paramString);
	}

	public static void setProfilePosition(int paramInt) {
		Global.setSharedPreferenceValues("profileposition", Integer.valueOf(paramInt));
	}

	public static void setRol(String paramString) {
		Global.setSharedPreferenceValues("rol", paramString);
	}

	public static void setSchoolID(int paramInt) {
		Global.setSharedPreferenceValues("schoolid", Integer.valueOf(paramInt));
	}

	public static void setUsername(String paramString) {
		Global.setSharedPreferenceValues("code", paramString);
	}

	public static void setUserId(int paramInt) {
		Global.setSharedPreferenceValues("idgebr", Integer.valueOf(paramInt));
	}

	public static void setStudentId(int paramInt) {
		Global.setSharedPreferenceValues("idleer", Integer.valueOf(paramInt));
	}

	public static void setEmployeeId(int paramInt) {
		Global.setSharedPreferenceValues("idpers", Integer.valueOf(paramInt));
	}

	public static void setIdType(int paramInt) {
		Global.setSharedPreferenceValues("idtype", Integer.valueOf(paramInt));
	}

	public static String buildMediusUrl(final String url) {
		final String formattedUrl = formatMediusUrl(url);
		if (formattedUrl.length() > 1 && !formattedUrl.endsWith("/mwp/mobile/meta.medius.axd")) return String.valueOf(formattedUrl) + "/mwp/mobile/meta.medius.axd";
		return "";
	}

	public static String formatMediusUrl(String medius) {
		if (medius == null || medius.length() < 1) return "";

		if (!medius.startsWith("http")) medius = "https://" + medius;

		else if (medius.startsWith("http://")) medius = medius.replaceFirst("http", "https");

		final Uri parse = Uri.parse(medius);
		final String authority = parse.getAuthority();
		if (authority != null) {
			String scheme = parse.getScheme();
			if (!scheme.equalsIgnoreCase("https")) scheme = "https";
			return String.format("%s://%s", scheme, authority);
		}
		return "";
	}
}