package esi.siw.e_health.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

import esi.siw.e_health.Dashboard;
import esi.siw.e_health.MainActivity;
import esi.siw.e_health.PassifAccount;


public class SessionManagement {

    // User name (make variable public to access from outside)
    public static final String KEY_ID = "idPatient";
    public static final String KEY_NOM = "Nom";
    public static final String KEY_PRENOM = "Prenom";
    public static final String KEY_LIEU_NAISSANCE = "Date_Naissance";
    public static final String KEY_DATE_NAISSANCE = "Lieu_Naissance";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_SEXE = "Sexe";
    public static final String KEY_AVATAR = "Avatar";
    public static final String KEY_AGE = "Age";
    public static final String KEY_ETAT = "Etat";
    // Sharedpref file name
    private static final String PREF_NAME = "Auth";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;


    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(int id, String Nom, String Prenom, String Lieu_Naissance, String Date_Naissance, String Email, String Sexe, String Avatar, String Age, String Etat) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_NOM, Nom);
        editor.putString(KEY_PRENOM, Prenom);
        editor.putString(KEY_LIEU_NAISSANCE, Lieu_Naissance);
        editor.putString(KEY_DATE_NAISSANCE, Date_Naissance);
        editor.putString(KEY_EMAIL, Email);
        editor.putString(KEY_SEXE, Sexe);
        editor.putString(KEY_AVATAR, Avatar);
        editor.putString(KEY_AGE, Age);
        editor.putString(KEY_ETAT, Etat);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * @param etat
     */
    public void checkLogin(String etat) {
        // Check login status
        if (this.isLoggedIn()) {

            Intent i;
            if (etat.equals("actif")) {
                // user is not logged in redirect him to Login Activity
                i = new Intent(_context, Dashboard.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                i = new Intent(_context, PassifAccount.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            // Staring Login Activity
            _context.startActivity(i);
            ((Activity) _context).finish();

        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        // idUser
        user.put(KEY_ID, String.valueOf(pref.getInt(KEY_ID, 0)));
        user.put(KEY_NOM, pref.getString(KEY_NOM, ""));
        user.put(KEY_PRENOM, pref.getString(KEY_PRENOM, ""));
        user.put(KEY_LIEU_NAISSANCE, pref.getString(KEY_LIEU_NAISSANCE, ""));
        user.put(KEY_DATE_NAISSANCE, pref.getString(KEY_DATE_NAISSANCE, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_SEXE, pref.getString(KEY_SEXE, ""));
        user.put(KEY_AVATAR, pref.getString(KEY_AVATAR, ""));
        user.put(KEY_AGE, pref.getString(KEY_AGE, ""));
        user.put(KEY_ETAT, pref.getString(KEY_ETAT, ""));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
