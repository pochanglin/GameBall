package idv.allen.gameball;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import idv.allen.gameball.activity.Login2Activity;
import idv.allen.gameball.activity.ManageActivity;
import idv.allen.gameball.activity.MemberActivity;
import idv.allen.gameball.activity.TeamsActivity;
import idv.allen.gameball.activity.TournamentActivity;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.service.GameService;
import idv.allen.gameball.util.Util;

public class MainActivity extends AppCompatActivity {
    private final int LOGIN_REQUEST = 0;
    private TextView tvName,tvLogin,tvLogout,tvRegister;
    private ImageView ivMemPic;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, GameService.class);
        startService(intent);
        setContentView(R.layout.activity_main);
        tvName = (TextView) findViewById(R.id.tvName);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        ivMemPic = (ImageView) findViewById(R.id.ivMemPic);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 從偏好設定檔中取得登入狀態來決定是否顯示「登出」
        pref = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            tvName.setText("歡迎," + pref.getString("MemberName",""));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            MembershipVO membershipVO = gson.fromJson(pref.getString("meberData",""),MembershipVO.class);
            byte[] pic = membershipVO.getMem_pic();
//            byte[] pic = Base64.decode(pref.getString("memberPic",""),2);
            ivMemPic.setImageBitmap(BitmapFactory.decodeByteArray(pic,0,pic.length));
            tvLogin.setVisibility(View.GONE);
            tvLogout.setVisibility(View.VISIBLE);
            tvRegister.setVisibility(View.GONE);
        } else {
            tvLogin.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.GONE);
        }
    }

    public void onLoginTextClick(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Login2Activity.class);
        //將自行定義的請求代碼一起送出，才能確認資料來源與出處是否為同一個
        startActivityForResult(intent, LOGIN_REQUEST);
    }
    public void onLogoutTextClick(View view) {
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        pref.edit().putBoolean("login", false)
                .remove("account")
                .remove("password")
                .remove("MemberName")
                .remove("memberPic")
                .remove("meberData")
                .remove("mem_id").apply();
        tvLogin.setVisibility(View.VISIBLE);
        tvLogout.setVisibility(View.GONE);
        tvName.setText("歡迎");
        ivMemPic.setImageResource(R.drawable.visitor);
        tvRegister.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //判斷請求代碼是否相同，確認來源是否正確
        if (requestCode != LOGIN_REQUEST) {
            return;
        }

        switch (resultCode) {
            case RESULT_OK:
//                Bundle bundle = data.getExtras();
//                String memberName = bundle.getString("MemberName");
//                byte[] b = bundle.getByteArray("MemberPicture");
//                tvName.setText("歡迎 , " + memberName);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(b,0,b.length);
//                ivMemPic.setImageBitmap(bitmap);
                break;
            case RESULT_CANCELED:

                break;
        }
    }

    public void onRegisterTextClick(View v) {
        Toast.makeText(this,"go to register",Toast.LENGTH_SHORT).show();
    }
    public void onManageCardClick(View view) {
        Intent i = new Intent(MainActivity.this, ManageActivity.class);
        startActivity(i);
    }
    public void onMemberCardClick(View view) {
        Intent i = new Intent(MainActivity.this, MemberActivity.class);
        startActivity(i);
    }
    public void onTournCardClick(View view) {
        Intent i = new Intent(MainActivity.this, TournamentActivity.class);
        startActivity(i);
    }
    public void onTeamsCardClick(View view) {
        Intent i = new Intent(MainActivity.this, TeamsActivity.class);
        startActivity(i);
    }
}
