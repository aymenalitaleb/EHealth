package esi.siw.e_health.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

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
import esi.siw.e_health.Dashboard;
import esi.siw.e_health.FirstChangePassword;
import esi.siw.e_health.PassifAccount;

/**
 * Created by Creator on 28/03/2018.
 */

public class LoginTask extends AsyncTask {

    private SessionManagement session;
    StringBuilder sb = new StringBuilder();
    SweetAlertDialog sweetAlertDialog;
    private Context context;

    public LoginTask(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitle("Logging ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            session = new SessionManagement(context.getApplicationContext());

            String email = (String) objects[0];
            String password = (String) objects[1];

            String link = "http://malitaleb.000webhostapp.com/login.php";
            String data = URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(email, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(password, "UTF-8");

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
            return sb.toString() + email + password;


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
        String response = (String) o;
//        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String queryResult = jsonObject.getString("query_result");
                if (queryResult.equals("SUCCESS")) {
                    int idPatient = jsonObject.getInt("idPatient");
                    String Nom = jsonObject.getString("Nom");
                    String Prenom = jsonObject.getString("Prenom");
                    String Date_Naissance = jsonObject.getString("Date_Naissance");
                    String Lieu_Naissance = jsonObject.getString("Lieu_Naissance");
                    String Avatar = jsonObject.getString("Avatar");
                    String Email = jsonObject.getString("Email");
                    String Sexe = jsonObject.getString("Sexe");
                    String Age = jsonObject.getString("Age");
                    String PremiereFois = jsonObject.getString("PremiereFois");
                    String Etat = jsonObject.getString("Etat");
                    session.createLoginSession(idPatient, Nom, Prenom, Lieu_Naissance, Date_Naissance, Email, Sexe, Avatar, Age, Etat);

                    Intent intent;
                    if (Etat.equals("passif")) {
                        intent = new Intent(context, PassifAccount.class);
                    } else if (PremiereFois.equals("non")) {
                        intent = new Intent(context, Dashboard.class);
                    } else {
                        intent = new Intent(context, FirstChangePassword.class);
                        intent.putExtra("Email",Email);
                        intent.putExtra("idPatient",idPatient);
                    }

                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Nom d'utilisateur et le mot de passe ne se correspondre pas !")
                            .show();
                }
            } catch (JSONException e) {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Une erreur est survenue !")
                        .show();
            }
        }
        sweetAlertDialog.dismiss();

    }
}
