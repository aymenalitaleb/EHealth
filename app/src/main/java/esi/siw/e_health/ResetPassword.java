package esi.siw.e_health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import esi.siw.e_health.tasks.ResetPasswordTask;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    Button btnResetPassword ;
    ImageView logo;
    TextView txtView;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Reset password");
        }

        initViews();

    }

    public void initViews () {
        btnResetPassword = findViewById(R.id.btnResetPassword);
        logo = findViewById(R.id.imgView_logo);
        email = findViewById(R.id.email);
        txtView = findViewById(R.id.txtView);

        btnResetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResetPassword:
                new ResetPasswordTask(this, this).execute(email.getText().toString());
                break;
        }
    }


}
