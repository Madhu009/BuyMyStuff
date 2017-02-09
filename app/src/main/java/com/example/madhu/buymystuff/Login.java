package com.example.madhu.buymystuff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madhu.buymystuff.Utilities.LoginReqestBean;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText et1,et2;
    private ProgressDialog pDlg = null;

    private static final String SERVICE_URL ="http://env-mes.elasticloud.star-vault.ro/rst/rest/Simple/Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Spinner spinner =(Spinner)findViewById(R.id.spinner);
        Button bt;
        et1=(EditText)findViewById(R.id.LUserName);
        et2=(EditText)findViewById(R.id.LPass);
        bt=(Button)findViewById(R.id.btLogin);
        bt.setOnClickListener(this);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String OutputMsg = "Selected User:"+"";

                Toast.makeText(
                        getApplicationContext(),OutputMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public  void onClick(View v) {
        switch (v.getId()) {

            case R.id.btLogin:
                String username=et1.getText().toString();
                String password=et2.getText().toString();

                if (TextUtils.isEmpty(username))
                {
                    et1.requestFocus();
                    et1.setError("Please Enter user name");
                }
                else
                if (TextUtils.isEmpty(password))
                {
                    et2.requestFocus();
                    et2.setError("Please Enter Password");
                }
                else {
                    LoginReqestBean loginReqestBean=new LoginReqestBean();
                    loginReqestBean.setUsername(username);
                    loginReqestBean.setPassword(password);
                    WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this, "Posting data...");
                    wst.addNameValuePair("MName", username);
                    wst.addNameValuePair("password", password);
                    wst.execute(new String[]{SERVICE_URL});
                   // startActivity(new Intent(this, Dashboard.class));
                }
                break;


        }


    }


    public void handleResponse(String response) {

        try {
            if (response.equals("no")) {

                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Services are not ready yet!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,Dashboard.class));
            }


        }

        catch(Exception e){
            // Log.e(TAG, e.getLocalizedMessage(), e);
        }


    }



    private void showProgressDialog() {

        pDlg = new ProgressDialog(this);
        pDlg.setMessage("Please wait..");
        pDlg.setProgressDrawable(this.getWallpaper());
        pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDlg.setCancelable(false);
        pDlg.show();

    }




    private class WebServiceTask extends AsyncTask<String,Integer, String> {

        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;

        private static final String TAG = "WebServiceTask";

        // connection timeout, in milliseconds (waiting to connect)
        private static final int CONN_TIMEOUT = 3000;

        // socket timeout, in milliseconds (waiting for data)
        private static final int SOCKET_TIMEOUT = 5000;

        private int taskType = POST_TASK;
        private Context mContext = null;
        private String processMessage = "Processing...";

        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        private ProgressDialog pDlg = null;

        public WebServiceTask(int taskType, Context mContext, String processMessage) {

            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }

        public void addNameValuePair(String name, String value) {

            params.add(new BasicNameValuePair(name, value));
        }

        private void showProgressDialog() {

            pDlg = new ProgressDialog(mContext);
            pDlg.setMessage(processMessage);
            pDlg.setProgressDrawable(mContext.getWallpaper());
            pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDlg.setCancelable(false);
            pDlg.show();

        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();

        }

        protected String doInBackground(String... urls) {

            String url = urls[0];
            String result = "";

            HttpResponse response = doResponse(url);

            if (response == null) {
                return result;
            } else {

                try {

                    result = inputStreamToString(response.getEntity().getContent());

                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }

            }

            return result;
        }

        @Override

        protected void onPostExecute(String response) {
            handleResponse(response);
            pDlg.dismiss();


        }

        // Establish connection and socket (data retrieval) timeouts
        private HttpParams getHttpParams() {

            HttpParams htpp = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

            return htpp;
        }

        private HttpResponse doResponse(String url) {

            // Use our connection and data timeouts as parameters for our
            // DefaultHttpClient
            HttpClient httpclient = new DefaultHttpClient(getHttpParams());

            HttpResponse response = null;

            try {
                switch (taskType) {

                    case POST_TASK:
                        HttpPost httppost = new HttpPost(url);
                        // Add parameters
                        httppost.setEntity(new UrlEncodedFormEntity(params));

                        response = httpclient.execute(httppost);
                        break;
                    case GET_TASK:
                        HttpGet httpget = new HttpGet(url);
                        response = httpclient.execute(httpget);
                        break;


                }
            } catch (Exception e) {


                Log.e(TAG, e.getLocalizedMessage(), e);

            }

            return response;
        }

        private String inputStreamToString(InputStream is) {

            String line = "";
            StringBuilder total = new StringBuilder();

            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                // Read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            // Return full string
            return total.toString();
        }

    }

}
