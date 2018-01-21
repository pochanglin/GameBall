package idv.allen.gameball.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.MainActivity;
import idv.allen.gameball.R;
import idv.allen.gameball.membership.MembershipDAO;
import idv.allen.gameball.membership.MembershipDAO_interface;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.util.Util;

public class Login2Activity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";
    private EditText etAccount,etPassword;
    private AsyncTask retrieveMemberTask;
    private String jsonIn;


//    class RetrieveMemberTask extends AsyncTask<MembershipVO, Void, MembershipVO> {
//
//        @Override
//        protected MembershipVO doInBackground(MembershipVO... params) {
//            MembershipVO membershipVO = params[0];
//            Gson gson = new Gson();
//            String membershipVOJSON = gson.toJson(membershipVO);
//            jsonIn = getRemoteData(Util.URL_MEM, membershipVOJSON);
//
//            MembershipVO mem1 = new MembershipVO();
//            try {
//                Log.d(TAG, "jsonIn: " + jsonIn);
//                JSONObject jsonObj = new JSONObject(jsonIn);
//                mem1.setMem_acc(jsonObj.getString("mem_acc"));
//                mem1.setMem_psw(jsonObj.getString("mem_psw"));
//                mem1.setMem_name(jsonObj.getString("mem_name"));
//                mem1.setMem_id(jsonObj.getString("mem_id"));
//
//                JSONArray jArray = new JSONArray(jsonObj.getString("mem_pic"));
//                byte[] b = new byte[jArray.length()];
//                for (int i = 0; i < jArray.length(); i++) {
//                    b[i] = (byte) jArray.getInt(i);
//                }
//                mem1.setMem_pic(b);
//            } catch (Exception e) {
//                return mem1;
//            }
//            return mem1;
//        }
//
//        @Override
//        protected void onPostExecute(MembershipVO membershipVO) {
//
//            if (isUserValid(membershipVO)) {
//                byte[] pic = membershipVO.getMem_pic();
//                String memberPic = Base64.encodeToString(pic,0,pic.length,2);
//                SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,MODE_PRIVATE);
//                preferences.edit()
//                        .putBoolean("login",true)
//                        .putString("account",membershipVO.getMem_acc())
//                        .putString("password",membershipVO.getMem_psw())
//                        .putString("MemberName",membershipVO.getMem_name())
//                        .putString("memberPic",memberPic)
//                        .putString("mem_id",membershipVO.getMem_id())
//                        .putString("meberData",jsonIn)
//                        .apply();
//
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("MemberName",membershipVO.getMem_name());
//                bundle.putByteArray("MemberPicture",membershipVO.getMem_pic());
//
//                intent.putExtras(bundle);
//                setResult(RESULT_OK,intent);
//                finish();
//            } else {
//                //Toast.makeText(Login2Activity.this,R.string.msg_InvalidUserOrPassword,Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setResult(RESULT_CANCELED);
        findViews();
    }
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        //如果login是true,將從偏好設定黨取出帳密進行比對
        if (login) {
            String userId = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            if (isMember(userId, password)) {
                setResult(RESULT_OK);
                finish();
            } else {
                //送進資料庫比對，若結果為false，
                return;
            }
        } else {
            //如果login是false,表示尚未登入
            return;
        }
    }
    private boolean isMember(final String account, final String password) {
        boolean isMember;
        try {
            if (networkConnected()) {
                isMember = new AsyncTask<String, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        MembershipDAO_interface dao = new MembershipDAO();
                        return dao.isMember(account, password);
                    }
                }.execute().get();
            } else {
                isMember = false;
                Toast.makeText(Login2Activity.this,"",Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            isMember = false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            isMember = false;
        }
        return isMember;
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
                    Toast.makeText(Login2Activity.this,R.string.msg_InvalidUserOrPassword,Toast.LENGTH_SHORT).show();
                    return;
                }

                if (networkConnected()) {
                    MembershipVO membershipVO = null;
                    try {
                        membershipVO = new AsyncTask<String,Void,MembershipVO>() {

                            @Override
                            protected MembershipVO doInBackground(String... params) {
                                MembershipDAO_interface dao = new MembershipDAO();
                                return dao.getMemberByAcc(params[0],params[1]);
                            }
                        }.execute(account,password).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (membershipVO == null) {
                        Toast.makeText(Login2Activity.this,"為甚麼會空直....",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (isUserValid(membershipVO)) {
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                        String meberData = gson.toJson(membershipVO);

                        byte[] pic = membershipVO.getMem_pic();
                        String memberPic = Base64.encodeToString(pic,0,pic.length,2);
                        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,MODE_PRIVATE);
                        preferences.edit()
                                .putBoolean("login",true)
                                .putString("account",membershipVO.getMem_acc())
                                .putString("password",membershipVO.getMem_psw())
                                .putString("MemberName",membershipVO.getMem_name())
                                .putString("memberPic",memberPic)
                                .putString("mem_id",membershipVO.getMem_id())
                                .putString("meberData",meberData)
                                .apply();

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("MemberName",membershipVO.getMem_name());
                        bundle.putByteArray("MemberPicture",membershipVO.getMem_pic());
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        //Toast.makeText(Login2Activity.this,R.string.msg_InvalidUserOrPassword,Toast.LENGTH_SHORT).show();
                    }



//                    MembershipVO membershipVO = new MembershipVO();
//                    membershipVO.setMem_acc(account);
//                    membershipVO.setMem_psw(password);
//                    retrieveMemberTask = new RetrieveMemberTask().execute(membershipVO);
                }
            }
        });
    }
    //
    private boolean isUserValid(MembershipVO membershipVO) {
        if (membershipVO.getMem_acc().equals("memIdError")) {
            Toast.makeText(Login2Activity.this,"無此帳號",Toast.LENGTH_SHORT).show();
            return false;
        } else if (membershipVO.getMem_psw().equals("memPwdError")) {
            Toast.makeText(Login2Activity.this,"密碼不對",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

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
