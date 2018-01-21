package idv.allen.gameball.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import idv.allen.gameball.MainActivity;
import idv.allen.gameball.R;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.util.Util;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private EditText etAccount,etPassword;
    private AsyncTask retrieveMemberTask;
    public MembershipVO membershipVO1;


    class RetrieveMemberTask extends AsyncTask<MembershipVO, Void, MembershipVO> {

        @Override
        protected MembershipVO doInBackground(MembershipVO... params) {
            MembershipVO membershipVO = params[0];
            String jsonIn;
            Gson gson = new Gson();
            String membershipVOJSON = gson.toJson(membershipVO);
            jsonIn = getRemoteData(Util.URL_MEM, membershipVOJSON);
            MembershipVO mem = gson.fromJson(jsonIn,MembershipVO.class);
            return mem;
        }

        @Override
        protected void onPostExecute(MembershipVO membershipVO) {

            if (membershipVO.getMem_acc().equals("memIdError") || membershipVO.getMem_psw().equals("memPwdError")) {
                membershipVO1 = membershipVO;

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
    }

    private void findViews() {
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (account.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(LoginActivity.this,R.string.msg_InvalidUserOrPassword,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isUserValid(account, password)) {
                    SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,MODE_PRIVATE);
                    preferences.edit()
                            .putBoolean("login",true)
                            .putString("account",account)
                            .putString("password",password);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,R.string.msg_InvalidUserOrPassword,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUserValid(String account, String password) {
        // 連線至server端檢查帳號密碼是否正確
        if (networkConnected()) {
            MembershipVO membershipVO = new MembershipVO();
            membershipVO.setMem_acc(account);
            membershipVO.setMem_psw(password);
            retrieveMemberTask = new RetrieveMemberTask().execute(membershipVO);
        } else {
            Toast.makeText(this,R.string.msg_NoNetwork,Toast.LENGTH_SHORT).show();
        }


        return account.equals("abc");
    }

//    private boolean getresult(MembershipVO m) {
//        if (membershipVO1.getMem_acc().equals("memIdError")) {
////            Toast.makeText(LoginActivity.this,"無此帳號",Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (membershipVO1.getMem_psw().equals("memPwdError")) {
////            Toast.makeText(LoginActivity.this,"密碼不對",Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            return true;
//        }
//    }

    // check if the device connect to the network
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private String getRemoteData(String url, String outStr) {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");

            OutputStream o = connection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(o);
            BufferedWriter bw = new BufferedWriter(ow);
            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }

    public void onCancelClick(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
