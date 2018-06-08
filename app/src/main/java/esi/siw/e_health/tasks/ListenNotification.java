package esi.siw.e_health.tasks;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import esi.siw.e_health.MainActivity;
import esi.siw.e_health.R;

public class ListenNotification extends Service {

    SessionManagement sessionManagement;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("1", "service started");



        checkNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void checkNotification() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                getNotification();

                Log.e("1", "checking notification");
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(readFromFile());
                    Log.e("jsonObject", jsonObject.toString());
                    if (jsonObject.getString("query_result").equals("SUCCESS")) {
                        Log.e("2", "success");
                        showNotification();
                    } else {
                        // TODO: DELETE THIS BLOCK
                        Log.e("3","no notification");
                    }
                } catch (JSONException e) {
                    Log.e("json exception",e.getMessage());
                    e.printStackTrace();
                }
            }
        },0, 2000);

    }

    private void showNotification() {
        Log.e("4","showing notification");
        Intent intent = new Intent(this, MainActivity.class);

//      intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xffffff);

        PendingIntent notificationIntent;
        notificationIntent = PendingIntent.getActivity(getBaseContext(),uniqueInt, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setTicker("EHEALTH")
                .setContentInfo("Nouveau questionnaire")
                .setContentText("Cliquer pour le voir.")
                .setContentIntent(notificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Votre medecin vous a envoyé un nouveau questionnaire, veuillez le répondre le plus tôt possible !"))
                .setSmallIcon(R.drawable.app_icon);


        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // If you want to many notification show you need to give unique Id for each Notification
        int randomInt = new Random().nextInt(9999-1)+1;
        manager.notify(randomInt, builder.build());
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput("notifications.json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("listen notification ", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("listen notification ", "Can not read file: " + e.toString());
        }

        return ret;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("esi.siw.e_health.RestartListener");
        sendBroadcast(broadcastIntent);

    }

    public void getNotification() {
        try {

            sessionManagement = new SessionManagement(getBaseContext());
            HashMap<String, String> userData = sessionManagement.getUserDetails();
            int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));

            String link = "http://malitaleb.000webhostapp.com/getNotifications.php";
            String data = URLEncoder.encode("idPatient", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(idPatient), "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            // Sending the data to the server
            wr.write(data);
            wr.flush();

            // Create a BufferReader to read the response from the server
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Reading the response of the server
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            String response = sb.toString();
            Log.e("response ",response);
            // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
            writeToFile(response, "notifications.json");



        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.getMessage());
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException: ", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException: ", e.getMessage());
        }
    }

    private void writeToFile(String data, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
