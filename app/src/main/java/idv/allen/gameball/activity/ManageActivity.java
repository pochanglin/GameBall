package idv.allen.gameball.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.tournment.TournamentDAO;
import idv.allen.gameball.tournment.TournamentDAO_interface;
import idv.allen.gameball.tournment.TournamentVO;
import idv.allen.gameball.util.Util;

public class ManageActivity extends AppCompatActivity {
    private static final int REQUEST_LOGIN = 1;
    private final static String TAG = "ManageActivity";
    private AsyncTask retrieveTournTask;
    private RecyclerView rvTournament;
    private TextView tvResult;
    private TournamentDAO_interface dao;


    class RetrieveTournTask extends AsyncTask<String,Void,List<TournamentVO>> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ManageActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected List<TournamentVO> doInBackground(String... params) {
            String mem_id = params[0];
            dao = new TournamentDAO();
            return dao.getAllByMember(mem_id);
        }

        @Override
        protected void onPostExecute(List<TournamentVO> tournamentVOs) {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (tournamentVOs == null || tournamentVOs.isEmpty()) {
                tvResult.setText("你沒辦比賽");
            } else {
                tvResult.setText("");
                showTournRecyclerView(tournamentVOs);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onLogin();

        setContentView(R.layout.activity_manage);
        setTitle("賽事管理");
        rvTournament = (RecyclerView) findViewById(R.id.rvTournament);
        tvResult = (TextView) findViewById(R.id.tvResult);

        //從偏好設定檔取出mem_id
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mem_id = pref.getString("mem_id","");
        //送到伺服端查尋資料
        if (networkConnected() || mem_id.equals("")) {
            retrieveTournTask = new RetrieveTournTask().execute(mem_id);
        } else {
            //沒連上網路
            tvResult.setText("請重刷!!!!");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public void showTournRecyclerView (List<TournamentVO> tournamentList) {
        rvTournament.setHasFixedSize(true);
        rvTournament.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rvTournament.setAdapter(new TournAdapter(tournamentList));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (resultCode == RESULT_OK) {
             switch (requestCode) {
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
             finish();
         }
    }

    private class TournAdapter extends RecyclerView.Adapter<TournAdapter.ViewHolder> {
        private List<TournamentVO> tournamentVOList;
        public TournAdapter(List<TournamentVO> tournamentVOList) {
            this.tournamentVOList = tournamentVOList;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivTournPic;
            private TextView tvTournName,tvTournStart,tvTournEnd,tvStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                ivTournPic = (ImageView) itemView.findViewById(R.id.ivTournPic);
                tvTournName = (TextView) itemView.findViewById(R.id.tvTournName);
                tvTournStart = (TextView) itemView.findViewById(R.id.tvTournStart);
                tvTournEnd = (TextView) itemView.findViewById(R.id.tvTournEnd);
                tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            }
        }
        @Override
        public TournAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tournament,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TournAdapter.ViewHolder holder, int position) {
            final TournamentVO tournamentVO = tournamentVOList.get(position);
            Log.d(TAG,tournamentVO.getTourn_id());
            String poster = tournamentVO.getTourn_poster_base64();
            if(poster == null) {
                try {
                    TournamentVO tournamentVO1 = new AsyncTask<String, Void, TournamentVO>() {
                        private ProgressDialog progressDialog;
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            progressDialog = new ProgressDialog(ManageActivity.this);
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                        }

                        @Override
                        protected TournamentVO doInBackground(String... params) {
                            return dao.getTournPoster(params[0]);
                        }

                        @Override
                        protected void onPostExecute(TournamentVO tournamentVO) {
                            if (progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }
                        }
                    }.execute(tournamentVO.getTourn_id()).get();
                    tournamentVO.setTourn_poster_base64(tournamentVO1.getTourn_poster_base64());
                    byte[] b = Base64.decode(tournamentVO1.getTourn_poster_base64(),0);
                    holder.ivTournPic.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                byte[] b = Base64.decode(tournamentVO.getTourn_poster_base64(),0);
                holder.ivTournPic.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
            }
//            byte[] b = Base64.decode(tournamentVO.getTourn_poster_base64(),0);
//            holder.ivTournPic.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));

            holder.tvTournName.setText(tournamentVO.getTourn_name());
            holder.tvTournStart.setText("開始日期："+tournamentVO.getTourn_start());
            holder.tvTournEnd.setText("結束日期："+tournamentVO.getTourn_end());
            holder.tvStatus.setText("狀態："+tournamentVO.getTourn_status());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tourn_id",tournamentVO.getTourn_id());
                    bundle.putString("tourn_name",tournamentVO.getTourn_name());
                    Intent i = new Intent(ManageActivity.this,GamedayManageActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return tournamentVOList.size();
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
