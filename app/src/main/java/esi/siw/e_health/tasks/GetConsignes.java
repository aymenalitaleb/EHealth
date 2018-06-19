package esi.siw.e_health.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import esi.siw.e_health.common.Common;

public class GetConsignes extends AsyncTask {

    SessionManagement session;
    StringBuilder sb = new StringBuilder();
    SweetAlertDialog sweetAlertDialog;
    private Context context;
    JSONArray jsonArray;
    Activity activity;
    LinearLayout linearLayout;
    LinearLayout noConsigne;
    LinearLayout connectionProblem;

    public GetConsignes(Context context, SweetAlertDialog sweetAlertDialog, Activity activity,
                        LinearLayout linearLayout, LinearLayout noConsigne, LinearLayout connectionProblem) {
        this.context = context;
        this.sweetAlertDialog = sweetAlertDialog;
        this.activity = activity;
        this.linearLayout = linearLayout;
        this.noConsigne = noConsigne;
        this.connectionProblem = connectionProblem;
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
        sweetAlertDialog.dismiss();
        String response = (String) o;
        if (response != null) {
            if (response.trim().charAt(0) == '[') {
                Common.addJsonFile(context, "consignes", response);
                noConsigne.setVisibility(View.GONE);
                connectionProblem.setVisibility(View.GONE);
                getConsignes();
            } else {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Il n'y a aucune consigne pour vous ! ")
                        .show();
            }
        } else {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Une érreure est survenue !")
                    .show();
        }
    }

    private void getConsignes() {
        Log.e("geSonsignesFunc", "yes");
        try{
            jsonArray = new JSONArray(Common.getJsonFile(context, "consignes"));
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(200);
            alphaAnimation.setFillAfter(true);


            // Loop through "consignes"
            for (int i=0; i<jsonArray.length();i++) {

                View view = new View(context);
                LinearLayout.LayoutParams horizentalBar = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,1
                );
                view.setBackgroundColor(activity.getResources().getColor(R.color.colorQuestionText));
                view.setLayoutParams(horizentalBar);
                view.setAnimation(alphaAnimation);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout consignesContainer = new LinearLayout(context);
                consignesContainer.setLayoutParams(params);
                consignesContainer.setOrientation(LinearLayout.VERTICAL);
                consignesContainer.setAnimation(alphaAnimation);


                LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardViewParams.setMargins(25,25,25,25);
                CardView cardView = new CardView(context);
                cardView.setLayoutParams(cardViewParams);
                cardView.setAnimation(alphaAnimation);

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TextView operation = new TextView(context);

                if (jsonObject.getString("Operation").equals("pre")) {
                    operation.setText("Pre-Operation");
                } else {
                    operation.setText("Post-Operation");
                }
                operation.setTextSize(25);
                operation.setTextColor(activity.getResources().getColor(R.color.colorQuestionText));
                operation.setGravity(Gravity.CENTER);
                operation.setAnimation(alphaAnimation);

                TextView consignes = new TextView(context);
                consignes.setText(jsonObject.getString("Consigne"));
                consignes.setTextSize(17);
                consignes.setLayoutParams(params);

                consignesContainer.addView(operation);
                consignesContainer.addView(view);
                consignesContainer.addView(consignes);
                cardView.addView(consignesContainer);
                linearLayout.addView(cardView);
            }
        } catch (JSONException e) {
            Log.e("errorJsonGetConsignes", e.getMessage());
        }

    }
}
