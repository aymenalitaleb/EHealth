package esi.siw.e_health.tasks;

import android.app.ProgressDialog;
import android.content.Context;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SendFeedback extends AsyncTask {


    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public SendFeedback(Context context, SweetAlertDialog sweetAlertDialog) {
        this.context = context;
        this.sweetAlertDialog = sweetAlertDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            int idPatient = (Integer) objects[0];
            String feedback = (String) objects[1];

            String link = "http://malitaleb.000webhostapp.com/sendFeedback.php";
            String data = URLEncoder.encode("idPatient", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(idPatient), "UTF-8") + "&"
                    + URLEncoder.encode("feedback", "UTF-8") + "="
                    + URLEncoder.encode(feedback, "UTF-8");

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
        try {
            JSONObject jsonObject = new JSONObject(response);
            String queryResult = jsonObject.getString("query_result");
            switch (queryResult) {
                case "SUCCESS":
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Yes ...")
                            .setContentText("Le feedback a été envoyé !")
                            .show();
                    break;
                case "INSERT":
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Une érreure est survenue !")
                            .show();
                    break;
                case "MEDECIN":
                    Toast.makeText(context, "We couldn't find your doctor, contact him for more details", Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nous n'avons pas trouver votre medecin !")
                            .show();
                    break;
                case "FAILURE":
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Une érreure est survenue !")
                            .show();
                    break;
            }
        } catch (JSONException e) {
            Log.e("jsonRespone", e.getMessage());
        }

    }
}
