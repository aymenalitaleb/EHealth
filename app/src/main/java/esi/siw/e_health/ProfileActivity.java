package esi.siw.e_health;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.common.Common;
import esi.siw.e_health.tasks.ChangePassword;
import esi.siw.e_health.tasks.SessionManagement;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        init();

    }

    private void init() {
        Button logout = findViewById(R.id.logout);
        Button changePassword = findViewById(R.id.changePassword);
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitle("Changement de mot de passe ...");
        sweetAlertDialog.setCancelable(false);

        logout.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final Context context = this;
        final SessionManagement sessionManagement = new SessionManagement(this);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_password_popup);
        dialog.setTitle("Change password");
        Button changePassword = dialog.findViewById(R.id.changePassword);
        final TextView oldPassword= dialog.findViewById(R.id.oldPassword);
        final TextView newPassword= dialog.findViewById(R.id.newPassword);
        final TextView confirmPassword = dialog.findViewById(R.id.confirmPassword);
        switch (v.getId()) {
            case R.id.logout:
                Intent intent = new Intent(this, Dashboard.class);
                intent.putExtra("finish","finish");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sessionManagement.logoutUser();
                break;
            case R.id.changePassword:
                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> userData = sessionManagement.getUserDetails();
                        int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));
                        if (Common.isConnectedToInternet(context)) {
                            String oldPass = oldPassword.getText().toString();
                            String newPass = newPassword.getText().toString();
                            String confirmPass = confirmPassword.getText().toString();
                            if (oldPass.equals("") || newPass.equals("") || confirmPass.equals("")) {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Vous devez remplir tous les champs !")
                                        .show();
                            } else {
                                if (confirmPass.equals(newPass)) {
                                    if (newPass.length() < 8) {
                                        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Oops...")
                                                .setContentText("Le nouveau mot de passe doit contenir au moins 8 caractères !")
                                                .show();
                                    } else {
                                        sweetAlertDialog.show();
                                        new ChangePassword(context, sweetAlertDialog).execute(idPatient,oldPassword.getText().toString(),newPassword.getText().toString());
                                        dialog.dismiss();
                                    }
                                } else {
                                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Les mots de passe ne sont pas égaux !")
                                            .show();
                                }
                            }


                        } else {
                            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Vérifiez que vous êtes connectés !")
                                    .show();
                        }
                    }
                });
                dialog.show();


                break;

        }
    }
}
