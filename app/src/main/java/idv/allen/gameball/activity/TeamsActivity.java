package idv.allen.gameball.activity;

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

import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;

public class TeamsActivity extends AppCompatActivity {
    private RecyclerView rvTeamList;

    class GetTeamsTask extends AsyncTask<String,Void,List<TeamVO>>{
        @Override
        protected List<TeamVO> doInBackground(String... params) {
            TeamDAO_interface dao = new TeamDAO();
            return dao.getAll();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
        rvTeamList = (RecyclerView) findViewById(R.id.rvTeamList);
        if (networkConnected()) {
            GetTeamsTask task = new GetTeamsTask();
            try {
                List<TeamVO> teamVOs = task.execute().get();
                showTeamRecyclerView(teamVOs);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {

        }
    }

    public void showTeamRecyclerView (List<TeamVO> teamVOList) {
        rvTeamList.setHasFixedSize(true);
        rvTeamList.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rvTeamList.setAdapter(new TeamsAdapter(teamVOList));
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
                    Intent i = new Intent(TeamsActivity.this,TestActivity.class);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return teamVOList.size();
        }
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
