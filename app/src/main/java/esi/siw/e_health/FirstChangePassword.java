package esi.siw.e_health;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.common.Common;
import esi.siw.e_health.tasks.ChangeFirstPassword;

public class FirstChangePassword extends AppCompatActivity implements View.OnClickListener {

    Button changePassword;
    EditText password, confirmPassword;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_change_password);
        init();
    }

    public void init() {
        changePassword = findViewById(R.id.changePassword);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.passwordConfirm);

        changePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePassword:
                if (Common.isConnectedToInternet(this)) {
                    String newPass = password.getText().toString();
                    String confirmPass = confirmPassword.getText().toString();
                    if (newPass.equals(confirmPass)) {
                        if (newPass.length() < 8) {
                            int idPatient = getIntent().getIntExtra("idPatient", 0);
                            String Email = getIntent().getStringExtra("Email");
                            sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                            sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            sweetAlertDialog.setTitle("Changement du mot de passe ...");
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.show();
                            new ChangeFirstPassword(this, sweetAlertDialog).execute(idPatient, password.getText().toString(), Email);
                        } else {
                            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Le nouveau mot de passe doit contenir au moins 8 caractères !")
                                    .show();
                        }
                    } else {
                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Les mots de passe ne sont pas égaux !")
                                .show();
                    }
                }

                break;
        }

    }
}
