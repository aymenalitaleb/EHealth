package esi.siw.e_health.tasks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import javax.security.auth.login.LoginException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.R;
import esi.siw.e_health.ResetPassword;
import esi.siw.e_health.ValidateResetCode;


public class ResetPasswordTask extends AsyncTask {

    private Context context;
    private Activity activity;
    private SweetAlertDialog sweetAlertDialog;

    private Button btnResetPassword ;
    private ImageView logo;
    private TextView txtView;
    private EditText email;

    public ResetPasswordTask(Context context, Activity activity, SweetAlertDialog sweetAlertDialog) {
        this.context = context;
        this.activity = activity;
        this.sweetAlertDialog = sweetAlertDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        initViews();
    }


    private void initViews() {
        btnResetPassword = activity.findViewById(R.id.btnResetPassword);
        logo = activity.findViewById(R.id.imgView_logo);
        email = activity.findViewById(R.id.email);
        txtView = activity.findViewById(R.id.txtView);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            String email = (String) objects[0];

            String link = "http://malitaleb.000webhostapp.com/resetPassword.php";
            String data = URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(email), "UTF-8");

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
        String response = (String) o;
        Log.e("response",response);
        try{
            JSONObject jsonObject = new JSONObject(response);
            String queryResult = jsonObject.getString("query_result");

            switch (queryResult) {
                case "SUCCESS":
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Le code de confirmation à été envoyé !")
                            .show();
                    break;
                case "EMAIL":
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("L'émail n'existe pas !")
                            .show();
                    break;
                default:
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Une érreure est survenue !")
                            .show();
                    break;
            }

        } catch (JSONException e) {
            Log.e("jsonException",e.getMessage());
        }
        sweetAlertDialog.dismiss();
        animateValidateResetCode();
    }

    private void animateValidateResetCode() {
        Intent intent = new Intent(context, ValidateResetCode.class);
        intent.putExtra("email", email.getText().toString());

        Pair[] pairs = new Pair[4];

        pairs[0] = new Pair<View, String>(logo, "logoTransition");
        pairs[1] = new Pair<View, String>(email, "emailTransition");
        pairs[2] = new Pair<View, String>(txtView, "txtViewTransition");
        pairs[3] = new Pair<View, String>(btnResetPassword, "buttonTransition");

        ActivityOptions options = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(activity, pairs);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (options != null) {
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }
        }

    }

}
