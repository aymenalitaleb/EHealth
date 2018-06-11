package esi.siw.e_health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    CircularProgressButton btnResetPassword ;

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

        btnResetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResetPassword:
                btnResetPassword.startAnimation();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnResetPassword.dispose();
    }
}
