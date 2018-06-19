package esi.siw.e_health;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import esi.siw.e_health.tasks.LoginTask;
import esi.siw.e_health.tasks.SessionManagement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText email, password;
    public Button login, forgotPassword;
    public ImageView logo;

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


    // For the animation


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

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
        forgotPassword = findViewById(R.id.forgotPassword);
        logo = findViewById(R.id.imgView_logo);

        forgotPassword.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.forgotPassword:
                animateResetPasswordActivity();
                break;
        }
    }

    private void animateResetPasswordActivity() {
        Intent intent = new Intent(MainActivity.this, ResetPassword.class);

        Pair[] pairs = new Pair[2];

        pairs[0] = new Pair<View, String>(logo, "logoTransition");
        pairs[1] = new Pair<View, String>(email, "emailTransition");

        ActivityOptions options = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (options != null) {
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }
        }

    }

    private void login() {
//        Toast.makeText(this, email.getText().toString() + "-" + password.getText().toString(), Toast.LENGTH_LONG).show();
        new LoginTask(this).execute(email.getText().toString(), password.getText().toString());

    }
}
