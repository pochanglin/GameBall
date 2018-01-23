package idv.allen.gameball.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.membership.MembershipDAO;
import idv.allen.gameball.membership.MembershipDAO_interface;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.plate_appearance.GameVO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO;
import idv.allen.gameball.plate_appearance.Plate_appearanceDAO_interface;
import idv.allen.gameball.plate_appearance.Plate_appearanceVO;
import idv.allen.gameball.plate_appearance.PlayerVO;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

import static idv.allen.gameball.util.Util.gamedayLiveWS;


public class PlateFragment extends Fragment {
    private static final String TAG = "PlateFragment";
    private Bundle bundle;
    private TextView tvInning,tvOut,tvAttack,tvBattingOrder,tvJerseyNumber,tvPlayerName,tvFieldPosition;
    private ImageView ivPlayerPic;
    private Spinner spDirection,spResult,spErrorHitter,spPoint,spOut;
    private Button btnSendResult,btnOverGame;
    private String bat_dir_id,pa_rslt_id;
    private Integer pa_rbi,pa_out;
    private GameVO game;
    private LocalBroadcastManager localBroadcastManager;
    private Dialog overDialog;

    public PlateFragment() {
        super();
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    class SendPaPointTask extends AsyncTask<String,Void,JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.SendPlateContent(params[0],params[1],params[2],params[3]);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            List<String> bat_outs = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String bat_out = jsonObject.getString(iterator.next());
                        bat_outs.add("造成 "+bat_out + " 出局");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, bat_outs);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spOut.setAdapter(adapter);
        }
    }

    class SendPaResultTask extends AsyncTask<String,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(String... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.SendPlateContent(params[0],params[1],params[2],"");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            List<String> bat_points = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String bat_point = jsonObject.getString(iterator.next());
                        bat_points.add(bat_point + " 分打點");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, bat_points);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPoint.setAdapter(adapter);
        }
    }

    class SendPaDirectTask extends AsyncTask<String,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(String... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.SendPlateContent(params[0],params[1],"","");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            List<String> bat_results = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String bat_result = jsonObject.getString(iterator.next());
                        bat_results.add(bat_result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, bat_results);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spResult.setAdapter(adapter);
        }
    }

    class SendPlateResultTask extends AsyncTask<Plate_appearanceVO,Void,GameVO> {
        @Override
        protected GameVO doInBackground(Plate_appearanceVO... params) {
            Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
            return dao.sendPlateResult(params[0]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plate,container,false);
        //
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        findViews(view);
        //從checkTeamAcitivity得到game物件
        String gameJSON = bundle.getString("gameJSON");
        Gson gson = new Gson();
        game = gson.fromJson(gameJSON,GameVO.class);
//        //建立ws連線
//        Util.connectServer(getContext(), "00000");
        //取出局數
        int inning = game.getInning();
        int currentInning=inning/2+inning%2;
        boolean halfInning=(inning%2==1);//True(上半局)False(下半局)
        if (halfInning) {
            tvInning.setText("第 "+String.valueOf(currentInning)+" 局上");
        } else {
            tvInning.setText("第 "+String.valueOf(currentInning)+" 局下");
        }
        //取出出局數
        tvOut.setText("出局數："+String.valueOf(game.getOut()));
        //取出攻擊方
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(game.getFirstTeam()).get();
            tvAttack.setText("攻擊方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //取出打者物件
        PlayerVO playerVO = game.getPlayerList().get(game.getFirstTeam()).get(game.getFirstSeq());
        tvBattingOrder.setText("第 "+playerVO.getBattingOrder()+" 棒");
        tvJerseyNumber.setText("背號 "+playerVO.getJerseyNumber()+" 號");
        tvPlayerName.setText("球員："+playerVO.getPlayerName());
        tvFieldPosition.setText("守備位置："+playerVO.getFieldPosition());
        //抓打者照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(playerVO.getPlayerId()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPlayerPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }
    public void findViews(View view) {
        //局數
        tvInning = (TextView) view.findViewById(R.id.tvInning);
        //出局數
        tvOut = (TextView) view.findViewById(R.id.tvOut);
        //攻擊方
        tvAttack = (TextView) view.findViewById(R.id.tvAttack);
        //棒次
        tvBattingOrder = (TextView) view.findViewById(R.id.tvBattingOrder);
        //背號
        tvJerseyNumber = (TextView) view.findViewById(R.id.tvJerseyNumber);
        //打者名稱
        tvPlayerName = (TextView) view.findViewById(R.id.tvPlayerName);
        //守備位置
        tvFieldPosition = (TextView) view.findViewById(R.id.tvFieldPosition);
        //打者照片
        ivPlayerPic = (ImageView) view.findViewById(R.id.ivPlayerPic);
        //打擊方向
        spDirection = (Spinner) view.findViewById(R.id.spDirection);
        spDirection.setSelection(0, true);
        spDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bat_dir_id = transferDirectionToBatDir(parent.getItemAtPosition(position).toString());
                SendPaDirectTask task = new SendPaDirectTask();
                task.execute(game.getGamedayId(),bat_dir_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //打擊結果
        spResult = (Spinner) view.findViewById(R.id.spResult);
        spResult.setSelection(0, true);
        spResult.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pa_rslt_id = transferPaResultToPaRsltId(parent.getItemAtPosition(position).toString());
                SendPaResultTask task = new SendPaResultTask();
                task.execute(game.getGamedayId(),bat_dir_id,pa_rslt_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //失誤
//        spErrorHitter = (Spinner) view.findViewById(R.id.spErrorHitter);
        //打點
        spPoint = (Spinner) view.findViewById(R.id.spPoint);
        spPoint.setSelection(0, true);
        spPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pa_rbi = transferPaPointToRBI(parent.getItemAtPosition(position).toString());
                SendPaPointTask task = new SendPaPointTask();
                task.execute(game.getGamedayId(),bat_dir_id,pa_rslt_id,String.valueOf(pa_rbi));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //出局數
        spOut = (Spinner) view.findViewById(R.id.spOut);
        spOut.setSelection(0, true);
        spOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pa_out = transferPaOut(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //送出打席結果按鈕
        btnSendResult = (Button) view.findViewById(R.id.btnSendResult);
        btnSendResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult();
            }
        });
        //結束比賽按鈕
        btnOverGame = (Button) view.findViewById(R.id.btnOverGame);
        btnOverGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overDialog = new Dialog(getActivity());
                overDialog.setCancelable(true);
                overDialog.setContentView(R.layout.dialog_over_game);
                Window dialogWindow = overDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = 1000;
                lp.alpha = 1.0f;
                dialogWindow.setAttributes(lp);
                findViewsOnDialog();
                overDialog.show();
            }
        });
    }
    public void findViewsOnDialog() {
        TextView tvWait = (TextView) overDialog.findViewById(R.id.tvWait);
        tvWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overDialog.cancel();
            }
        });
        TextView tvOver = (TextView) overDialog.findViewById(R.id.tvOver);
        tvOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overGame();
                getActivity().finish();
            }
        });
    }
    public void overGame(){
        new AsyncTask<String,Void,Void>() {
            @Override
            protected Void doInBackground(String... params) {
                Plate_appearanceDAO_interface dao = new Plate_appearanceDAO();
                dao.OverGame(bundle.getString("tourn_id"),game.getGamedayId());
                return null;
            }
        }.execute();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "end");
        jsonObject.addProperty("gameday_id", game.getGamedayId());
        gamedayLiveWS.send(jsonObject.toString());
    }

    GameVO gameVO;
    public void sendResult() {
        Plate_appearanceVO plate_appearanceVO = new Plate_appearanceVO();
        plate_appearanceVO.setGameday_id(game.getGamedayId());
        plate_appearanceVO.setBat_dir_id(bat_dir_id);
        plate_appearanceVO.setPa_rslt_id(pa_rslt_id);
        plate_appearanceVO.setPa_rbi(pa_rbi);
        plate_appearanceVO.setPa_out(pa_out);
        //送出打席結果到ws
        JsonObject jsonObject = new JsonObject();
        String plateJSON = new Gson().toJson(plate_appearanceVO);
        jsonObject.addProperty("action", "pa_result");
        jsonObject.addProperty("pa_result", plateJSON);
        gamedayLiveWS.send(jsonObject.toString());
        Log.d(TAG, "output: " + plateJSON);
        //送出打席結果到控制器
        SendPlateResultTask task = new SendPlateResultTask();
        try {
            gameVO = task.execute(plate_appearanceVO).get();
            Log.d(TAG,String.valueOf(gameVO.getOut()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //
        if (gameVO.getErrorMsgs() != null) {
            List<String> errorMsgs = gameVO.getErrorMsgs();
            Toast.makeText(getActivity(),errorMsgs.get(0),Toast.LENGTH_SHORT).show();
            for (int i = 0; i < errorMsgs.size(); i++) {
                String errorMsg = errorMsgs.get(i);
                errorMsgs.remove(errorMsg);
//                gameVO.setErrorMsgs(null);
//                gameVO.getErrorMsgs().removeAll(gameVO.getErrorMsgs());

            }
            return;
        }
        //發送廣播
        String gameVOJSON = new Gson().toJson(gameVO);
        Bundle b = new Bundle();
        b.putString("gameVOJSON",gameVOJSON);
        Intent intent = new Intent(Util.BROADCAST_PLATE);
        intent.putExtras(b);
        localBroadcastManager.sendBroadcast(intent);
        //送出打席後切換為投手頁
        ViewPager vpDataRecord = (ViewPager)getActivity().findViewById(R.id.vpDataRecord);
        vpDataRecord.setCurrentItem(0);
        //取出局數
        int inning = gameVO.getInning();
        int currentInning=inning/2+inning%2;
        boolean halfInning=(inning%2==1);//True(上半局)False(下半局)
        if (halfInning) {
            tvInning.setText("第 "+String.valueOf(currentInning)+" 局上");
            setFirstAttackTeam();
        } else {
            tvInning.setText("第 "+String.valueOf(currentInning)+" 局下");
            setSecondAttackTeam();
        }
    }
    public void setSecondAttackTeam() {
        //取出出局數
        tvOut.setText("出局數："+String.valueOf(gameVO.getOut()));
        //取出攻擊方
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(gameVO.getSecondTeam()).get();
            tvAttack.setText("攻擊方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //取出打者物件
        PlayerVO playerVO = gameVO.getPlayerList().get(gameVO.getSecondTeam()).get(gameVO.getSecondSeq());
        tvBattingOrder.setText("第 "+playerVO.getBattingOrder()+" 棒");
        tvJerseyNumber.setText("背號 "+playerVO.getJerseyNumber()+" 號");
        tvPlayerName.setText("球員："+playerVO.getPlayerName());
        tvFieldPosition.setText("守備位置："+playerVO.getFieldPosition());
        //抓打者照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(playerVO.getPlayerId()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPlayerPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void setFirstAttackTeam() {
        //取出出局數
        tvOut.setText("出局數："+String.valueOf(gameVO.getOut()));
        //取出攻擊方
        TeamVO teamVO;
        try {
            teamVO = new AsyncTask<String,Void,TeamVO>() {
                @Override
                protected TeamVO doInBackground(String... params) {
                    TeamDAO_interface dao = new TeamDAO();
                    return dao.findByPK(params[0]);
                }
            }.execute(gameVO.getFirstTeam()).get();
            tvAttack.setText("攻擊方:"+teamVO.getTeam_name());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //取出打者物件
        PlayerVO playerVO = gameVO.getPlayerList().get(gameVO.getFirstTeam()).get(gameVO.getFirstSeq());
        tvBattingOrder.setText("第 "+playerVO.getBattingOrder()+" 棒");
        tvJerseyNumber.setText("背號 "+playerVO.getJerseyNumber()+" 號");
        tvPlayerName.setText("球員："+playerVO.getPlayerName());
        tvFieldPosition.setText("守備位置："+playerVO.getFieldPosition());
        //抓打者照片
        MembershipVO membershipVO;
        try {
            membershipVO = new AsyncTask<String,Void,MembershipVO>() {
                @Override
                protected MembershipVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.findByPrimaryKey(params[0]);
                }
            }.execute(playerVO.getPlayerId()).get();
            Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
            ivPlayerPic.setImageBitmap(bitmap);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    public Integer transferPaOut(String item) {
        switch (item) {
            case"造成 0 出局":
                return 0;
            case"造成 1 出局":
                return 1;
            case"造成 2 出局":
                return 2;
            case"造成 3 出局":
                return 3;
            default:
                return null;
        }
    }
    public Integer transferPaPointToRBI(String item) {
        switch (item) {
            case"0 分打點":
                return 0;
            case"1 分打點":
                return 1;
            case"2 分打點":
                return 2;
            case"3 分打點":
                return 3;
            case"4 分打點":
                return 4;
            default:
                return null;
        }
    }
    public String transferPaResultToPaRsltId(String item) {
        switch (item) {
            case"一壘安打":
                return "BR001";
            case"二壘安打":
                return "BR002";
            case"三壘安打":
                return "BR003";
            case"全壘打":
                return "BR004";
            case"野手選擇":
                return "BR005";
            case"保送":
                return "BR006";
            case"三振":
                return "BR007";
            case"飛球出局":
                return "BR008";
            case"滾地出局":
                return "BR009";
            case"高飛犧牲打":
                return "BR010";
            case"失誤":
                return "BR011";
            default:
                return null;
        }
    }
    public String transferDirectionToBatDir(String item) {
        switch (item) {
            case"無":
                return "BD001";
            case"投手":
                return "BD002";
            case"一壘手":
                return "BD003";
            case"二壘手":
                return "BD004";
            case"三壘手":
                return "BD005";
            case"游擊手":
                return "BD006";
            case"自由手":
                return "BD007";
            case"左外野":
                return "BD008";
            case"中外野":
                return "BD009";
            case"右外野":
                return "BD010";
            default:
                return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
