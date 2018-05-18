package esi.siw.e_health;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
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
                android.app.Fragment fragment1 = getFragmentManager().findFragmentById(R.id.fragmentConsignes);
                if (fragment1==null || !fragment1.isVisible()) {
                    fragmentClass = ConsignesFragment.class;
                }

                break;
            case R.id.questionnaireBtn:
                android.app.Fragment fragment2 = getFragmentManager().findFragmentById(R.id.relativeLayout);
                if (fragment2==null || !fragment2.isVisible()) {
                    fragmentClass = QuestionnaireFragment.class;
                }
                break;
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
