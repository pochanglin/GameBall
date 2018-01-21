package idv.allen.gameball.fragment;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

import idv.allen.gameball.R;
import idv.allen.gameball.activity.GamedayLiveActivity;
import idv.allen.gameball.gameday.GamedayDAO;
import idv.allen.gameball.gameday.GamedayDAO_interface;
import idv.allen.gameball.gameday.GamedayVO;
import idv.allen.gameball.team.TeamDAO;
import idv.allen.gameball.team.TeamDAO_interface;
import idv.allen.gameball.team.TeamVO;
import idv.allen.gameball.tournment.TournamentVO;

public class TournGamedayFragment extends Fragment {
    private RecyclerView rvGameDay;
    private Bundle bundle;
    private TournamentVO tournamentVO;

    class GetGamdayTask extends AsyncTask<String,Void,List<GamedayVO>>{

        @Override
        protected List<GamedayVO> doInBackground(String... params) {
            String tourn_id = params[0];
            GamedayDAO_interface dao = new GamedayDAO();
            return dao.tournGetAll(tourn_id);
        }
    }

    class GetTeamTask extends AsyncTask<String,Void,TeamVO> {

        @Override
        protected TeamVO doInBackground(String... params) {
            TeamDAO_interface dao = new TeamDAO();
            return dao.findByPK(params[0]);
        }
    }

    public TournGamedayFragment() {
        super();
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourn_gameday,container,false);
        String tournamentVOJSON = bundle.getString("tournamentVOJSON");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        tournamentVO = gson.fromJson(tournamentVOJSON,TournamentVO.class);
        rvGameDay = (RecyclerView) view.findViewById(R.id.rvGameDay);
        GetGamdayTask task = new GetGamdayTask();
        try {
            List<GamedayVO> gamedayVOs = task.execute(tournamentVO.getTourn_id()).get();
            showTournRecyclerView(gamedayVOs);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void showTournRecyclerView (List<GamedayVO> gamedayVOs) {
        rvGameDay.setHasFixedSize(true);
        rvGameDay.setLayoutManager(
                new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        GamedayAdapter adapter = new GamedayAdapter(gamedayVOs);
        rvGameDay.setAdapter(adapter);
    }

    private class GamedayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<GamedayVO> gamedayList;
        public GamedayAdapter(List<GamedayVO> gamedayList) {
            this.gamedayList = gamedayList;
        }

        //尚未開始的比賽
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTeamAName,tvTeamBName,tvGamedayTime,tvGamedayDate,tvGamedayName;
            private ImageView ivTeamALogo,ivTeamBLogo;
            private LinearLayout manageBtnLayout;
            private Button btnGamedayLive;
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
                btnGamedayLive = (Button) view.findViewById(R.id.btnGamedayLive);
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
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_gameday_info,parent,false);
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
                        String gamedayDate = gamedayVO.getGameday_time().toString();
                        viewHolder.tvGamedayDate.setText(gamedayDate.substring(0,10));
                        //賽事時間
                        viewHolder.tvGamedayTime.setText(gamedayDate.substring(10,16));
                        //前往文字轉播頁
                        viewHolder.btnGamedayLive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(),GamedayLiveActivity.class);
                                String gamedayVOJSON = new Gson().toJson(gamedayVO);
                                Bundle bundle = new Bundle();
                                bundle.putString("gameday_id",gamedayVO.getGameday_id());
                                bundle.putString("tourn_id",gamedayVO.getTourn_id());
                                bundle.putString("team_a_id",gamedayVO.getTeam_a_id());
                                bundle.putString("team_b_id",gamedayVO.getTeam_b_id());
                                bundle.putString("gamedayVOJSON",gamedayVOJSON);
                                i.putExtras(bundle);
                                startActivity(i);
                            }
                        });
                        break;
                }
            }

        }

        @Override
        public int getItemCount() {
            return gamedayList.size();
        }
    }
}
