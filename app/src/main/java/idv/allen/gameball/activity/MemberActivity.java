package idv.allen.gameball.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.membership.Hitter_summaryVO;
import idv.allen.gameball.membership.MembershipDAO;
import idv.allen.gameball.membership.MembershipDAO_interface;
import idv.allen.gameball.membership.MembershipVO;
import idv.allen.gameball.membership.Pitcher_summaryVO;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.util.Util;

public class MemberActivity extends AppCompatActivity {
    private static final String TAG = "MemberActivity";
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_LOGIN = 1;
    private ImageView ivMemPic;
    private TextView tvName,tvFiled,tvHeight,tvWeight,tvBirth,tvHitGameSum,tvHitPaSum,tvHitsSum,tvHitHrSum,tvHitRBISum;
    private TextView tvPitchGameSum,tvPitchesSum,tvStrikeSum,tvSOSum,tvWinSum;
    private File file;
    private AsyncTask uploadPicTask;
    private MembershipVO membershipVO;
    SharedPreferences preferences;
    private RecyclerView rvMyTeam;

    class UploadPicTask extends AsyncTask<MembershipVO,Void,Boolean> {

        @Override
        protected Boolean doInBackground(MembershipVO... params) {
            MembershipDAO_interface dao = new MembershipDAO();
            return dao.update(params[0]);
        }
    }

    class GetMyTeamTask extends AsyncTask<String,Void,List<TeamVO>> {

