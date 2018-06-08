package esi.siw.e_health;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import esi.siw.e_health.tasks.ChangePassword;
import esi.siw.e_health.tasks.SessionManagement;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {



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
                        new ChangePassword(context).execute(idPatient,oldPassword.getText().toString(),newPassword.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

        }
    }
}
