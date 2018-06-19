package esi.siw.e_health.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangePassword extends AsyncTask {

    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public ChangePassword(Context context, SweetAlertDialog sweetAlertDialog) {
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
            String oldPassword = (String) objects[1];
            String newPassword = (String) objects[2];

            String link = "http://malitaleb.000webhostapp.com/changePassword.php";
            String data = URLEncoder.encode("idPatient", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(idPatient), "UTF-8")+ "&"
                    + URLEncoder.encode("oldPassword", "UTF-8") + "="
                    + URLEncoder.encode(oldPassword, "UTF-8") + "&"
                    + URLEncoder.encode("newPassword", "UTF-8") + "="
                    + URLEncoder.encode(newPassword, "UTF-8");


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
        String result = (String) o;
        Log.e("result", result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("query_result")) {
                switch (jsonObject.getString("query_result")) {
                    case "SUCCESS":
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Mot de passe changé")
                                .show();
                        break;
                    case "PASSWORD":
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Ancien mot de passe est incorrecte !")
                                .show();
                        break;
                    default:
                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Une érreure est survenue !")
                                .show();
                        break;
                }
            }  else {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Une érreure est survenue !")
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sweetAlertDialog.dismiss();
    }

}
