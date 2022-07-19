package com.equalinfotechuser;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreference {
    private static AppSharedPreference appSharedPrefrence;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private Context context;
    private static final String IS_LOGIN = "";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    //  public static final String KEY_AGENCY = "agencuName";


    public AppSharedPreference(Context context) {
        this.appSharedPrefs = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public static AppSharedPreference getsharedprefInstance(Context con) {
        if (appSharedPrefrence == null)
            appSharedPrefrence = new AppSharedPreference(con);
        return appSharedPrefrence;
    }

    public SharedPreferences getAppSharedPrefs() {
        return appSharedPrefs;
    }

    public void setAppSharedPrefs(SharedPreferences appSharedPrefs) {
        this.appSharedPrefs = appSharedPrefs;
    }


    public SharedPreferences.Editor getPrefsEditor() {

        return prefsEditor;
    }

    public void Commit() {
        prefsEditor.commit();
    }

    public void clearallSharedPrefernce() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public String getDate() {
        return appSharedPrefs.getString("Date", "");
    }

    public void setDate(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Date", path);
        prefsEditor.commit();
    }


    public String getBonous() {
        return appSharedPrefs.getString("Bonous", "");
    }

    public void setBonous(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Bonous", path);
        prefsEditor.commit();
    }


    public String getpunchstatus() {
        return appSharedPrefs.getString("punchstatus", "");
    }

    public void setpunchstatus(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("punchstatus", path);
        prefsEditor.commit();
    }


    public String getAll() {
        return appSharedPrefs.getString("All", "");
    }

    public void setAll(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("All", path);
        prefsEditor.commit();
    }


    public String getAlert() {
        return appSharedPrefs.getString("Alert", "");
    }

    public void setAlert(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Alert", path);
        prefsEditor.commit();
    }


    public String getaddress() {
        return appSharedPrefs.getString("address", "");
    }

    public void setaddress(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("address", path);
        prefsEditor.commit();
    }
/*
    public Boolean getLanguage() {
        return appSharedPrefs.getBoolean("language", false);
    }
*/

/*
    public void setLanguag(Boolean flag) {
        prefsEditor.putBoolean("language", flag).commit();
    }
*/


    public String getEmailId() {
        return appSharedPrefs.getString("EmailId", "");
    }

    public void setEmailId(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("EmailId", path);
        prefsEditor.commit();
    }

    public String getRegistration() {
        return appSharedPrefs.getString("Registration", "");
    }

    public void setRegistration(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Registration", path);
        prefsEditor.commit();
    }


    public String getlatitude() {
        return appSharedPrefs.getString("latitude", "");
    }


    public void setlatitude(String flag) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("latitude", flag);
        prefsEditor.commit();
    }


    public String getlongitude() {
        return appSharedPrefs.getString("longitude", "");
    }


    public void setlongitude(String flag) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("longitude", flag);
        prefsEditor.commit();
    }


    public String getEmployee_code() {
        return appSharedPrefs.getString("Employee_code", "");
    }

    public void setEmployee_code(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Employee_code", path);
        prefsEditor.commit();
    }

    public String getLanguage() {
        return appSharedPrefs.getString("Language", "");
    }

    public void setLanguage(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Language", path);
        prefsEditor.commit();
    }

    public String getLanguage_id() {
        return appSharedPrefs.getString("Language_id", "");
    }

    public void setLanguage_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Language_id", path);
        prefsEditor.commit();
    }

    public String gethome() {
        return appSharedPrefs.getString("home", "");
    }

    public void sethome(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("home", path);
        prefsEditor.commit();
    }

    public String getuser_id() {
        return appSharedPrefs.getString("user_id", "");
    }

    public void setuser_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("user_id", path);
        prefsEditor.commit();
    }

    public String getUser_id() {
        return appSharedPrefs.getString("User_id", "");
    }

    public void setUser_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("User_id", path);
        prefsEditor.commit();
    }

    public String getusertype() {
        return appSharedPrefs.getString("usertype", "");
    }

    public void setusertype(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("usertype", path);
        prefsEditor.commit();
    }

    public String getCategory() {
        return appSharedPrefs.getString("Category", "");
    }


    public void setCategory(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Category", path);
        prefsEditor.commit();
    }

    public String getDifference() {
        return appSharedPrefs.getString("Difference", "");
    }


    public void setDifference(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Difference", path);
        prefsEditor.commit();
    }

    public String getbmi_score() {
        return appSharedPrefs.getString("bmi_score", "");
    }


    public void setbmi_score(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("bmi_score", path);
        prefsEditor.commit();
    }

    public String getfirebase_id() {
        return appSharedPrefs.getString("firebase_id", "");
    }

    public void setfirebase_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("firebase_id", path);
        prefsEditor.commit();
    }

    public String gettime() {
        return appSharedPrefs.getString("time", "");
    }


    public void settime(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("time", path);
        prefsEditor.commit();
    }

    public String getcustomer_id() {
        return appSharedPrefs.getString("customer_id", "");
    }


    public void setcustomer_id(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("customer_id", path);
        prefsEditor.commit();
    }

    public String getlang() {
        return appSharedPrefs.getString("lang", "");
    }


    public void setlang(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("lang", path);
        prefsEditor.commit();
    }


    public Boolean getlanguage() {
        return appSharedPrefs.getBoolean("language", false);
    }


    public void setlanguage(Boolean flag) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putBoolean("language", flag);
        prefsEditor.commit();
    }

    public String getImage() {
        return appSharedPrefs.getString("image", "");
    }


    public void setImage(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("image", path);
        prefsEditor.commit();
    }

    public String getheader() {
        return appSharedPrefs.getString("header", "");
    }


    public void setheader(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("header", path);
        prefsEditor.commit();
    }

    public String getsubheading() {
        return appSharedPrefs.getString("subheading", "");
    }

    public void setsubheading(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("subheading", path);
        prefsEditor.commit();
    }

    public String getname() {
        return appSharedPrefs.getString("name", "");
    }

    public void setname(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("name", path);
        prefsEditor.commit();
    }

    public String getuser_type() {
        return appSharedPrefs.getString("user_type", "");
    }

    public void setuser_type(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("user_type", path);
        prefsEditor.commit();
    }

    public String getCodettype() {
        return appSharedPrefs.getString("Codettype", "");
    }

    public void setCodettype(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Codettype", path);
        prefsEditor.commit();
    }


    public String getFreewrite() {
        return appSharedPrefs.getString("Freewrite", "");
    }

    public void setFreewrite(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Freewrite", path);
        prefsEditor.commit();
    }


    public String getDailychallenge() {
        return appSharedPrefs.getString("Dailychallenge", "");
    }

    public void setDailychallenge(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Dailychallenge", path);
        prefsEditor.commit();
    }

    public String getAllattendencebyemployee() {
        return appSharedPrefs.getString("Allattendencebyemployee", "");
    }

    public void setAllattendencebyemployee(String path) {
        this.prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString("Allattendencebyemployee", path);
        prefsEditor.commit();
    }


    public void createLoginSession(String email, String password) {
        prefsEditor.putBoolean(IS_LOGIN, true);
        prefsEditor.putString(KEY_EMAIL, email);
        prefsEditor.putString(KEY_PASSWORD, password);
        //  prefsEditor.putString(KEY_AGENCY, agencuName);
        prefsEditor.commit();
    }


}
