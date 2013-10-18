package com.wart.magister;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Data {
	public static void Clear() {
		SharedPreferences.Editor localEditor = Global.GetSharedPreferenceValue("profile").edit();
		localEditor.clear();
		localEditor.commit();
	}

	public static void ClearExitAndAppFolder() {
		SetNormalExit(Boolean.valueOf(false));
	}

	public static String GetAppFamily() {
		return Global.GetSharedPreferenceValue("profile").getString("appfamily", "");
	}

	public static String GetAppFolder() {
		return Global.GetSharedPreferenceValue("profile").getString("appfolder", "");
	}

	public static String GetAppName() {
		return Global.GetSharedPreferenceValue("profile").getString("appname", "");
	}

	public static String GetApplicatieNaam() {
		return Global.GetSharedPreferenceValue("profile").getString("applicatienaam", "");
	}

	public static boolean GetApplicationRunning() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("applicationrunning", false);
	}

	public static boolean GetBetaald() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("betaald", false);
	}

	public static String GetBetalingsURL() {
		return Global.GetSharedPreferenceValue("profile").getString("betalingsurl", "");
	}

	public static String GetDatabase() {
		return Global.GetSharedPreferenceValue("profile").getString("db", "");
	}

	@SuppressWarnings("deprecation")
	public static Date GetDateModified() {
		return new Date(Global.GetSharedPreferenceValue("profile").getString("datemodified", Global.NODATE.toGMTString()));
	}

	public static boolean GetDeleteProfile() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("deleteprofile", false);
	}

	public static String GetDeviceCode() {
		return Global.GetSharedPreferenceValue("profile").getString("devicecode", "");
	}

	public static String GetFullName() {
		return Global.GetSharedPreferenceValue("profile").getString("naam", "");
	}

	public static boolean GetIsExecuted() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("isexecuted", false);
	}

	public static String GetKey() {
		return Global.GetSharedPreferenceValue("profile").getString("key", "");
	}

	public static String getLicense() {
		return Global.GetSharedPreferenceValue("profile").getString("licentie", "");
	}

	public static boolean GetLoaded() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("loaded", false);
	}

	public static String GetMagisterSuite() {
		return Global.GetSharedPreferenceValue("profile").getString("magistersuite", "");
	}

	protected static String GetMediusForwarder() {
		return Global.GetSharedPreferenceValue("profile").getString("mediusforwarder", "");
	}

	public static String GetMediusURL() {
		return Global.GetSharedPreferenceValue("profile").getString("medius", "");
	}

	public static String GetMediusVersion() {
		return Global.GetSharedPreferenceValue("profile").getString("mediusversion", "");
	}

	public static String GetMenuItemName() {
		return Global.GetSharedPreferenceValue("profile").getString("menuitemname", "");
	}

	public static boolean GetNormalExit() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("normalexit", true);
	}

	public static boolean GetProfileExists() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("profileexists", true);
	}

	public static int GetProfilePosition() {
		return Global.GetSharedPreferenceValue("profile").getInt("profileposition", -1);
	}

	public static String GetRol() {
		return Global.GetSharedPreferenceValue("profile").getString("rol", "");
	}

	public static int GetSchoolID() {
		return Global.GetSharedPreferenceValue("profile").getInt("schoolid", -1);
	}

	public static boolean GetServiceRunning() {
		return Global.GetSharedPreferenceValue("profile").getBoolean("servicerunning", false);
	}

	public static String GetUser() {
		return Global.GetSharedPreferenceValue("profile").getString("code", "");
	}

	public static int GetidGebr() {
		return Global.GetSharedPreferenceValue("profile").getInt("idgebr", -1);
	}

	public static int GetidLeer() {
		return Global.GetSharedPreferenceValue("profile").getInt("idleer", -1);
	}

	public static int GetidPers() {
		return Global.GetSharedPreferenceValue("profile").getInt("idpers", -1);
	}

	public static int GetidType() {
		return Global.GetSharedPreferenceValue("profile").getInt("idtype", -1);
	}

	public static void InitializeApp(Context paramContext) {
		String str = paramContext.getPackageName();
		// ResourceManager.setBaseName(str);
		int i = paramContext.getResources().getIdentifier("forwarder", "string", str);
		int j = paramContext.getResources().getIdentifier("app_name", "string", str);
		SetMediusForwarder(paramContext.getResources().getString(i));
		SetAppName(paramContext.getResources().getString(j));
		SetAppFamily(GetAppName());
		if (GetAppFamily().startsWith("a")) SetAppFamily(GetAppFamily().substring(1));
		SetAppFolder(paramContext.getApplicationInfo().dataDir);
	}

	public static void SetAppFamily(String paramString) {
		Global.SetSharedPreferenceValues("profile", "appfamily", paramString);
	}

	public static void SetAppFolder(String paramString) {
		Global.SetSharedPreferenceValues("profile", "appfolder", paramString);
	}

	public static void SetAppName(String paramString) {
		Global.SetSharedPreferenceValues("profile", "appname", paramString);
	}

	public static void SetApplicatieNaam(String paramString) {
		Global.SetSharedPreferenceValues("profile", "applicatienaam", paramString);
	}

	public static void SetApplicationRunning(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "applicationrunning", paramBoolean);
	}

	public static void SetBetaald(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "betaald", paramBoolean);
	}

	public static void SetBetalingsURL(String paramString) {
		Global.SetSharedPreferenceValues("profile", "betalingsurl", paramString);
	}

	public static void SetDatabase(String paramString) {
		Global.SetSharedPreferenceValues("profile", "db", paramString);
	}

	public static void SetDateModified(Date paramDate) {
		Global.SetSharedPreferenceValues("profile", "datemodified", paramDate);
	}

	public static void SetDeleteProfile(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "deleteprofile", paramBoolean);
	}

	public static void SetDeviceCode(String paramString) {
		Global.SetSharedPreferenceValues("profile", "devicecode", paramString);
	}

	public static void SetFullName(String paramString) {
		Global.SetSharedPreferenceValues("profile", "naam", paramString);
	}

	public static void SetIsExecuted(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "isexecuted", paramBoolean);
	}

	public static void SetKey(String paramString) {
		Global.SetSharedPreferenceValues("profile", "key", paramString);
	}

	public static void setLicense(String paramString) {
		Global.SetSharedPreferenceValues("profile", "licentie", paramString);
	}

	public static void SetLoaded(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "loaded", paramBoolean);
	}

	public static void SetMagisterSuite(String paramString) {
		Global.SetSharedPreferenceValues("profile", "magistersuite", paramString);
	}

	protected static void SetMediusForwarder(String paramString) {
		Global.SetSharedPreferenceValues("profile", "mediusforwarder", paramString);
	}

	public static void SetMediusURL(String paramString) {
		Global.SetSharedPreferenceValues("profile", "medius", buildMediusUrl(paramString));
	}

	public static void SetMediusVersion(String paramString) {
		Global.SetSharedPreferenceValues("profile", "mediusversion", paramString);
	}

	public static void SetMenuItemName(String paramString) {
		Global.SetSharedPreferenceValues("profile", "menuitemname", paramString);
	}

	public static void SetNormalExit(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "normalexit", paramBoolean);
	}

	public static void SetProfileExists(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "profileexists", paramBoolean);
	}

	public static void SetProfilePosition(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "profileposition", Integer.valueOf(paramInt));
	}

	public static void SetRol(String paramString) {
		Global.SetSharedPreferenceValues("profile", "rol", paramString);
	}

	public static void SetSchoolID(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "schoolid", Integer.valueOf(paramInt));
	}

	public static void SetServiceRunning(Boolean paramBoolean) {
		Global.SetSharedPreferenceValues("profile", "servicerunning", paramBoolean);
	}

	public static void SetUser(String paramString) {
		Global.SetSharedPreferenceValues("profile", "code", paramString);
	}

	public static void SetidGebr(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "idgebr", Integer.valueOf(paramInt));
	}

	public static void SetidLeer(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "idleer", Integer.valueOf(paramInt));
	}

	public static void SetidPers(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "idpers", Integer.valueOf(paramInt));
	}

	public static void SetidType(int paramInt) {
		Global.SetSharedPreferenceValues("profile", "idtype", Integer.valueOf(paramInt));
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