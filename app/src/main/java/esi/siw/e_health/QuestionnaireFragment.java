package esi.siw.e_health;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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

import org.json.*;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.HashMap;

import esi.siw.e_health.Tasks.GetQuestionnaire;
import esi.siw.e_health.Tasks.SessionManagement;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    // Session Manager Class
    SessionManagement session;
    View view;

    LinearLayout linearLayout;
    Button validateQuestionnaire;
    RelativeLayout relativeLayout;
    JSONArray jsonArray;
    Button questionnaireBtn, consginesBtn;

    public QuestionnaireFragment() {
        // Required empty public constructor
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
        new GetQuestionnaire(getActivity()).execute(idPatient);
        // Toast.makeText(getActivity(), readFromFile(), Toast.LENGTH_LONG).show();

        getQuestions();


        // Inflate the layout for this fragment
        return relativeLayout;

    }




    private String readFromFile(String fileName) {

        String ret = "";

        try {
            InputStream inputStream = getActivity().openFileInput(fileName);

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

    public void getQuestions() {
        try {
            jsonArray = new JSONArray(readFromFile("questionnaire.json"));
            JSONObject jsonObject = null;
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
        } catch (JSONException e) {
            Log.e("errooooooooooooooooor2", e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateQuestionnaire:

                new AlertDialog.Builder(getContext())
                    .setTitle("Confirm")
                    .setMessage("Do you confirm your response of the survey ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                JSONArray questionnaireReponse = new JSONArray();
                                int j;
                                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                                    View view = linearLayout.getChildAt(i);
                                    if (view instanceof TextView) {
                                        JSONObject question = new JSONObject();
                                        question.put("idQuestion", view.getId());
                                        question.put("Question",  ((TextView) view).getText());
                                        j = i + 1;
                                        View checkBox = linearLayout.getChildAt(j);
                                        JSONArray lesChoix =  new JSONArray();
                                        while (checkBox instanceof CheckBox) {
                                            JSONObject choix = new JSONObject();
                                            choix.put("idChoix", checkBox.getId());
                                            choix.put("Choix", ((CheckBox) checkBox).getText());
                                            choix.put("idQuestion",view.getId());
                                            choix.put("Choisi", ((CheckBox) checkBox).isChecked());
                                            lesChoix.put(choix);
                                            question.put("Choix", lesChoix);
                                            j++;
                                            checkBox = linearLayout.getChildAt(j);
                                        }
                                        i=j-1;
                                        questionnaireReponse.put(question);
                                    }
                                }
                                Log.e("JSON FILE", String.valueOf(questionnaireReponse));
                            } catch (JSONException e) {
                                Log.e("erroooooooooooooButton",e.getMessage());
                            }
                        }})
                    .setNegativeButton(android.R.string.no, null).show();

                break;

        }
    }
}
