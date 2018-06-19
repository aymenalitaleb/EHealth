package esi.siw.e_health.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.Dashboard;
import esi.siw.e_health.PassifAccount;

public class CheckAccount extends AsyncTask{

    private SweetAlertDialog sweetAlertDialog;
    SessionManagement session;
    private int idPatient;
    private Context context;
    String Etat;


    public CheckAccount(Context context, SweetAlertDialog sweetAlertDialog, int idPatient) {
        this.context = context;
        this.sweetAlertDialog = sweetAlertDialog;
        this.idPatient = idPatient;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            session = new SessionManagement(context);
            HashMap<String, String> userData = session.getUserDetails();
            Etat = userData.get(SessionManagement.KEY_ETAT);

            String link = "http://malitaleb.000webhostapp.com/checkAccount.php";
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

            // The response will be read by the onPostExecute method
            return sb.toString();


        } catch (UnsupportedEncodingException e) {
            return new String("UnsupportedEncodingException: " + e.getMessage());
        } catch (MalformedURLException e) {
            return new String("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            return new String("IOException: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        sweetAlertDialog.dismiss();
        String response = (String) o;
        Log.e("response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("query_result")) {
                if (jsonObject.getString("query_result").equals("SUCCESS")) {
                    String Etat = jsonObject.getString("Etat");
                    Intent intent;
                    if (Etat.equals("actif") && this.Etat.equals("passif") ) {
                        intent = new Intent(context, Dashboard.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else if (Etat.equals("passif") && this.Etat.equals("actif")) {
                        intent = new Intent(context, PassifAccount.class);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    }

                }
            }
        } catch (JSONException e) {
            Log.e("checkAccount", e.getMessage());
        }
    }
}
