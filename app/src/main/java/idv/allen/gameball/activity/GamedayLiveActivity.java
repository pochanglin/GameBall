package idv.allen.gameball.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import idv.allen.gameball.R;
import idv.allen.gameball.fragment.LiveTeamAFragment;
import idv.allen.gameball.fragment.LiveTeamBFragment;
import idv.allen.gameball.fragment.Page;
import idv.allen.gameball.gameday.GamedayVO;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

public class GamedayLiveActivity extends AppCompatActivity {
    private final static String TAG = "GamedayLiveActivity";
    private LocalBroadcastManager broadcastManager;
    Page page1,page2;
    TeamVO teamA,teamB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameday_live);
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerGamedayLiveReceiver();
        //取得gameday_id
        Bundle bundle = getIntent().getExtras();
        String gameday_id = bundle.getString("gameday_id");
        //建立ws連線
        Util.connectServer(this, gameday_id);
        //
        final GamedayVO gamedayVO = new Gson().fromJson(bundle.getString("gamedayVOJSON"),GamedayVO.class);

        try {
            teamA = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(gamedayVO.getTeam_a_id());
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            teamB = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(gamedayVO.getTeam_b_id());
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        LiveTeamAFragment liveTeamAFragment = new LiveTeamAFragment();
        liveTeamAFragment.setData(bundle);
        LiveTeamBFragment liveTeamBFragment = new LiveTeamBFragment();
        liveTeamBFragment.setData(bundle);

        page1 = new Page(liveTeamAFragment,teamA.getTeam_name());
        page2 = new Page(liveTeamBFragment,teamB.getTeam_name());
        //create viewpager
        ViewPager vpDataRecord =(ViewPager) findViewById(R.id.vpGamedayLive);
        vpDataRecord.setAdapter(new GamedayLiveAdapter(getSupportFragmentManager()));
        //create tablayout
        TabLayout tabDataRecord = (TabLayout) findViewById(R.id.tabGamedayLive);
        tabDataRecord.setupWithViewPager(vpDataRecord);

        ImageView ivTeamALogo = (ImageView) findViewById(R.id.ivTeamALogo);
        ivTeamALogo.setImageBitmap(BitmapFactory.decodeByteArray(teamA.getTeam_logo(),0,teamA.getTeam_logo().length));
        ImageView ivTeamBLogo = (ImageView) findViewById(R.id.ivTeamBLogo);
        ivTeamBLogo.setImageBitmap(BitmapFactory.decodeByteArray(teamB.getTeam_logo(),0,teamB.getTeam_logo().length));
        TextView tvTeamAName = (TextView) findViewById(R.id.tvTeamAName);
        tvTeamAName.setText(teamA.getTeam_name());
        TextView tvTeamBName = (TextView) findViewById(R.id.tvTeamBName);
        tvTeamBName.setText(teamB.getTeam_name());
        TextView tvGamedayName = (TextView) findViewById(R.id.tvGamedayName);
        tvGamedayName.setText(gamedayVO.getGameday_name());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.disconnectServer();
    }

    private void registerGamedayLiveReceiver() {
        Log.d(TAG,"註冊廣播囉~~");
        IntentFilter intentFilter = new IntentFilter(Util.WS_GAME_START);
        GamedayLiveReceiver gamedayLiveReceiver = new GamedayLiveReceiver();
        broadcastManager.registerReceiver(gamedayLiveReceiver,intentFilter);
    }

    private class GamedayLiveReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pa_appearance = intent.getStringExtra("pa_appearance");
            Log.d(TAG, pa_appearance);
            JsonObject jsonObject = new Gson().fromJson(pa_appearance, JsonObject.class);
            String type = jsonObject.get("type").getAsString();

            if ("start".equals(type)) {
                SweetAlertDialog pDialog = new SweetAlertDialog(GamedayLiveActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                pDialog.setTitleText("比賽開始!");
                pDialog.setCustomImage(R.drawable.logo7);
                pDialog.show();
            }
            if ("end".equals(type)) {
                SweetAlertDialog pDialog = new SweetAlertDialog(GamedayLiveActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                pDialog.setTitleText("比賽結束!");
                pDialog.setCustomImage(R.drawable.logo7);
                pDialog.show();
            }


        }
    }

    private class GamedayLiveAdapter extends FragmentPagerAdapter {
        List<Page> pageList;
        public GamedayLiveAdapter(FragmentManager fm) {
            super(fm);
            pageList = new ArrayList<>();
            pageList.add(page1);
            pageList.add(page2);
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }
}
