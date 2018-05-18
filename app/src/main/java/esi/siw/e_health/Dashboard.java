package esi.siw.e_health;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getIntent().hasExtra("finish")) {
            finish();
        }

        init();

        // Manupiling the fragment
        Fragment fragment = null;
        Class fragmentClass = QuestionnaireFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void init() {
        Button questionnaireBtn = findViewById(R.id.questionnaireBtn);
        Button consignesBtn = findViewById(R.id.consignesbtn);
        ImageView profileBtn = findViewById(R.id.profileBtn);

        profileBtn.setOnClickListener(this);
        questionnaireBtn.setOnClickListener(this);
        consignesBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Fragment fragment;
        Class fragmentClass = null;

        switch (v.getId()) {
            case R.id.consignesbtn:
                fragmentClass = ConsignesFragment.class;

                break;
            case R.id.questionnaireBtn:
                fragmentClass = QuestionnaireFragment.class;
                break;
            case R.id.profileBtn:
                startActivity(new Intent(this,ProfileActivity.class));
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Log.e("class naaaaame: ", fragment.getClass().getName());
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment,fragment.getClass().getName()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
