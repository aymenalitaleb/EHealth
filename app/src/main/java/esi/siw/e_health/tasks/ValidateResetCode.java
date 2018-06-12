package esi.siw.e_health.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import esi.siw.e_health.R;

public class ValidateResetCode extends AsyncTask {

    private Context context;
    private ProgressDialog progressDialog;

    public ValidateResetCode(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait ..."); // Setting Message
        progressDialog.setTitle("Validating the code"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
        progressDialog.show(); // Display Progress Dialog

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
                            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                                try {
                                    int idPatient = jsonObject.getInt("idPatient");
                                    progressDialog.dismiss();
                                    new ChangePasswordReset(context).execute(idPatient, password.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "Passwords are not equal !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                    break;
                case "CODE":
                    Toast.makeText(context, "Code has expired", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "There was an error, please try again !", Toast.LENGTH_SHORT).show();
                    break;
            }

        } catch (JSONException e) {
            Log.e("jsonException",e.getMessage());
        }
        progressDialog.dismiss();
    }


}