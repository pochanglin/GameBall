package idv.allen.gameball.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.gameday.GamedayVO;
import idv.allen.gameball.membership.MembershipDAO;
import idv.allen.gameball.membership.MembershipDAO_interface;
import idv.allen.gameball.plate_appearance.Plate_appearanceVO;
import idv.allen.gameball.plate_appearance.PlayerVO;
import idv.allen.gameball.util.Util;


public class LiveTeamAFragment extends Fragment {
    private final static String TAG = "LiveTeamAFragment";
    private LocalBroadcastManager broadcastManager;
    private List<Plate_appearanceVO> paList = new ArrayList<>();
    private RecyclerView rvTeamALive;
    private Bundle bundle;
    private TextView tvGamedayScore;
    private Map<String, String> rosterMap;
    private String teamA;

    class GetAllRosterTask extends AsyncTask<String,Void,Map<String,String>>{

        @Override
        protected Map<String, String> doInBackground(String... params) {
            MembershipDAO_interface dao = new MembershipDAO();
            return dao.getAllRoster(params[0],params[1]);
        }
    }

    public LiveTeamAFragment() {
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        registerTeamALiveReceiver();

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_team_a, container, false);

        teamA =  bundle.getString("team_a_id");

        String gamedayVOJSON = bundle.getString("gamedayVOJSON");
        Gson gson = new Gson();
        GamedayVO gamedayVO = gson.fromJson(gamedayVOJSON , GamedayVO.class);

