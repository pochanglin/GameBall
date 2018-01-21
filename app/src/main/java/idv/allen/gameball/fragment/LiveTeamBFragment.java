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


public class LiveTeamBFragment extends Fragment {
    private final static String TAG = "LiveTeamBFragment";
    private LocalBroadcastManager broadcastManager;
    private Bundle bundle;
    private List<Plate_appearanceVO> paList = new ArrayList<>();
    private Map<String, String> rosterMap;
    private TextView tvGamedayScore;
    private RecyclerView rvTeamBLive;
    private String teamB;

    class GetAllRosterTask extends AsyncTask<String,Void,Map<String,String>> {

        @Override
        protected Map<String, String> doInBackground(String... params) {
            MembershipDAO_interface dao = new MembershipDAO();
            return dao.getAllRoster(params[0],params[1]);
        }
    }

    public LiveTeamBFragment() {
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        registerTeamBLiveReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_team_b, container, false);
        teamB =  bundle.getString("team_b_id");
        //get data from bundle
        String gamedayVOJSON = bundle.getString("gamedayVOJSON");
        GamedayVO gamedayVO = new Gson().fromJson(gamedayVOJSON , GamedayVO.class);
        //get teamB roster
        GetAllRosterTask task = new GetAllRosterTask();
        try {
            rosterMap = task.execute(gamedayVO.getTourn_id(), gamedayVO.getTeam_b_id()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //score
        tvGamedayScore = (TextView) getActivity().findViewById(R.id.tvGamedayScore);
        //recyclerview
        rvTeamBLive = view.findViewById(R.id.rvTeamBLive);
        showPlateRecyclerView();
        return view;
    }

    private class TeamBLiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public TeamBLiveAdapter() {
        }

        //contain inning
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

        //plate
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
        public int getItemViewType(int position) {
            Plate_appearanceVO plate_appearanceVO = paList.get(position);
            //type等於1，表示已換局
            if (plate_appearanceVO.getType() == 1) {
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
                //呈現包含局數的view
                case 0 :
                    InningViewHolder inningViewHolder = (InningViewHolder)holder;
                    int inning = plate_appearanceVO.getInning();
                    PlayerVO playerVO = plate_appearanceVO.getPlayerVO();
                    Log.d(TAG,playerVO.getPlayerId());
                    //打者照片
                    byte[] player1 = Base64.decode(rosterMap.get(playerVO.getPlayerId()),0);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(player1,0,player1.length);
                    inningViewHolder.ivPlayerPic.setImageBitmap(bitmap);
                    //局數
                    inningViewHolder.tvInning.setText("第 "+String.valueOf(inning/2+inning%2)+" 局" + getTopOrBottom(inning));
                    //棒次、打者
                    inningViewHolder.tvPlayer.setText("第 "+playerVO.getBattingOrder()+" 棒  "+playerVO.getPlayerName());
                    //打擊方向、內容
                    inningViewHolder.tvPaResult.setText("擊出 "+(plate_appearanceVO.getBat_dir_id())+" 方向 "
                            +(plate_appearanceVO.getPa_rslt_id()));
                    //出局數、打點
                    inningViewHolder.tvOutPoint.setText("造成："+plate_appearanceVO.getPa_out()+"出局 "+plate_appearanceVO.getPa_rbi()+"分打點");
                    //動畫
                    if (position == paList.size()-1) {
                        inningViewHolder.itemView.startAnimation(getShakeAnimation());
                    }
                    break;
                case 1 :
                    PlateViewHolder plateViewHolder = (PlateViewHolder)holder;
                    PlayerVO playerVO2 = plate_appearanceVO.getPlayerVO();
                    Log.d(TAG,playerVO2.getPlayerName());
                    //打者照片
                    byte[] player2 = Base64.decode(rosterMap.get(playerVO2.getPlayerId()),0);
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(player2,0,player2.length);
                    plateViewHolder.ivPlayerPic.setImageBitmap(bitmap2);
                    //棒次、打者
                    plateViewHolder.tvPlayer.setText("第 "+playerVO2.getBattingOrder()+" 棒  "+playerVO2.getPlayerName());
                    //打擊方向、內容
                    plateViewHolder.tvPaResult.setText("擊出 "+(plate_appearanceVO.getBat_dir_id())+" 方向 "
                            +(plate_appearanceVO.getPa_rslt_id()));
                    //出局數、打點
                    plateViewHolder.tvOutPoint.setText("造成："+plate_appearanceVO.getPa_out()+"出局 "+plate_appearanceVO.getPa_rbi()+"分打點");
                    //動畫
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
    }

    private class TeamBLiveReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pa_appearance = intent.getStringExtra("pa_appearance");
            Log.d(TAG, pa_appearance);
            JsonObject jsonObject = new Gson().fromJson(pa_appearance, JsonObject.class);
            String team = jsonObject.get("team").getAsString();

            if (teamB.equals(team)) {
                String plateJSON = jsonObject.get("plateJSON").getAsString();
                Plate_appearanceVO plate_appearanceVO = new Gson().fromJson(plateJSON,Plate_appearanceVO.class);
                //將team b打席加入集合
                paList.add(plate_appearanceVO);
                //更改比分
                String firstPoint = jsonObject.get("firstPoint").getAsString();
                String secondPoint = jsonObject.get("secondPoint").getAsString();
                tvGamedayScore.setText(firstPoint+"   終場   "+secondPoint);
                rvTeamBLive.getAdapter().notifyDataSetChanged();
                rvTeamBLive.smoothScrollToPosition(paList.size()-1);
                //跳轉至當前頁面
                ViewPager vpGamedayLive = (ViewPager)getActivity().findViewById(R.id.vpGamedayLive);
                vpGamedayLive.setCurrentItem(1);
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

    private void registerTeamBLiveReceiver() {
        Log.d(TAG,"註冊廣播囉~~");
        IntentFilter intentFilter = new IntentFilter(Util.WS_PLATE);
        TeamBLiveReceiver teamBLiveReceiver = new TeamBLiveReceiver();
        broadcastManager.registerReceiver(teamBLiveReceiver,intentFilter);
    }

    public void showPlateRecyclerView () {
        rvTeamBLive.setHasFixedSize(true);
        rvTeamBLive.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        TeamBLiveAdapter adapter = new TeamBLiveAdapter();
        rvTeamBLive.setAdapter(adapter);
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
