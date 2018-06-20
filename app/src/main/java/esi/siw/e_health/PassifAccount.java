package esi.siw.e_health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import esi.siw.e_health.tasks.SessionManagement;

public class PassifAccount extends AppCompatActivity {

    Button btnlogout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passif_account);

        btnlogout = findViewById(R.id.btnLogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(getBaseContext());
                Intent intent = new Intent(getBaseContext(), Dashboard.class);
                intent.putExtra("finish","finish");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                sessionManagement.logoutUser();
                finish();
            }
        });
    }
}
