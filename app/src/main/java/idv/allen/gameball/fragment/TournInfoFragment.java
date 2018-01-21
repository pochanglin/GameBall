package idv.allen.gameball.fragment;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.tournment.TournamentDAO;
import idv.allen.gameball.tournment.TournamentDAO_interface;
import idv.allen.gameball.tournment.TournamentVO;

public class TournInfoFragment extends Fragment {
    private Bundle bundle;
    private RecyclerView rvTeamsLogo;
    private TextView tvRegisterStart,tvRegisterOver,tvTournStart,tvTournEnd,tvTournTeamAmt,tvTournSystem,tvTournFee,tvTournName;
    private ImageView ivTournPoster;
    TournamentVO tournamentVO;
    String tourn_poster_base64;

    public TournInfoFragment() {
        super();
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    class GetPartiTeamTask extends AsyncTask<String,Void,List<TeamVO>> {

        @Override
        protected List<TeamVO> doInBackground(String... params) {
            TeamDAO_interface dao = new TeamDAO();
            return dao.getTournAllTeam(params[0]);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourn_info,container,false);
        rvTeamsLogo = (RecyclerView) view.findViewById(R.id.rvTeamsLogo);
        GetPartiTeamTask task = new GetPartiTeamTask();
        try {
            List<TeamVO> teamVOList = task.execute(bundle.getString("tourn_id")).get();
            showTeamLogoRecyclerView(teamVOList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        findViews(view);
        String tournamentVOJSON = bundle.getString("tournamentVOJSON");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        tournamentVO = gson.fromJson(tournamentVOJSON,TournamentVO.class);
        try {
            tourn_poster_base64 = new AsyncTask<String,Void,String>() {

                @Override
                protected String doInBackground(String... params) {
                    TournamentDAO_interface dao = new TournamentDAO();
                    TournamentVO tournamentVO = dao.getTournPoster(params[0]);
                    return tournamentVO.getTourn_poster_base64();
                }
            }.execute(tournamentVO.getTourn_id()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        setViews();
        return view;
    }

    public void findViews(View view){
        ivTournPoster = (ImageView) view.findViewById(R.id.ivTournPoster);
        tvTournName = (TextView) view.findViewById(R.id.tvTournName);
        tvRegisterStart = (TextView) view.findViewById(R.id.tvRegisterStart);
        tvRegisterOver = (TextView) view.findViewById(R.id.tvRegisterOver);
        tvTournStart = (TextView) view.findViewById(R.id.tvTournStart);
        tvTournEnd = (TextView) view.findViewById(R.id.tvTournEnd);
        tvTournTeamAmt = (TextView) view.findViewById(R.id.tvTournTeamAmt);
        tvTournSystem = (TextView) view.findViewById(R.id.tvTournSystem);
        tvTournFee = (TextView) view.findViewById(R.id.tvTournFee);
    }

    public void setViews() {
        byte[] b = Base64.decode(tourn_poster_base64,0);
        ivTournPoster.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
        tvTournName.setText(tournamentVO.getTourn_name());
        tvRegisterStart.setText(String.valueOf(tournamentVO.getTourn_sign_start()));
        tvRegisterOver.setText(String.valueOf(tournamentVO.getTourn_sign_end()));
        tvTournStart.setText(String.valueOf(tournamentVO.getTourn_start()));
        tvTournEnd.setText(String.valueOf(tournamentVO.getTourn_end()));
        tvTournTeamAmt.setText(String.valueOf(tournamentVO.getTourn_team_amt()));
        tvTournSystem.setText(tournamentVO.getTourn_system());
        tvTournFee.setText(String.valueOf(tournamentVO.getTourn_fee()));
    }

    private class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
        private List<TeamVO> teamVOList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivTeamPic;
            private TextView tvTeamName;
            public ViewHolder(View itemView) {
                super(itemView);
                ivTeamPic = (ImageView) itemView.findViewById(R.id.ivTeamPic);
                tvTeamName = (TextView) itemView.findViewById(R.id.tvTeamName);
            }
        }

        public TeamAdapter(List<TeamVO> teamVOList) {
            this.teamVOList = teamVOList;
        }

        @Override
        public TeamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team_round,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TeamAdapter.ViewHolder holder, int position) {
            TeamVO teamVO = teamVOList.get(position);
            holder.tvTeamName.setText(teamVO.getTeam_name());
            byte [] b = Base64.decode(teamVO.getTeam_logo_base64(),0);
            holder.ivTeamPic.setImageBitmap(BitmapFactory.decodeByteArray(b,0,b.length));
        }

        @Override
        public int getItemCount() {
            return teamVOList.size();
        }
    }

    public void showTeamLogoRecyclerView (List<TeamVO> teamVOList) {
        rvTeamsLogo.setHasFixedSize(true);
        rvTeamsLogo.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        TeamAdapter adapter = new TeamAdapter(teamVOList);
        rvTeamsLogo.setAdapter(adapter);
    }
}
