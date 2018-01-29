package idv.allen.gameball.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import idv.allen.gameball.R;
import idv.allen.gameball.gameday.GamedayDAO;
import idv.allen.gameball.gameday.GamedayDAO_interface;
import idv.allen.gameball.gameday.GamedayVO;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

public class GamedayManageActivity extends AppCompatActivity {
    private final static String TAG = "GamedayManageActivity";
    private AsyncTask retrieveGamedayTask;
    private RecyclerView rvGameDay;
    GamedayAdapter adapter;
    private String tourn_id;
    Bundle bundle;
    private ChangeDataReceiver changeDataReceiver;
    private LocalBroadcastManager localBroadcastManager;

    class RetrieveGamedayTask extends AsyncTask<String,Void,List<GamedayVO>> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GamedayManageActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<GamedayVO> doInBackground(String... params) {
            String tourn_id = params[0];
            GamedayDAO_interface dao = new GamedayDAO();
            return dao.tournGetAll(tourn_id);
        }

        @Override
        protected void onPostExecute(List<GamedayVO> gamedayVOs) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (gamedayVOs == null || gamedayVOs.isEmpty()) {

            } else {
                showTournRecyclerView (gamedayVOs);
            }
        }
    }

    class GetTeamTask extends AsyncTask<String,Void,TeamVO> {

        @Override
        protected TeamVO doInBackground(String... params) {
            TeamDAO_interface dao = new TeamDAO();
            return dao.findByPK(params[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //註冊廣播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        registerChangeDataReceiver();
        setContentView(R.layout.activity_gameday_manage);
        rvGameDay = (RecyclerView) findViewById(R.id.rvGameDay);
        Bundle bundle = getIntent().getExtras();
        tourn_id = bundle.getString("tourn_id");
        String tourn_name = bundle.getString("tourn_name");
        setTitle(tourn_name);

        if (networkConnected()) {
            retrieveGamedayTask = new RetrieveGamedayTask().execute(tourn_id);
        } else {
            //沒連上網路
        }
        //
//        rvGameDay.setHasFixedSize(true);
//        rvGameDay.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
//        List<GamedayVO> gamedayVOList = new ArrayList<>();
//        GamedayVO g = new GamedayVO();
//        g.setTeam_a_id("ffffffffffffff");
//        gamedayVOList.add(g);
//        GamedayVO d = new  GamedayVO();
//        d.setTeam_a_id("55555555");
//        gamedayVOList.add(d);
//
//        adapter = new GamedayAdapter(gamedayVOList);
//        rvGameDay.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(changeDataReceiver);
    }

    public void showTournRecyclerView (List<GamedayVO> gamedayVOs) {
        rvGameDay.setHasFixedSize(true);
        rvGameDay.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        adapter = new GamedayAdapter(gamedayVOs);
        rvGameDay.setAdapter(adapter);
    }

    private void registerChangeDataReceiver() {
        IntentFilter filter = new IntentFilter(Util.BROADCAST_GAMEDAY);
        changeDataReceiver = new ChangeDataReceiver();
        localBroadcastManager.registerReceiver(changeDataReceiver, filter);
        Log.d(TAG,"Broadcast registered");
    }

    private class GamedayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<GamedayVO> gamedayList;
        private boolean[] gamedayButtonExpanded;

        public GamedayAdapter (List<GamedayVO> gamedayList) {
            this.gamedayList = gamedayList;
            this.gamedayButtonExpanded = new boolean[gamedayList.size()];
        }
        //尚未開始的比賽
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTeamAName,tvTeamBName,tvGamedayTime,tvGamedayDate,tvGamedayName;
            private ImageView ivTeamALogo,ivTeamBLogo;
            private LinearLayout manageBtnLayout;
            private Button btnDataRecord,btnCheckTeam;
            public ViewHolder (View view) {
                super(view);
                tvTeamAName = (TextView) view.findViewById(R.id.tvTeamAName);
                tvTeamBName = (TextView) view.findViewById(R.id.tvTeamBName);
                tvGamedayTime = (TextView) view.findViewById(R.id.tvGamedayTime);
                tvGamedayDate = (TextView) view.findViewById(R.id.tvGamedayDate);
                tvGamedayName = (TextView) view.findViewById(R.id.tvGamedayName);
                ivTeamALogo = (ImageView) view.findViewById(R.id.ivTeamALogo);
                ivTeamBLogo = (ImageView) view.findViewById(R.id.ivTeamBLogo);
                manageBtnLayout = (LinearLayout) view.findViewById(R.id.manageBtnLayout);
                btnCheckTeam = (Button) view.findViewById(R.id.btnCheckTeam);
                btnDataRecord = (Button) view.findViewById(R.id.btnDataRecord);

            }
        }
        //已結束比賽
        class ViewHolderOver extends RecyclerView.ViewHolder {
            private TextView tvTeamAName,tvTeamBName,tvGamedayName,tvGamedayScore;
            private ImageView ivTeamALogo,ivTeamBLogo;

            public ViewHolderOver (View view) {
                super(view);
                tvTeamAName = (TextView) view.findViewById(R.id.tvTeamAName);
                tvTeamBName = (TextView) view.findViewById(R.id.tvTeamBName);
                tvGamedayName = (TextView) view.findViewById(R.id.tvGamedayName);
                ivTeamALogo = (ImageView) view.findViewById(R.id.ivTeamALogo);
                ivTeamBLogo = (ImageView) view.findViewById(R.id.ivTeamBLogo);
                tvGamedayScore = (TextView) view.findViewById(R.id.tvGamedayScore);

            }
        }

        @Override
        public int getItemViewType(int position) {
            GamedayVO gamedayVO = gamedayList.get(position);
            if (gamedayVO.getGameday_status().equals("比賽結束")) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            switch (viewType) {
                case 0:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gameday_over,parent,false);
                    return new GamedayAdapter.ViewHolderOver(v);
                case 1:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gameday,parent,false);
                    return new ViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final GamedayVO gamedayVO = gamedayList.get(position);
            if (gamedayVO != null) {
                switch (holder.getItemViewType()) {
                    case 0:
                        ViewHolderOver viewHolderOver = (ViewHolderOver) holder;
                        //GET TEAM A
//                        try {
//                            GetTeamTask task = new GetTeamTask();
//                            TeamVO teamVO = task.execute(gamedayVO.getTeam_a_id()).get();
//                            viewHolderOver.tvTeamAName.setText(teamVO.getTeam_name());
//                            viewHolderOver.ivTeamALogo
//                                    .setImageBitmap(BitmapFactory.decodeByteArray(teamVO.getTeam_logo(),0,teamVO.getTeam_logo().length));
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
                        viewHolderOver.tvTeamAName.setText(gamedayVO.getTeamAVO().getTeam_name());
                        byte[] teamAOver = Base64.decode(gamedayVO.getTeamAVO().getTeam_logo_base64(),0);
                        viewHolderOver.ivTeamALogo
                                .setImageBitmap(BitmapFactory.decodeByteArray(teamAOver,0,teamAOver.length));
                        //GET TEAM B
//                        try {
//                            GetTeamTask task = new GetTeamTask();
//                            TeamVO teamVO = task.execute(gamedayVO.getTeam_b_id()).get();
//                            viewHolderOver.tvTeamBName.setText(teamVO.getTeam_name());
//                            viewHolderOver.ivTeamBLogo
//                                    .setImageBitmap(BitmapFactory.decodeByteArray(teamVO.getTeam_logo(),0,teamVO.getTeam_logo().length));
//                        }  catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
                        viewHolderOver.tvTeamBName.setText(gamedayVO.getTeamBVO().getTeam_name());
                        byte[] teamBOver = Base64.decode(gamedayVO.getTeamBVO().getTeam_logo_base64(),0);
                        viewHolderOver.ivTeamBLogo
                                .setImageBitmap(BitmapFactory.decodeByteArray(teamBOver,0,teamBOver.length));
                        viewHolderOver.tvGamedayName.setText(gamedayVO.getGameday_name());
                        viewHolderOver.tvGamedayScore.setText(gamedayVO.getTeam_a_score()+"   終場   "+gamedayVO.getTeam_b_score());
                        break;
                    case 1:
                        String teamAName = null ,teamBName = null;
                        ViewHolder viewHolder = (ViewHolder) holder;
                        //GET TEAM A
//                        try {
//                            GetTeamTask task = new GetTeamTask();
//                            TeamVO teamVO = task.execute(gamedayVO.getTeam_a_id()).get();
//                            teamAName = teamVO.getTeam_name();
//                            viewHolder.tvTeamAName.setText(teamAName);
//                            viewHolder.ivTeamALogo
//                                    .setImageBitmap(BitmapFactory.decodeByteArray(teamVO.getTeam_logo(),0,teamVO.getTeam_logo().length));
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
                        viewHolder.tvTeamAName.setText(gamedayVO.getTeamAVO().getTeam_name());
                        byte[] teamA = Base64.decode(gamedayVO.getTeamAVO().getTeam_logo_base64(),0);
                        viewHolder.ivTeamALogo
                                    .setImageBitmap(BitmapFactory.decodeByteArray(teamA,0,teamA.length));
                        // GET TEAM B
//                        try {
//                            GetTeamTask task = new GetTeamTask();
//                            TeamVO teamVO = task.execute(gamedayVO.getTeam_b_id()).get();
//                            teamBName = teamVO.getTeam_name();
//                            viewHolder.tvTeamBName.setText(teamBName);
//                            viewHolder.ivTeamBLogo
//                                    .setImageBitmap(BitmapFactory.decodeByteArray(teamVO.getTeam_logo(),0,teamVO.getTeam_logo().length));
//                        } catch (InterruptedException | ExecutionException e) {
//                            e.printStackTrace();
//                        }
                        viewHolder.tvTeamBName.setText(gamedayVO.getTeamBVO().getTeam_name());
                        byte[] teamB = Base64.decode(gamedayVO.getTeamBVO().getTeam_logo_base64(),0);
                        viewHolder.ivTeamBLogo
                                .setImageBitmap(BitmapFactory.decodeByteArray(teamB,0,teamB.length));
                        //賽事名稱
                        viewHolder.tvGamedayName.setText(gamedayVO.getGameday_name());
                        //賽事日期
                        if (gamedayVO.getGameday_time() != null) {
                            String gamedayDate = gamedayVO.getGameday_time().toString();
                            viewHolder.tvGamedayDate.setText(gamedayDate.substring(0,10));
                            //賽事時間
                            viewHolder.tvGamedayTime.setText(gamedayDate.substring(10,16));
                        }

                        //伸縮子layout
                        viewHolder.itemView.setOnClickListener(new MyListener(position));
                        viewHolder.manageBtnLayout.setVisibility(gamedayButtonExpanded[position] ? View.VISIBLE : View.GONE);

                        bundle = new Bundle();
                        //報到管理按鈕監聽器
                        viewHolder.btnCheckTeam.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //put data into bundle
                                bundle.putString("Tourn_id",gamedayVO.getTourn_id());
                                bundle.putString("Team_a_id",gamedayVO.getTeam_a_id());
                                bundle.putString("Team_b_id",gamedayVO.getTeam_b_id());
                                bundle.putString("Gameday_name",gamedayVO.getGameday_name());
                                bundle.putString("Gameday_id",gamedayVO.getGameday_id());
                                //pass data to CheckTeamActivity
                                Intent i = new Intent(GamedayManageActivity.this,CheckTeamActivity.class);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        });
                        //數據紀錄
                        viewHolder.btnDataRecord.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent i = new Intent(GamedayManageActivity.this,DataRecordActivity.class);
//                                i.putExtras(bundle);
//                                startActivity(i);
                            }
                        });
                        break;
                }
            }
        }
        //監聽itemview被按壓的事件
        class MyListener implements  View.OnClickListener {
            private int position;
            public MyListener (int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                adapter.expand(position);
            }
        }

        void expand(int position) {
            for (int i = 0; i < gamedayButtonExpanded.length; i++) {
                gamedayButtonExpanded[i] = false;
            }
            gamedayButtonExpanded[position] = true;
//            gamedayButtonExpanded[position] = !gamedayButtonExpanded[position];
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return gamedayList.size();
        }
    }

    private class ChangeDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"QQQQQQQQQQQQQQQQQQQQQQQQ");
            if (networkConnected()) {
                AsyncTask retrieveGamedayTask = new RetrieveGamedayTask().execute(tourn_id);
            } else {
                //沒連上網路
            }
        }
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