        @Override
        protected List<TeamVO> doInBackground(String... params) {
            TeamDAO_interface dao = new TeamDAO();
            return dao.getAllMyTeam(params[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();

        preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memberDataJson = preferences.getString("meberData","");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        membershipVO = gson.fromJson(memberDataJson, MembershipVO.class);
        if (membershipVO == null) {
            Log.d("null",memberDataJson);
            return;
        }
        setContentView(R.layout.activity_member);
        findViews();
        //所屬球隊
        GetMyTeamTask task = new GetMyTeamTask();
        try {
            List<TeamVO> myTeamList = task.execute(membershipVO.getMem_id()).get();
            showTeamRecyclerView(myTeamList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //打者生涯數據
        try {
            Hitter_summaryVO hitter_summaryVO = new AsyncTask<String,Void,Hitter_summaryVO>() {

                @Override
                protected Hitter_summaryVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.getHitterSummary(params[0]);
                }
            }.execute(membershipVO.getMem_id()).get();
            tvHitGameSum.setText(String.valueOf(hitter_summaryVO.getHitter_summary_games()));
            tvHitPaSum.setText(String.valueOf(hitter_summaryVO.getHitter_summary_panum()));
            tvHitsSum.setText(String.valueOf(hitter_summaryVO.getHitter_summary_hits()));
            tvHitHrSum.setText(String.valueOf(hitter_summaryVO.getHitter_summary_hr()));
            tvHitRBISum.setText(String.valueOf(hitter_summaryVO.getHitter_summary_rbi()));

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        //投手生涯數據
        try {
            Pitcher_summaryVO pitcher_summaryVO = new AsyncTask<String,Void,Pitcher_summaryVO>() {

                @Override
                protected Pitcher_summaryVO doInBackground(String... params) {
                    MembershipDAO_interface dao = new MembershipDAO();
                    return dao.getPitcherSummary(params[0]);
                }
            }.execute(membershipVO.getMem_id()).get();
            tvPitchGameSum.setText(String.valueOf(pitcher_summaryVO.getPitcher_summary_games()));
            tvPitchesSum.setText(String.valueOf(pitcher_summaryVO.getPitcher_summary_pitches()));
            tvStrikeSum.setText(String.valueOf(pitcher_summaryVO.getPitcher_summary_strike()));
            tvSOSum.setText(String.valueOf(pitcher_summaryVO.getPitcher_summary_so()));
            tvWinSum.setText(String.valueOf(pitcher_summaryVO.getPitcher_summary_win()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //基本資料
        ivMemPic.setImageBitmap(BitmapFactory.decodeByteArray(
                membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length));
        tvName.setText(membershipVO.getMem_name() + "  |  (" + membershipVO.getMem_nickname() + ")");
        tvFiled.setText(membershipVO.getMem_filed() + " | " + membershipVO.getMem_bt());
        tvHeight.setText("身高 : "+membershipVO.getMem_height() + "cm");
        tvWeight.setText("體重 : "+membershipVO.getMem_weight() + "kg");
        tvBirth.setText("生日 : "+String.valueOf(membershipVO.getMem_birth()));
    }

    private void findViews() {
        //會員基本資料
        ivMemPic = (ImageView) findViewById(R.id.ivMemPic);
        tvName = (TextView) findViewById(R.id.tvName);
        tvFiled = (TextView) findViewById(R.id.tvFiled);
        tvHeight = (TextView) findViewById(R.id.tvHeight);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvBirth = (TextView) findViewById(R.id.tvBirth);
        //會員所屬球隊
        rvMyTeam = (RecyclerView) findViewById(R.id.rvMyTeam);
        //會員打擊生涯數據
        tvHitGameSum = (TextView) findViewById(R.id.tvHitGameSum);
        tvHitPaSum = (TextView) findViewById(R.id.tvHitPaSum);
        tvHitsSum = (TextView) findViewById(R.id.tvHitsSum);
        tvHitHrSum = (TextView) findViewById(R.id.tvHitHrSum);
        tvHitRBISum = (TextView) findViewById(R.id.tvHitRBISum);
        //會員投球生涯數據
        tvPitchGameSum = (TextView) findViewById(R.id.tvPitchGameSum);
        tvPitchesSum = (TextView) findViewById(R.id.tvPitchesSum);
        tvStrikeSum = (TextView) findViewById(R.id.tvStrikeSum);
        tvSOSum = (TextView) findViewById(R.id.tvSOSum);
        tvWinSum = (TextView) findViewById(R.id.tvWinSum);
    }

    public void showTeamRecyclerView (List<TeamVO> teamVOList) {
        rvMyTeam.setHasFixedSize(true);
        rvMyTeam.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rvMyTeam.setAdapter(new TeamsAdapter(teamVOList));
    }

    private class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
        private List<TeamVO> teamVOList;

        public TeamsAdapter(List<TeamVO> teamVOList) {
            this.teamVOList = teamVOList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivTeamsLogo;
            private TextView tvTeamsName;

            public ViewHolder(View itemView) {
                super(itemView);
                ivTeamsLogo = (ImageView) itemView.findViewById(R.id.ivTeamsLogo);
                tvTeamsName = (TextView) itemView.findViewById(R.id.tvTeamsName);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_teams,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TeamVO teamVO = teamVOList.get(position);
            byte[] b = Base64.decode(teamVO.getTeam_logo_base64(),0);
            holder.ivTeamsLogo.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
            holder.tvTeamsName.setText(teamVO.getTeam_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberActivity.this,TestActivity.class);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return teamVOList.size();
        }
    }

    public void onTakePictureClick(View view){
        takePicture();
    }

    private void takePicture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        i.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        if (isIntentAvailable(this,i)) {
            startActivityForResult(i,REQ_TAKE_PICTURE);
        } else {

        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = Util.getImageScale(file.getPath(),100,100);
                    Bitmap pic = BitmapFactory.decodeFile(file.getPath(), opt);
                    //上傳
                    if (pic == null) return;
                    byte[] image = Util.bitmapToPNG(pic);
                    membershipVO.setMem_pic(image);
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    if (networkConnected()) {
                        try {
                            boolean result = new UploadPicTask().execute(membershipVO).get();
                            //the result from server
                            if (result) {
                                Toast.makeText(this,"上傳成功 ^____^",Toast.LENGTH_SHORT).show();
                                preferences.edit().remove("meberData").apply();
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                                preferences.edit().putString("meberData",gson.toJson(membershipVO)).apply();
                                ivMemPic.setImageBitmap(pic);
                            } else {
                                Toast.makeText(this,"上傳失敗 Q____Q",Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                    break;
                case REQUEST_LOGIN:
                    SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                    boolean login = preferences.getBoolean("login", false);
                    if (login) {
                        return;
                    } else {
                        onLogin();
                    }
            }
        } else {

        }
    }

    private final static int REQ_PERMISSIONS = 0;

    private void askPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        int result = 0;
        for (int i = 0; i < permissions.length; i++) {
            result = ContextCompat.checkSelfPermission(this,permissions[i]);
        }

        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,permissions,REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += getString(R.string.text_NotGranted);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void onLogin() {
        Intent loginIntent = new Intent(this, Login2Activity.class);
        startActivityForResult(loginIntent, REQUEST_LOGIN);
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
