package esi.siw.e_health.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Common {

//    public static void writeToFile(String data, String fileName, Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        } catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//
//    public static String readFromFile(String fileName, Activity activity) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = activity.openFileInput(fileName);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        } catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//
//        return ret;
//    }

    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();

    }

    public static boolean isConnectedToInternet (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public static void addJsonFile(Context context, String jsonName, String jsonFile) {
        if (context != null) {
            SharedPreferences pref = context.getSharedPreferences(jsonName, 0); // 0 == Private Mode;
            SharedPreferences.Editor editor;

            editor = pref.edit();
            editor.putString(jsonName, jsonFile);
            editor.apply();
        }
    }

    public static String getJsonFile(Context context, String jsonName) {
        SharedPreferences pref;
        String jsonFile = "";
        if (context != null) {
            pref = context.getSharedPreferences(jsonName, 0); // 0 == Private Mode;
            jsonFile = pref.getString(jsonName, "");
            Log.e("pref", pref.getString(jsonName, ""));

        }
        return jsonFile;
    }

}