        GetAllRosterTask task = new GetAllRosterTask();
        try {
            rosterMap = task.execute(gamedayVO.getTourn_id(), gamedayVO.getTeam_a_id()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        tvGamedayScore = (TextView) getActivity().findViewById(R.id.tvGamedayScore);
        // 初始化打席清單
        rvTeamALive = view.findViewById(R.id.rvTeamALive);
        showPlateRecyclerView();
        return view;
    }
    public void showPlateRecyclerView () {
        rvTeamALive.setHasFixedSize(true);
        rvTeamALive.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        TeamALiveAdapter adapter = new TeamALiveAdapter();
        rvTeamALive.setAdapter(adapter);
    }

    private void registerTeamALiveReceiver() {
        Log.d(TAG,"註冊廣播囉~~");
        IntentFilter intentFilter = new IntentFilter(Util.WS_PLATE);
        TeamALiveReceiver teamALiveReceiver = new TeamALiveReceiver();
        broadcastManager.registerReceiver(teamALiveReceiver,intentFilter);
    }

    private class TeamALiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        public TeamALiveAdapter() {

        }
        //局數
        class InningViewHolder extends RecyclerView.ViewHolder {
            private TextView tvPlayer,tvPaResult,tvOutPoint,tvInning;
            private ImageView ivPlayerPic;
            public InningViewHolder(View itemView) {
                super(itemView);
                //局數
                tvInning = (TextView) itemView.findViewById(R.id.tvInning);
                //棒次、打者
                tvPlayer = (TextView) itemView.findViewById(R.id.tvPlayer);
                //方向、結果
                tvPaResult = (TextView) itemView.findViewById(R.id.tvPaResult);
                //出局數、打點
                tvOutPoint = (TextView) itemView.findViewById(R.id.tvOutPoint);
                //打者照片
                ivPlayerPic = (ImageView) itemView.findViewById(R.id.ivPlayerPic);
            }
        }
        //打席
        class PlateViewHolder extends RecyclerView.ViewHolder {
            private TextView tvPlayer,tvPaResult,tvOutPoint;
            private ImageView ivPlayerPic;
            public PlateViewHolder(View itemView) {
                super(itemView);
                //棒次、打者
                tvPlayer = (TextView) itemView.findViewById(R.id.tvPlayer);
                //方向、結果
                tvPaResult = (TextView) itemView.findViewById(R.id.tvPaResult);
                //出局數、打點
                tvOutPoint = (TextView) itemView.findViewById(R.id.tvOutPoint);
                //打者照片
                ivPlayerPic = (ImageView) itemView.findViewById(R.id.ivPlayerPic);
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            switch (viewType) {
                case 0:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inning,parent,false);
                    return new InningViewHolder(v);
                case 1:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plate_appearance,parent,false);
                    return new PlateViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Plate_appearanceVO plate_appearanceVO = paList.get(position);
            switch (holder.getItemViewType()) {
                case 0 :
                    InningViewHolder inningViewHolder = (InningViewHolder)holder;
                    int inning = plate_appearanceVO.getInning();
                    PlayerVO playerVO = plate_appearanceVO.getPlayerVO();

                    Log.d(TAG,playerVO.getPlayerId());
                    byte[] player1 = Base64.decode(rosterMap.get(playerVO.getPlayerId()),0);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(player1,0,player1.length);
                    inningViewHolder.ivPlayerPic.setImageBitmap(bitmap);

                    inningViewHolder.tvInning.setText("第 "+String.valueOf(inning/2+inning%2)+" 局" + getTopOrBottom(inning));
                    inningViewHolder.tvPlayer.setText("第 "+playerVO.getBattingOrder()+" 棒  "+playerVO.getPlayerName());
                    inningViewHolder.tvPaResult.setText("擊出 "+(plate_appearanceVO.getBat_dir_id())+" 方向 "
                                                                +(plate_appearanceVO.getPa_rslt_id()));
                    inningViewHolder.tvOutPoint.setText("造成："+plate_appearanceVO.getPa_out()+"出局 "+plate_appearanceVO.getPa_rbi()+"分打點");

                    if (position == paList.size()-1) {
                        inningViewHolder.itemView.startAnimation(getShakeAnimation());
                    }

                    break;
                case 1 :
                    PlateViewHolder plateViewHolder = (PlateViewHolder)holder;
                    PlayerVO playerVO2 = plate_appearanceVO.getPlayerVO();

                    Log.d(TAG,playerVO2.getPlayerName());
                    byte[] player2 = Base64.decode(rosterMap.get(playerVO2.getPlayerId()),0);
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(player2,0,player2.length);
                    plateViewHolder.ivPlayerPic.setImageBitmap(bitmap2);

                    plateViewHolder.tvPlayer.setText("第 "+playerVO2.getBattingOrder()+" 棒  "+playerVO2.getPlayerName());
                    plateViewHolder.tvPaResult.setText("擊出 "+(plate_appearanceVO.getBat_dir_id())+" 方向 "
                            +(plate_appearanceVO.getPa_rslt_id()));
                    plateViewHolder.tvOutPoint.setText("造成："+plate_appearanceVO.getPa_out()+"出局 "+plate_appearanceVO.getPa_rbi()+"分打點");

                    if (position == paList.size()-1) {
                        plateViewHolder.itemView.startAnimation(getShakeAnimation());
                    }
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return paList.size();
        }

        @Override
        public int getItemViewType(int position) {
            Plate_appearanceVO plate_appearanceVO = paList.get(position);
            //type等於1，表示已換局
            if (plate_appearanceVO.getType() == 1) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    private class TeamALiveReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pa_appearance = intent.getStringExtra("pa_appearance");
            Log.d(TAG, pa_appearance);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(pa_appearance, JsonObject.class);
            String team = jsonObject.get("team").getAsString();

            if (teamA.equals(team)) {
                String plateJSON = jsonObject.get("plateJSON").getAsString();
                Plate_appearanceVO plate_appearanceVO = gson.fromJson(plateJSON,Plate_appearanceVO.class);
                //將team a打席加入集合
                paList.add(plate_appearanceVO);
                //更改比分
                String firstPoint = jsonObject.get("firstPoint").getAsString();
                String secondPoint = jsonObject.get("secondPoint").getAsString();
                tvGamedayScore.setText(firstPoint+"   終場   "+secondPoint);
                rvTeamALive.getAdapter().notifyDataSetChanged();
                rvTeamALive.smoothScrollToPosition(paList.size()-1);
                //跳轉至當前頁面
                ViewPager vpGamedayLive = (ViewPager)getActivity().findViewById(R.id.vpGamedayLive);
                vpGamedayLive.setCurrentItem(0);
            }
        }
    }

    private String getTopOrBottom(Integer inning) {
        boolean halfInning=(inning%2==1);
        if (halfInning) {
            return "上";
        } else {
            return "下";
        }
    }

    private TranslateAnimation getShakeAnimation() {
        TranslateAnimation shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
        shakeAnimation.setDuration(1000);
        CycleInterpolator cycleInterpolator = new CycleInterpolator(7);
        shakeAnimation.setInterpolator(cycleInterpolator);
        return shakeAnimation;
    }
//    public String transferPaResultToPaRsltId(String item) {
//        switch (item) {
//            case"BR001":
//                return "一壘安打";
//            case"BR002":
//                return "二壘安打";
//            case"BR003":
//                return "三壘安打";
//            case"BR004":
//                return "全壘打";
//            case"BR005":
//                return "野手選擇";
//            case"BR006":
//                return "保送";
//            case"BR007":
//                return "三振";
//            case"BR008":
//                return "飛球出局";
//            case"BR009":
//                return "滾地出局";
//            case"BR010":
//                return "高飛犧牲";
//            case"BR011":
//                return "失誤";
//            default:
//                return null;
//        }
//    }
//    public String transferDirectionToBatDir(String item) {
//        switch (item) {
//            case"BD001":
//                return "  無  ";//BD001
//            case"BD002":
//                return " 投手 ";
//            case"BD003":
//                return "一壘手";
//            case"BD004":
//                return "二壘手";
//            case"BD005":
//                return "三壘手";
//            case"BD006":
//                return "游擊手";
//            case"BD007":
//                return "自由手";
//            case"BD008":
//                return "左外野";
//            case"BD009":
//                return "中外野";
//            case"BD010":
//                return "右外野";
//            default:
//                return null;
//        }
//    }
}
