package esi.siw.e_health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import esi.siw.e_health.tasks.ChangeFirstPassword;

public class FirstChangePassword extends AppCompatActivity implements View.OnClickListener {

    Button changePassword;
    EditText password, confirmPassword;

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
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    int idPatient = getIntent().getIntExtra("idPatient", 0);
                    String Email = getIntent().getStringExtra("Email");
                    new ChangeFirstPassword(this).execute(idPatient, password.getText().toString(), Email);
                }
                break;
        }

    }
}
