package esi.siw.e_health.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class ChangePassword extends AsyncTask {

    private Context context;

    public ChangePassword(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String link = "http://malitaleb.000webhostapp.com/changePassword.php";

        String idPatient = String.valueOf(objects[0]);
        String oldPassword = (String) objects[1];
        String newPassword = (String) objects[2];

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("idPatient", idPatient));
        nameValuePairs.add(new BasicNameValuePair("oldPassword", oldPassword));
        nameValuePairs.add(new BasicNameValuePair("newPassword", newPassword));

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

        return "Success";
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        String result = (String) o;
        if (result.equals("Error")) {
            Toast.makeText(context, "Old password is incorrect", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Password changed !", Toast.LENGTH_SHORT).show();
        }

    }

}
