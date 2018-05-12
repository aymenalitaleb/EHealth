package esi.siw.e_health;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import esi.siw.e_health.Tasks.GetQuestionnaire;
import esi.siw.e_health.Tasks.SessionManagement;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionnaireFragment extends Fragment {

    // Session Manager Class
    SessionManagement session;
    View view;

    LinearLayout linearLayout;
    JSONArray jsonArray;

    public QuestionnaireFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_questionnaire, container, false);
        linearLayout = view.findViewById(R.id.linearLayout);


        session = new SessionManagement(getActivity());
        HashMap<String, String> userData = session.getUserDetails();
        int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));
        new GetQuestionnaire(getActivity()).execute(idPatient);
        // Toast.makeText(getActivity(), readFromFile(), Toast.LENGTH_LONG).show();

        getQuestions();


        // Inflate the layout for this fragment
        return linearLayout;

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
                    // Getting question
                    TextView question = new TextView(getContext());
                    jsonObject = jsonArray.getJSONObject(i);
                    question.setText(jsonObject.getString("Question"));
                    question.setId(jsonObject.getInt("idQuestion"));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    question.setLayoutParams(params);
                    linearLayout.addView(question);

                    // Getting choices
                    JSONArray choix = jsonObject.getJSONArray("Choix");
                    for (int j = 0; j < choix.length(); j++) {
                        CheckBox choi = new CheckBox(getContext());
                        choi.setText(choix.getJSONObject(j).getString("Choix"));
                        choi.setLayoutParams(params);
                        linearLayout.addView(choi);
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
