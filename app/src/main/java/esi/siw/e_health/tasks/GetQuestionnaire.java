package esi.siw.e_health.tasks;

import android.app.ActionBar;
import android.app.Activity;
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
import esi.siw.e_health.Dashboard;
import esi.siw.e_health.R;
import esi.siw.e_health.common.Common;

public class GetQuestionnaire extends AsyncTask {

    SessionManagement session;
    SweetAlertDialog sweetAlertDialog;
    private LinearLayout noSurvey;
    private LinearLayout connectionProblem;
    private ActionBar toolbar;
    private Context context;

    LinearLayout linearLayout;
    Button validateQuestionnaire;
    JSONObject jsonObject;
    Dashboard activity;


    public GetQuestionnaire(Context context, Dashboard activity, LinearLayout linearLayout,
                            Button validateQuestionnaire, SweetAlertDialog sweetAlertDialog,
                            LinearLayout noSurvey, LinearLayout connectionProblem ) {
        this.context = context;
        this.activity = activity;
        this.linearLayout = linearLayout;
        this.validateQuestionnaire = validateQuestionnaire;
        this.sweetAlertDialog = sweetAlertDialog;
        this.noSurvey = noSurvey;
        this.connectionProblem = connectionProblem;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            session = new SessionManagement(context.getApplicationContext());

            int idPatient = (Integer) objects[0];

            String link = "http://malitaleb.000webhostapp.com/getQuestionnaire.php";
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
            return "UnsupportedEncodingException: " + e.getMessage();
        } catch (MalformedURLException e) {
            return "MalformedURLException: " + e.getMessage();
        } catch (IOException e) {
            return "IOException: " + e.getMessage();
        }
    }


    @Override
    protected void onPostExecute(Object o) {
        sweetAlertDialog.dismiss();
        String response = (String) o;
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("query_result")) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vous n'avez pas de questionnaire pour le moment ")
                            .show();
                } else {
                    Common.addJsonFile(context, "questionnaire", response);
                    noSurvey.setVisibility(View.GONE);
                    connectionProblem.setVisibility(View.GONE);
                    getQuestions();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Nous n'avons pas pu récupérer le questionnaire, réessayez plus tard ! ")
                    .show();
        }
    }

    private void getQuestions() {
        try {
            Log.e("jsonObject", Common.getJsonFile(context, "questionnaire"));
            jsonObject = new JSONObject(Common.getJsonFile(context,"questionnaire"));

            activity.setActionBarTitle(jsonObject.getString("Questionnaire"));

            // Check if it's answered
            if (jsonObject.getString("Repondu").equals("oui")) {
                getQuestionsAnswered();
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
                        question.setTextColor(activity.getResources().getColor(R.color.colorQuestionText));
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
                        view.setBackgroundColor(activity.getResources().getColor(R.color.colorQuestionText));
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
            Log.e("getQuestionTask", e.getMessage());
        }

    }

    private void getQuestionsAnswered() {
        try {
            jsonObject = new JSONObject(Common.getJsonFile(context, "questionnaire"));

            JSONArray jsonArray = jsonObject.getJSONArray("Questions");
            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    validateQuestionnaire.setEnabled(false);
                    validateQuestionnaire.setText("SURVEY ALREADY ANSWERED");
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
                    question.setTextColor(activity.getResources().getColor(R.color.colorQuestionText));
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
                    view.setBackgroundColor(activity.getResources().getColor(R.color.colorQuestionText));
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
        } catch (JSONException e) {
            Log.e("getQuestionsAnsweredTak", e.getMessage());
        }

    }
}
