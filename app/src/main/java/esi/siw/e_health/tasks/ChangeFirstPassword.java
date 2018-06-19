package esi.siw.e_health.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChangeFirstPassword extends AsyncTask {


    ProgressDialog progressDialog;
    private Context context;
    private SweetAlertDialog sweetAlertDialog;

    public ChangeFirstPassword(Context context, SweetAlertDialog sweetAlertDialog) {
        this.context = context;
        this.sweetAlertDialog = sweetAlertDialog;
    }


    @Override
    protected Object doInBackground(Object[] objects) {

        String link = "http://malitaleb.000webhostapp.com/changeFirstPassword.php";

        String idPatient = String.valueOf(objects[0]);
        String Password = (String) objects[1];
        String Email = (String) objects[2];

        List<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("idPatient", idPatient));
        nameValuePairs.add(new BasicNameValuePair("Password", Password));

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(link);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {
            Log.e("errrrrrrrorr",e.getMessage());
            return "Error";
        } catch (IOException e) {
            Log.e("errrrrrrrorr",e.getMessage());
            return "Error";
        }

        ArrayList<String> auth = new ArrayList<>();
        auth.add(Email);
        auth.add(Password);
        return auth;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ArrayList<String> auth = (ArrayList<String>) o;
        Log.e("email",auth.get(0));
        Log.e("password",auth.get(1));
        sweetAlertDialog.dismiss();
        new LoginTask(context).execute(auth.get(0), auth.get(1));

    }
}
