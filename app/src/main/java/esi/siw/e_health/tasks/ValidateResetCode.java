package esi.siw.e_health.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import esi.siw.e_health.R;

public class ValidateResetCode extends AsyncTask {

    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public ValidateResetCode(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitle("Validating the code ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            String email = (String) objects[0];
            String code = (String) objects[1];

            String link = "http://malitaleb.000webhostapp.com/validateCode.php";
            String data = URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(email), "UTF-8") + "&"
                    + URLEncoder.encode("code", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(code), "UTF-8") + "&";

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
        String response = (String) o;
        Log.e("response",response);
        try{
            final JSONObject jsonObject = new JSONObject(response);
            String queryResult = jsonObject.getString("query_result");

            switch (queryResult) {
                case "SUCCESS":
                    Toast.makeText(context, "Code is valid", Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.change_password_reset_popup);
                    dialog.setTitle("Change password");
                    Button changePassword = dialog.findViewById(R.id.changePassword);
                    final TextView password= dialog.findViewById(R.id.password);
                    final TextView confirmPassword= dialog.findViewById(R.id.confirmPassword);
                    changePassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String txtPassword = password.getText().toString();
                            String txtConfirmPassword = confirmPassword.getText().toString();
                            if (txtPassword.equals(txtConfirmPassword)) {
                                if (txtPassword.length() > 7) {
                                    try {
                                        int idPatient = jsonObject.getInt("idPatient");
                                        sweetAlertDialog.dismiss();
                                        SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                                        sweetAlertDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        sweetAlertDialog2.setTitle("Changing password ...");
                                        sweetAlertDialog2.setCancelable(false);
                                        sweetAlertDialog2.show();
                                        new ChangePasswordReset(context, sweetAlertDialog2).execute(idPatient, password.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }  else {
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Le nouveau mot de passe doit contenir au moins 8 caractères !")
                                            .show();
                                }
                            } else {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Les mots de passe ne sont pas égaux !")
                                        .show();
                            }
                        }
                    });
                    dialog.show();
                    break;
                case "CODE":
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Le code à été expiré !")
                            .show();
                    break;
                default:
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Une erreur est survenue !")
                            .show();
                    break;
            }

        } catch (JSONException e) {
            Log.e("jsonException",e.getMessage());
        }
        sweetAlertDialog.dismiss();
    }


}
