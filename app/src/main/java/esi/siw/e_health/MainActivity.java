package esi.siw.e_health;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import esi.siw.e_health.Tasks.LoginTask;
import esi.siw.e_health.Tasks.SessionManagement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText email, password;
    public Button login;

    SessionManagement session;

    RelativeLayout rellay1, rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rellay1 =  findViewById(R.id.rellay1);
        rellay2 =  findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 1200);
        session = new SessionManagement(this);
        session.checkLogin();

        init();


    }


    public void init() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
        }
    }

    private void login() {
        Toast.makeText(this, email.getText().toString() + "-" + password.getText().toString(), Toast.LENGTH_LONG).show();
        new LoginTask(this).execute(email.getText().toString(), password.getText().toString());
    }
}
