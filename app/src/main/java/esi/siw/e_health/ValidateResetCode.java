package esi.siw.e_health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ValidateResetCode extends AppCompatActivity implements View.OnClickListener {

    Button validateCode ;
    EditText validationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_reset_code);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Reset password");
        }
        initViews();
    }

    public void initViews() {
        validateCode = findViewById(R.id.validateCode);
        validationCode = findViewById(R.id.validationCode);
        validateCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.validateCode:
                if (getIntent() != null) {
                    new esi.siw.e_health.tasks.ValidateResetCode(this).execute(getIntent().getStringExtra("email"), validationCode.getText().toString());
                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Une érreure est survenue !")
                            .show();
                }

                break;
        }
    }

}
