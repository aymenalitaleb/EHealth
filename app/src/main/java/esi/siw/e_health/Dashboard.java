package esi.siw.e_health;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import esi.siw.e_health.tasks.CheckAccount;
import esi.siw.e_health.tasks.ListenNotification;
import esi.siw.e_health.tasks.SessionManagement;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    SweetAlertDialog sweetAlertDialog;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitle("Vérification du compte ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        session = new SessionManagement(this);
        HashMap<String, String> userData = session.getUserDetails();
        int idPatient = Integer.parseInt(userData.get(SessionManagement.KEY_ID));


        new CheckAccount(this, sweetAlertDialog, idPatient).execute(idPatient);


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

        // Starting service for checking notififation
        intent = new Intent(Dashboard.this, ListenNotification.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

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
//            Log.e("class naaaaame: ", fragment.getClass().getName());
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment,fragment.getClass().getName()).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
//                Log.e ("isMyServiceRunning?", true+"");
                return true;
            }
        }
//        Log.e ("isMyServiceRunning?", false+"");
        return false;
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
