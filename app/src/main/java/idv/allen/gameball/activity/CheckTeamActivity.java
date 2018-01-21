package idv.allen.gameball.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.fragment.Page;
import idv.allen.gameball.fragment.TeamAFragment;
import idv.allen.gameball.fragment.TeamBFragment;
import idv.allen.gameball.gameday.GamedayDAO;
import idv.allen.gameball.gameday.GamedayDAO_interface;
import idv.allen.gameball.plate_appearance.GameVO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO_interface;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

import static idv.allen.gameball.service.GameService.notiStartGameWS2;
import static idv.allen.gameball.util.Util.gamedayLiveWS;

public class CheckTeamActivity extends AppCompatActivity {
    private final static String TAG = "CheckTeamActivity";
    String teamAName,teamBName,gameday_id,team_a_id,team_b_id,tourn_id;
    Page page1,page2;
    Dialog myDialog;
    private TextView tvWait,tvStart,tvBatFirst;

    class SendBatFirstTask extends AsyncTask<String,Void,Void> {

        @Override
        protected Void doInBackground(String... params) {
            GamedayDAO_interface dao = new GamedayDAO();
            dao.updateBatOrderFirst(params[0],params[1]);
            return null;
        }
    }

    class GetGameVOTask extends AsyncTask<String,Void,GameVO> {

        @Override
        protected GameVO doInBackground(String... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.startGame(params[0],params[1]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_team);
        Bundle bundle = getIntent().getExtras();
        String Gameday_name = bundle.getString("Gameday_name");
        setTitle(Gameday_name);
        //get data from gamedaymanageactivity
        team_a_id = bundle.getString("Team_a_id");
        team_b_id = bundle.getString("Team_b_id");
        gameday_id = bundle.getString("Gameday_id");
        tourn_id = bundle.getString("Tourn_id");
        try {
            TeamVO teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(team_a_id).get();
            teamAName = teamVO.getTeam_name();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        try {
            TeamVO teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(team_b_id).get();
            teamBName = teamVO.getTeam_name();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        bundle.putString("teamAName",teamAName);
        bundle.putString("teamBName",teamBName);
        //send data to the teamAfragment
        TeamAFragment teamAFragment = new TeamAFragment();
        teamAFragment.setData(bundle);
        //send data to the teamBfragment
        TeamBFragment teamBFragment = new TeamBFragment();
        teamBFragment.setData(bundle);
        page1 = new Page(teamAFragment,teamAName);
        page2 = new Page(teamBFragment,teamBName);
        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create viewpager
        ViewPager vpCheckTeam =(ViewPager) findViewById(R.id.vpCheckTeam);
        vpCheckTeam.setAdapter(new CheckTeamAdapter(getSupportFragmentManager()));
        //create tablayout
        TabLayout tabCheckTeam = (TabLayout) findViewById(R.id.tabCheckTeam);
        tabCheckTeam.setupWithViewPager(vpCheckTeam);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.check_team,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_start_game:
                Toast.makeText(CheckTeamActivity.this,"start game",Toast.LENGTH_SHORT).show();
                createDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createDialog(){
        myDialog = new Dialog(CheckTeamActivity.this);
        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.dialog_start_game);
        Window dialogWindow = myDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 1000;
        lp.alpha = 1.0f;
        dialogWindow.setAttributes(lp);
        findViewsOnDialog();
        //decide bat order
        int num = (int)(Math.random()*10) + 1;
        final String teamBefore;
        String teamAfter;
        if (num > 5) {
            teamBefore = teamAName;
            teamAfter = teamBName;
        } else {
            teamBefore = teamBName;
            teamAfter = teamAName;
        }
        tvBatFirst.setText(teamBefore+"：先攻"+"\n"+teamAfter+"：後攻");
        tvWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //送出先攻球隊
                SendBatFirstTask task = new SendBatFirstTask();
                if (teamBefore.equals(teamAName)) {
                    task.execute(gameday_id,team_a_id);
                } else {
                    task.execute(gameday_id,team_b_id);
                }
                //取得gameVO並帶往DataRecordActivity
                GameVO gameVO = new GameVO();
                GetGameVOTask task1 = new GetGameVOTask();
                if (networkConnected()) {
                    try {
                        gameVO = task1.execute(tourn_id,gameday_id).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    return;
                }
                Gson gson = new Gson();
                //建立賽事連線
                Util.connectServer(CheckTeamActivity.this, gameVO.getGamedayId());
                //發送開始比賽的推播
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "notiGameStart");
                jsonObject.addProperty("gamedayId", gameVO.getGamedayId());
                notiStartGameWS2.send(jsonObject.toString());

                Bundle bundle = new Bundle();
                String gameJSON = gson.toJson(gameVO);
                bundle.putString("gameJSON",gameJSON);
                bundle.putString("tourn_id",tourn_id);
                bundle.putString("gameday_id",gameVO.getGamedayId());
                Intent i = new Intent(CheckTeamActivity.this,DataRecordActivity.class);
                i.putExtras(bundle);
                startActivity(i);

                //發送開始比賽推播
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("action", "start");
                jsonObject2.addProperty("gameday_id", gameVO.getGamedayId());
                gamedayLiveWS.send(jsonObject2.toString());

                CheckTeamActivity.this.finish();
            }
        });
        myDialog.show();
    }

    private void findViewsOnDialog(){
        tvWait = (TextView) myDialog.findViewById(R.id.tvWait);
        tvStart = (TextView) myDialog.findViewById(R.id.tvStart);
        tvBatFirst = (TextView) myDialog.findViewById(R.id.tvBatFirst);
    }

    private class CheckTeamAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public CheckTeamAdapter (FragmentManager fragmentManager) {
            super(fragmentManager);
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

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}

