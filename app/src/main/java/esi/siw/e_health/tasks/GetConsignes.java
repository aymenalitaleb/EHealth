package esi.siw.e_health.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import esi.siw.e_health.common.Common;

public class GetConsignes extends AsyncTask {

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Session Manager Class
    SessionManagement session;
    StringBuilder sb = new StringBuilder();
    ProgressDialog progressDialog;
    private Context context;

    public GetConsignes(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Chargement des consignes ..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        // progressDialog.show(); // Display Progress Dialog
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            session = new SessionManagement(context.getApplicationContext());

            int idPatient = (Integer) objects[0];

            String link = "http://malitaleb.000webhostapp.com/getConsignes.php";
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
        // progressDialog.dismiss();
        String response = (String) o;
        // Toast.makeText(context,response,Toast.LENGTH_LONG).show();
        if (response != null) {
            Common.writeToFile(response, "consignes.json", context);
        }

    }
}
