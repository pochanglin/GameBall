package idv.allen.gameball.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.tournment.TournamentDAO;
import idv.allen.gameball.tournment.TournamentDAO_interface;
import idv.allen.gameball.tournment.TournamentVO;

public class TournamentActivity extends AppCompatActivity {
    private RecyclerView rvTournament;
    private TextView tvResult;

    private class GetTournTask extends AsyncTask<String,Void,List<TournamentVO>> {
        @Override
        protected List<TournamentVO> doInBackground(String... params) {
            TournamentDAO_interface dao = new TournamentDAO();
            return dao.getAll();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        rvTournament = (RecyclerView) findViewById(R.id.rvTournament);
        tvResult = (TextView) findViewById(R.id.tvResult);
        //送到伺服端查尋資料
        if (networkConnected()) {
            GetTournTask task = new GetTournTask();
            List<TournamentVO> tournamentVOList = null;
            try {
                tournamentVOList = task.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            showTournRecyclerView(tournamentVOList);
        } else {
            //沒連上網路
            tvResult.setText("請重刷!!!!");
        }
        setTitle("比賽專區");

    }
    public void showTournRecyclerView (List<TournamentVO> tournamentList) {
        rvTournament.setHasFixedSize(true);
        rvTournament.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rvTournament.setAdapter(new TournAdapter(tournamentList));
    }

    private class TournAdapter extends RecyclerView.Adapter<TournAdapter.ViewHolder>{
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tournament_client,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TournAdapter.ViewHolder holder, int position) {
            final TournamentVO tournamentVO = tournamentVOList.get(position);
            holder.tvTournName.setText(tournamentVO.getTourn_name());
            holder.tvTournStart.setText("開始日期："+tournamentVO.getTourn_start());
            holder.tvTournEnd.setText("結束日期："+tournamentVO.getTourn_end());
            try {
                TournamentVO tournamentVO1 = new AsyncTask<String, Void, TournamentVO>() {
                    private ProgressDialog progressDialog;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(TournamentActivity.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }

                    @Override
                    protected TournamentVO doInBackground(String... params) {
                        TournamentDAO_interface dao = new TournamentDAO();
                        return dao.getTournPoster(params[0]);
                    }

                    @Override
                    protected void onPostExecute(TournamentVO tournamentVO) {
                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    }
                }.execute(tournamentVO.getTourn_id()).get();

                byte[] b = Base64.decode(tournamentVO1.getTourn_poster_base64(),0);
                holder.ivTournPic.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String tournamentVOJSON = gson.toJson(tournamentVO);
                    bundle.putString("tournamentVOJSON",tournamentVOJSON);
                    bundle.putString("tourn_id",tournamentVO.getTourn_id());
                    bundle.putString("tourn_name",tournamentVO.getTourn_name());
                    Intent i = new Intent(TournamentActivity.this,TournInfoActivity.class);
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
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
