package esi.siw.e_health;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.common.Common;
import esi.siw.e_health.tasks.GetConsignes;
import esi.siw.e_health.tasks.SendFeedback;
import esi.siw.e_health.tasks.SessionManagement;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsignesFragment extends Fragment implements View.OnClickListener {

    View view;
    Button sendFeedback;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout, connectionProblem, noConsgine;
    SessionManagement session;
    SweetAlertDialog sweetAlertDialog;
    JSONArray jsonArray;
    Button btnRefresh, btnRefresh2;
    ScrollView scrollView;
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
        scrollView = view.findViewById(R.id.scrollView);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        linearLayout = view.findViewById(R.id.linearLayout);
        connectionProblem = view.findViewById(R.id.connectionProblem);
        noConsgine = view.findViewById(R.id.noConsigne);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh2 = view.findViewById(R.id.btnRefresh2);
        btnRefresh2.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);


        session = new SessionManagement(getActivity());
        HashMap<String, String> userData = session.getUserDetails();
        idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));

        if (Common.isConnectedToInternet(getContext())) {
            scrollView.removeAllViews();
            scrollView.addView(linearLayout);

            relativeLayout.removeView(scrollView);
            relativeLayout.removeView(sendFeedback);

            relativeLayout.addView(scrollView);
            relativeLayout.addView(sendFeedback);

            sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            sweetAlertDialog.setTitle("Chargement des consignes ...");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();
            new GetConsignes(getContext(), sweetAlertDialog, getActivity(), linearLayout, noConsgine, connectionProblem).execute(idPatient);
        } else {
            if (Common.getJsonFile(getContext(), "consignes").equals("")) {
                Toast.makeText(getContext(), "No connection !", Toast.LENGTH_SHORT).show();
                scrollView.setVisibility(View.GONE);
                sendFeedback.setVisibility(View.GONE);
                connectionProblem.setVisibility(View.VISIBLE);
                noConsgine.setVisibility(View.GONE);
            } else {
                getConsignes();
            }
        }
        return relativeLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendFeedback:
                sendFeedback();
                break;
            case R.id.btnRefresh:
                if (Common.isConnectedToInternet(getContext())) {
                    scrollView.setVisibility(View.VISIBLE);
                    sendFeedback.setVisibility(View.VISIBLE);

                    scrollView.removeAllViews();
                    scrollView.addView(linearLayout);

                    relativeLayout.removeView(scrollView);
                    relativeLayout.removeView(sendFeedback);
                    relativeLayout.addView(scrollView);
                    relativeLayout.addView(sendFeedback);

                    session = new SessionManagement(getActivity());
                    HashMap<String, String> userData = session.getUserDetails();
                    int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));

                    sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    sweetAlertDialog.setTitle("Chargement des consignes ...");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();
                    new GetConsignes(getContext(), sweetAlertDialog, getActivity(), linearLayout, noConsgine, connectionProblem).execute(idPatient);
                } else {
                    if (Common.getJsonFile(getContext(), "consignes").equals("")) {
                        scrollView.setVisibility(View.GONE);
                        sendFeedback.setVisibility(View.GONE);
                        connectionProblem.setVisibility(View.VISIBLE);
                        noConsgine.setVisibility(View.GONE);


                    } else {
                        getConsignes();
                    }
                }
                break;
            case R.id.btnRefresh2:
                if (Common.isConnectedToInternet(getContext())) {
                    scrollView.setVisibility(View.VISIBLE);
                    sendFeedback.setVisibility(View.VISIBLE);

                    scrollView.removeAllViews();
                    scrollView.addView(linearLayout);

                    relativeLayout.removeView(scrollView);
                    relativeLayout.removeView(sendFeedback);
                    relativeLayout.addView(scrollView);
                    relativeLayout.addView(sendFeedback);

                    session = new SessionManagement(getActivity());
                    HashMap<String, String> userData = session.getUserDetails();
                    int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));

                    sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    sweetAlertDialog.setTitle("Chargement des consignes ...");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.show();
                    new GetConsignes(getContext(), sweetAlertDialog, getActivity(), linearLayout, noConsgine, connectionProblem).execute(idPatient);
                } else {
                    if (Common.getJsonFile(getContext(), "consignes").equals("")) {
                        scrollView.setVisibility(View.GONE);
                        sendFeedback.setVisibility(View.GONE);
                        connectionProblem.setVisibility(View.VISIBLE);
                        noConsgine.setVisibility(View.GONE);

                    } else {
                        getConsignes();
                    }
                }
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
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Confirmer ?")
                        .setContentText("Envoi du feedback.")
                        .setConfirmText("Oui, envoyer-le")
                        .setCancelText("Non")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                String feedbackTxt = feedBack.getText().toString();
                                if (feedbackTxt.equals("")) {
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Le feedback ne doit pas être vide !")
                                            .show();
                                } else {
                                    SweetAlertDialog sweetAlertDialog2 = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                    sweetAlertDialog2.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    sweetAlertDialog2.setTitle("Envoi du feedback ...");
                                    sweetAlertDialog2.setCancelable(false);
                                    sweetAlertDialog2.show();
                                    new SendFeedback(getContext(), sweetAlertDialog2).execute(idPatient, feedbackTxt);
                                    dialog.dismiss();
                                }

                            }
                        })
                        .show();
            }
        });
        dialog.show();
    }

    public void getConsignes() {
        try{
            jsonArray = new JSONArray(Common.getJsonFile(getContext(), "consignes"));
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
