package esi.siw.e_health.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.R;

public class ValidateSurvey extends AsyncTask {

    private Context context;
    private SweetAlertDialog sweetAlertDialog;
    private LinearLayout linearLayout;
    JSONObject jsonObject;
    private Button validateQuestionnaire;
    private SweetAlertDialog sweetAlertDialog2;


    public ValidateSurvey(Context context, LinearLayout linearLayout, Button validateQuestionnaire, SweetAlertDialog sweetAlertDialog2) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.validateQuestionnaire = validateQuestionnaire;
        this.sweetAlertDialog2 = sweetAlertDialog2;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            String jsonFile = (String) objects[0];

            String link = "http://malitaleb.000webhostapp.com/validateSurvey.php";
            String data = URLEncoder.encode("jsonFile", "UTF-8") + "="
                    + URLEncoder.encode(String.valueOf(jsonFile), "UTF-8") ;


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
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Le questionnaire à été validé !")
                            .show();
                    linearLayout.removeAllViewsInLayout();
                    validateQuestionnaire.setEnabled(false);
                    validateQuestionnaire.setText("Questionnaire répondu !");
                    getQuestions();
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
        sweetAlertDialog2.dismiss();
    }

    private void getQuestions() {
        try {
            jsonObject = new JSONObject(readFromFile("questionnaire.json"));

            // Check if it's answered
            if (jsonObject.getString("Repondu").equals("oui")) {
                // Toast.makeText(context, "Le questionnaire à été repondu", Toast.LENGTH_LONG).show();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("Questions");
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setStartOffset(200);
                        alphaAnimation.setFillAfter(true);
                        // Getting question
                        TextView question = new TextView(context);
                        jsonObject = jsonArray.getJSONObject(i);



                        // Peremeters of the view
                        question.setText(jsonObject.getString("Question"));
                        question.setId(jsonObject.getInt("idQuestion"));
                        question.setTextSize(25);
                        question.setTextColor(context.getResources().getColor(R.color.colorQuestionText));
                        question.setGravity(Gravity.CENTER);
                        question.setAnimation(alphaAnimation);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        question.setLayoutParams(params);

                        LinearLayout questionContainer = new LinearLayout(context);
                        questionContainer.setLayoutParams(params);
                        questionContainer.setOrientation(LinearLayout.VERTICAL);
                        questionContainer.setAnimation(alphaAnimation);

                        View view = new View(context);
                        LinearLayout.LayoutParams horizentalBar = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,1
                        );
                        view.setBackgroundColor(context.getResources().getColor(R.color.colorQuestionText));
                        view.setLayoutParams(horizentalBar);
                        view.setAnimation(alphaAnimation);


                        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        cardViewParams.setMargins(25,25,25,25);
                        CardView cardView = new CardView(context);
                        cardView.setLayoutParams(cardViewParams);
                        cardView.setAnimation(alphaAnimation);

                        questionContainer.addView(question);
                        questionContainer.addView(view);
                        cardView.addView(questionContainer);
                        linearLayout.addView(cardView);


                        // Getting choices
                        JSONArray choix = jsonObject.getJSONArray("Choix");
                        for (int j = 0; j < choix.length(); j++) {

                            CheckBox choi = new CheckBox(context);

                            // Paremeters of the view
                            params.setMargins(5,5,5,5);
                            choi.setText(choix.getJSONObject(j).getString("Choix"));
                            choi.setId(choix.getJSONObject(j).getInt("idChoix"));
                            if (choix.getJSONObject(j).getString("Choisi").equals("oui")) {
                                choi.setChecked(true);
                            } else {
                                choi.setChecked(false);
                            }
                            choi.setEnabled(false);
                            choi.setTextSize(17);

                            choi.setLayoutParams(params);
                            questionContainer.addView(choi);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("errooooooooooooooooor2", e.getMessage());
        }

    }


    private String readFromFile(String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

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
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
