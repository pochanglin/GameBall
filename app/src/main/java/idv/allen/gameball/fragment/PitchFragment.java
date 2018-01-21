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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import idv.allen.gameball.R;
import idv.allen.gameball.membership.MembershipDAO;
import idv.allen.gameball.membership.MembershipDAO_interface;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.plate_appearance.GameVO;
import idv.allen.gameball.plate_appearance.PitcherVO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO_interface;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

import static idv.allen.gameball.util.Util.gamedayLiveWS;


public class PitchFragment extends Fragment {
    private static final String TAG = "PitchFragment";
    private Bundle bundle;
    private TextView tvCurrentBallTotal,tvCurrentBallNum,tvDefend,tvPitcher;
    private ImageView ivPitcherPic;
    private GameVO game;
    private RadioGroup rgChoseBall;
    String pitchResult;
    private PitchReceiver pitchReceiver;
    private LocalBroadcastManager localBroadcastManager;

    public PitchFragment() {
        super();
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    private class PitchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"接到廣播了");
            Bundle b = intent.getExtras();
            String gameVOJSON = b.getString("gameVOJSON");
            GameVO gameVO = new Gson().fromJson(gameVOJSON,GameVO.class);
            changeData(gameVO);
            showDialog(gameVO);

        }
    }
    private void registerPitchReceiver() {
        IntentFilter filter = new IntentFilter(Util.BROADCAST_PLATE);
        pitchReceiver = new PitchReceiver();
        localBroadcastManager.registerReceiver(pitchReceiver, filter);
        Log.d(TAG,"Broadcast registered");
    }

    public void changeData(GameVO game) {
        int inning = game.getInning();
        boolean halfInning=(inning%2==1);//True(上半局)False(下半局)
        if (halfInning) {
            //後攻隊
            setSecondDefendTeam(game);
        } else {
            //先攻隊
            setFirstDefendTeam(game);
        }
    }
    int currentInning = 1;
    public void showDialog(GameVO game) {
        int inning = game.getInning();
        if (currentInning != inning) {
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            pDialog.setTitleText("攻守交換!");
            pDialog.setCustomImage(R.drawable.logo7);
            pDialog.show();
            currentInning = inning;
        }
    }

    class SendContentTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.sendPitchResult(params[0],params[1]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pitch,container,false);
        //註冊廣播接收器
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        registerPitchReceiver();
        findViews(view);
        //從checkTeamAcitivity得到game物件
        String gameJSON = bundle.getString("gameJSON");
        Gson gson = new Gson();
        game = gson.fromJson(gameJSON,GameVO.class);
        //初始球數面板顯示
        tvCurrentBallTotal.setText("第 "+String.valueOf(game.getCurrentNumBall()) + " 球");
        tvCurrentBallNum.setText(String.valueOf(game.getCurrentStrike())+" 好 "+String.valueOf(game.getCurrentBall())+" 壞");
        //取出防守球隊
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(game.getSecondTeam()).get();
            tvDefend.setText("防守方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //從Game物件取出投手
        PitcherVO pitcherVO = game.getPitcherList().get(game.getSecondTeam()).get(game.getSecondPitcher());
        tvPitcher.setText("投    手:"+pitcherVO.getPitcherName());
        //抓投手照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(game.getSecondPitcher()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPitcherPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void findViews(View view) {
        //總球數
        tvCurrentBallTotal = (TextView) view.findViewById(R.id.tvCurrentBallTotal);
        //這人次好壞球數
        tvCurrentBallNum = (TextView) view.findViewById(R.id.tvCurrentBallNum);
        //防守隊
        tvDefend = (TextView) view.findViewById(R.id.tvDefend);
        //投手
        tvPitcher = (TextView) view.findViewById(R.id.tvPitcher);
        //投手照片
        ivPitcherPic = (ImageView) view.findViewById(R.id.ivPitcherPic);

        rgChoseBall = (RadioGroup) view.findViewById(R.id.rgChoseBall);
        //送出投球內容按鈕
        Button btnSendContent = (Button)view.findViewById(R.id.btnSendContent);
        btnSendContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pitchResult = getDataFromRadio();
                //發送投球推播
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "pitchResult");
                jsonObject.addProperty("pitchResult", pitchResult);
                jsonObject.addProperty("gameday_id", game.getGamedayId());
                gamedayLiveWS.send(jsonObject.toString());

                SendContentTask task = new SendContentTask();
                try {
                    String ballInfoJSON = task.execute(game.getGamedayId(),pitchResult).get();
                    Map ballInfo = new Gson().fromJson(
                            ballInfoJSON, new TypeToken<HashMap<String, String>>() {}.getType()
                    );
                    Log.d(TAG,ballInfo.get("currentNumBall").toString());
                    //先取出錯誤訊息，若等於notError則顯示當前數據
                    if (ballInfo.get("errorPitch").toString().equals("notError")){
                        //總球數面板顯示
                        tvCurrentBallTotal.setText("第 "+ballInfo.get("currentNumBall").toString() + " 球");
                        //好壞球數面板顯示
                        tvCurrentBallNum.setText(ballInfo.get("currentStrike").toString()+" 好 "
                                + ballInfo.get("currentBall").toString() + " 壞" );
                    } else {
                        Toast.makeText(getActivity(),ballInfo.get("errorPitch").toString(),Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public String getDataFromRadio() {
        switch (rgChoseBall.getCheckedRadioButtonId()) {
            case R.id.rbStrike:
                pitchResult = "strike";
                return pitchResult;
            case R.id.rbBall:
                pitchResult = "ball";
                return pitchResult;
            default:
                pitchResult = null;
                return pitchResult;
        }
    }

    private void setFirstDefendTeam(GameVO game){
        //初始球數面板顯示
        tvCurrentBallTotal.setText("第 "+String.valueOf(game.getCurrentNumBall()) + " 球");
        tvCurrentBallNum.setText(String.valueOf(game.getCurrentStrike())+" 好 "+String.valueOf(game.getCurrentBall())+" 壞");
        //取出防守球隊
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(game.getFirstTeam()).get();
            tvDefend.setText("防守方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //從Game物件取出投手
        PitcherVO pitcherVO = game.getPitcherList().get(game.getFirstTeam()).get(game.getFirstPitcher());
        tvPitcher.setText("投    手:"+pitcherVO.getPitcherName());
        //抓投手照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(game.getFirstPitcher()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPitcherPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setSecondDefendTeam(GameVO game){
        //初始球數面板顯示
        tvCurrentBallTotal.setText("第 "+String.valueOf(game.getCurrentNumBall()) + " 球");
        tvCurrentBallNum.setText(String.valueOf(game.getCurrentStrike())+" 好 "+String.valueOf(game.getCurrentBall())+" 壞");
        //取出防守球隊
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(game.getSecondTeam()).get();
            tvDefend.setText("防守方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //從Game物件取出投手
        PitcherVO pitcherVO = game.getPitcherList().get(game.getSecondTeam()).get(game.getSecondPitcher());
        tvPitcher.setText("投    手:"+pitcherVO.getPitcherName());
        //抓投手照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(game.getSecondPitcher()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPitcherPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(pitchReceiver);
    }
}
