package esi.siw.e_health;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.*;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import esi.siw.e_health.common.Common;
import esi.siw.e_health.tasks.GetQuestionnaire;
import esi.siw.e_health.tasks.SessionManagement;
import esi.siw.e_health.tasks.ValidateSurvey;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    // Session Manager Class
    SessionManagement session;
    View view;


    Context context;
    LinearLayout linearLayout;
    Button validateQuestionnaire;
    RelativeLayout relativeLayout;
    JSONObject jsonObject;
    Button questionnaireBtn, consginesBtn;

    public QuestionnaireFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questionnaire, container, false);
        validateQuestionnaire = view.findViewById(R.id.validateQuestionnaire);
        validateQuestionnaire.setOnClickListener(this);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        linearLayout = view.findViewById(R.id.linearLayout);

        scrollView.removeAllViews();
        scrollView.addView(linearLayout);

        relativeLayout.removeAllViews();
        relativeLayout.addView(scrollView);
        relativeLayout.addView(validateQuestionnaire);

        session = new SessionManagement(getActivity());
        HashMap<String, String> userData = session.getUserDetails();
        int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));
        if (!Common.isFileExist("questionnaire.json")) {
            new GetQuestionnaire(getActivity()).execute(idPatient);
        }

        getQuestions();

        // Inflate the layout for this fragment
        return relativeLayout;

    }


    public void getQuestions() {
        try {
            jsonObject = new JSONObject(Common.readFromFile("questionnaire.json", getActivity()));

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
                        TextView question = new TextView(getContext());
                        jsonObject = jsonArray.getJSONObject(i);



                        // Peremeters of the view
                        question.setText(jsonObject.getString("Question"));
                        question.setId(jsonObject.getInt("idQuestion"));
                        question.setTextSize(25);
                        question.setTextColor(getResources().getColor(R.color.colorQuestionText));
                        question.setGravity(Gravity.CENTER);
                        question.setAnimation(alphaAnimation);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        question.setLayoutParams(params);

                        LinearLayout questionContainer = new LinearLayout(getContext());
                        questionContainer.setLayoutParams(params);
                        questionContainer.setOrientation(LinearLayout.VERTICAL);
                        questionContainer.setAnimation(alphaAnimation);

                        View view = new View(getContext());
                        LinearLayout.LayoutParams horizentalBar = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,1
                        );
                        view.setBackgroundColor(getResources().getColor(R.color.colorQuestionText));
                        view.setLayoutParams(horizentalBar);
                        view.setAnimation(alphaAnimation);


                        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        cardViewParams.setMargins(25,25,25,25);
                        CardView cardView = new CardView(getContext());
                        cardView.setLayoutParams(cardViewParams);
                        cardView.setAnimation(alphaAnimation);

                        questionContainer.addView(question);
                        questionContainer.addView(view);
                        cardView.addView(questionContainer);
                        linearLayout.addView(cardView);


                        // Getting choices
                        JSONArray choix = jsonObject.getJSONArray("Choix");
                        for (int j = 0; j < choix.length(); j++) {

                            CheckBox choi = new CheckBox(getContext());

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
            Log.e("errooooooooooooooooor2", e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateQuestionnaire:
                validateSurvey();
                break;

        }
    }

    private void validateSurvey() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Do you confirm your response of the survey ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            JSONObject jsonObject = new JSONObject(Common.readFromFile("questionnaire.json", getActivity()));
                            JSONObject reponsesQuestionnaire = new JSONObject();
                            reponsesQuestionnaire.put("idQuestionnaire", jsonObject.getInt("idQuestionnaire"));
                            reponsesQuestionnaire.put("Repondu", "oui");

                            JSONArray questionnaireReponse = new JSONArray();

                            // Loop through CardViews
                            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                                CardView view = (CardView) linearLayout.getChildAt(i);
                                LinearLayout questionContainer = (LinearLayout) view.getChildAt(0);
                                JSONArray lesChoix =  new JSONArray();
                                JSONObject question = new JSONObject();
                                // Loop through questionContainer
                                JSONObject lastQuestion = new JSONObject();
                                for (int j=0; j<questionContainer.getChildCount(); j++) {
                                    View view2 = questionContainer.getChildAt(j);
                                    JSONObject choix = new JSONObject();
                                    if (j>1) {
                                        // Questions' choices
                                        choix.put("idChoix", view2.getId());
                                        choix.put("Choix", ((CheckBox) view2).getText().toString());
                                        choix.put("idQuestion",lastQuestion.getInt("idQuestion"));
                                        // I had boolean problem
                                        if (((CheckBox) view2).isChecked()) {
                                            choix.put("Choisi", "oui");
                                        } else {
                                            choix.put("Choisi", "non");
                                        }
                                        lesChoix.put(choix);
                                        Log.e("lesChoix", lesChoix.toString());
                                        question.put("Choix", lesChoix);
                                    } else if (view2 instanceof TextView) {
                                        // It means it's the question text
                                        question.put("idQuestion", view2.getId());
                                        question.put("Question",  ((TextView) view2).getText().toString());
                                        lastQuestion = question;
                                    }

                                }
                                questionnaireReponse.put(question);
                            }
                            reponsesQuestionnaire.put("Questions",questionnaireReponse);
                            Log.e("JSON FILE2", String.valueOf(reponsesQuestionnaire));
                            new ValidateSurvey(getContext(), linearLayout, validateQuestionnaire).execute(reponsesQuestionnaire.toString());


                        } catch (JSONException e) {
                            Log.e("erroooooooooooooButton",e.getMessage());
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }
    private void getQuestionsAnswered() {
        try {
            jsonObject = new JSONObject(Common.readFromFile("questionnaire.json", getActivity()));

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
                    TextView question = new TextView(getContext());
                    jsonObject = jsonArray.getJSONObject(i);



                    // Peremeters of the view
                    question.setText(jsonObject.getString("Question"));
                    question.setId(jsonObject.getInt("idQuestion"));
                    question.setTextSize(25);
                    question.setTextColor(getResources().getColor(R.color.colorQuestionText));
                    question.setGravity(Gravity.CENTER);
                    question.setAnimation(alphaAnimation);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    question.setLayoutParams(params);

                    LinearLayout questionContainer = new LinearLayout(getContext());
                    questionContainer.setLayoutParams(params);
                    questionContainer.setOrientation(LinearLayout.VERTICAL);
                    questionContainer.setAnimation(alphaAnimation);

                    View view = new View(getContext());
                    LinearLayout.LayoutParams horizentalBar = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,1
                    );
                    view.setBackgroundColor(getResources().getColor(R.color.colorQuestionText));
                    view.setLayoutParams(horizentalBar);
                    view.setAnimation(alphaAnimation);


                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    cardViewParams.setMargins(25,25,25,25);
                    CardView cardView = new CardView(getContext());
                    cardView.setLayoutParams(cardViewParams);
                    cardView.setAnimation(alphaAnimation);

                    questionContainer.addView(question);
                    questionContainer.addView(view);
                    cardView.addView(questionContainer);
                    linearLayout.addView(cardView);


                    // Getting choices
                    JSONArray choix = jsonObject.getJSONArray("Choix");
                    for (int j = 0; j < choix.length(); j++) {

                        CheckBox choi = new CheckBox(getContext());

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
            Log.e("errooooooooooooooooor2", e.getMessage());
        }

    }

}
