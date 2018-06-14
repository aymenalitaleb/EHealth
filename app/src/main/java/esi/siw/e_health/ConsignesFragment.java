package esi.siw.e_health;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import esi.siw.e_health.common.Common;
import esi.siw.e_health.tasks.GetConsignes;
import esi.siw.e_health.tasks.SendFeedback;
import esi.siw.e_health.tasks.SessionManagement;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsignesFragment extends Fragment implements View.OnClickListener {

    View view;
    Button questionnaireBtn, consginesBtn, sendFeedback;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    SessionManagement session;
    JSONArray jsonArray;
    int idPatient;


    public ConsignesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consignes, container, false);
        sendFeedback = view.findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(this);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        linearLayout = view.findViewById(R.id.linearLayout);

        scrollView.removeAllViews();
        scrollView.addView(linearLayout);

        relativeLayout.removeAllViews();
        relativeLayout.addView(scrollView);
        relativeLayout.addView(sendFeedback);

        session = new SessionManagement(getActivity());
        HashMap<String, String> userData = session.getUserDetails();
        idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));
        if (!Common.isFileExist("questionnaire.json")) {
            new GetConsignes(getActivity()).execute(idPatient);
        }

        getConsignes();


        return relativeLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendFeedback:
                sendFeedback();
                break;
        }
    }

    private void sendFeedback() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.send_feedback_popup);
        dialog.setTitle("Send feedback");
        final EditText feedBack = dialog.findViewById(R.id.feedback);
        Button btnSendFeedback = dialog.findViewById(R.id.btnSendFeedback);
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Chargement du questionnaire ..."); // Setting Message
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.setCancelable(false);
                progressDialog.show(); // Display Progress Dialog
                new SendFeedback(getContext(), progressDialog).execute(idPatient, feedBack.getText().toString());
                dialog.dismiss();
            }
        });


        dialog.show();



    }


    public void getConsignes() {
        try{
            jsonArray = new JSONArray(Common.readFromFile("consignes.json", getActivity()));
            Log.e("jsonArray", jsonArray.toString());

            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setStartOffset(200);
            alphaAnimation.setFillAfter(true);


            // Loop through "consignes"
            for (int i=0; i<jsonArray.length();i++) {

                View view = new View(getContext());
                LinearLayout.LayoutParams horizentalBar = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,1
                );
                view.setBackgroundColor(getResources().getColor(R.color.colorQuestionText));
                view.setLayoutParams(horizentalBar);
                view.setAnimation(alphaAnimation);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout consignesContainer = new LinearLayout(getContext());
                consignesContainer.setLayoutParams(params);
                consignesContainer.setOrientation(LinearLayout.VERTICAL);
                consignesContainer.setAnimation(alphaAnimation);


                LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                cardViewParams.setMargins(25,25,25,25);
                CardView cardView = new CardView(getContext());
                cardView.setLayoutParams(cardViewParams);
                cardView.setAnimation(alphaAnimation);

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TextView operation = new TextView(getContext());

                if (jsonObject.getString("Operation").equals("pre")) {
                    operation.setText("Pre-Operation");
                } else {
                    operation.setText("Post-Operation");
                }
                operation.setTextSize(25);
                operation.setTextColor(getResources().getColor(R.color.colorQuestionText));
                operation.setGravity(Gravity.CENTER);
                operation.setAnimation(alphaAnimation);

                TextView consignes = new TextView(getContext());
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
