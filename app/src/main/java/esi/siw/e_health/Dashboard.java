package esi.siw.e_health;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

    public void init() {
        Button questionnaireBtn = findViewById(R.id.questionnaireBtn);
        Button consignesBtn = findViewById(R.id.consignesbtn);

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
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
