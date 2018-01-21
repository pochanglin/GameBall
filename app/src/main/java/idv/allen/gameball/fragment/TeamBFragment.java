package idv.allen.gameball.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import idv.allen.gameball.R;
import idv.allen.gameball.gameday.GamedayDAO;
import idv.allen.gameball.gameday.GamedayDAO_interface;
import idv.allen.gameball.gameday.GamedayVO;
import idv.allen.gameball.roster.RosterDAO;
import idv.allen.gameball.roster.RosterDAO_interface;
import idv.allen.gameball.roster.RosterVO;

public class TeamBFragment extends Fragment {
    private RecyclerView rvTeamB;
    TeamCheckAdapter teamCheckAdapter;
    List<RosterVO> rosterVOList;
    private Bundle bundle;
    private Button btnAbandonTeamB,btnCheckTeamB;
    private String gameday_id;

    public TeamBFragment() {
    }

    public void setData(Bundle bundle) {
        this.bundle = bundle;
    }

    class GetRosterTask extends AsyncTask<String,Void,List<RosterVO>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<RosterVO> doInBackground(String... params) {
            RosterDAO_interface dao = new RosterDAO();
            return dao.getAll(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(List<RosterVO> rosterVOList) {
            super.onPostExecute(rosterVOList);
        }
    }

    class SendCheckTeamTask extends AsyncTask<GamedayVO,Void,Boolean> {

        @Override
        protected Boolean doInBackground(GamedayVO... params) {
            GamedayDAO_interface dao = new GamedayDAO();
            return dao.updateTeamBCheck(params[0]);
        }
    }

    class SendTeamRosterTask extends AsyncTask<List<RosterVO>,Void,Void> {

        @Override
        protected Void doInBackground(List<RosterVO>... params) {
            RosterDAO_interface dao = new RosterDAO();
            dao.chagneRoster(params[0]);
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_b,container,false);

        rvTeamB = (RecyclerView) view.findViewById(R.id.rvTeamB);
        btnAbandonTeamB = (Button) view.findViewById(R.id.btnAbandonTeamB);
        btnCheckTeamB = (Button) view.findViewById(R.id.btnCheckTeamB);
        //set onclick listener of two button
        btnCheckTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckTeamBClick(v);
            }
        });

        btnAbandonTeamB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAbandonTeamBClick(v);
            }
        });
        //get data from GamedayManageActivity
        String tourn_id = bundle.getString("Tourn_id");
        Log.d("Tourn_id",tourn_id);
        String team_b_id = bundle.getString("Team_b_id");
        gameday_id = bundle.getString("Gameday_id");
        //AsyncTask get data
        if (networkConnected()) {
            GetRosterTask task = new GetRosterTask();
            try {
                rosterVOList = task.execute(tourn_id,team_b_id).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            showRosterRecyclerView(rosterVOList);
        } else {

        }
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(teamCheckAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvTeamB);

        return view;
    }
    boolean b;
    public void onCheckTeamBClick (View v) {
        SendTeamRosterTask task1 = new SendTeamRosterTask();
        task1.execute(rosterVOList);

        GamedayVO gamedayVO = new GamedayVO();
        gamedayVO.setGameday_id(gameday_id);
        gamedayVO.setTeam_b_check("準時");
        SendCheckTeamTask task = new SendCheckTeamTask();

        try {
            b = task.execute(gamedayVO).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (b) {
//            final Dialog myDialog = new Dialog(getActivity());
//            myDialog.setCancelable(true);
//            myDialog.setContentView(R.layout.dialog_team_check);
//            Window dialogWindow = myDialog.getWindow();
//            dialogWindow.setGravity(Gravity.CENTER);
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.width = 1000;
//            lp.alpha = 1.0f;
//            dialogWindow.setAttributes(lp);
//            Button btnPress = (Button)myDialog.findViewById(R.id.btnPress);
//            myDialog.show();
//            btnPress.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myDialog.cancel();
//                }
//            });
            //Sweetalert
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText("報到成功！");
            pDialog.setContentText(bundle.getString("teamBName")+"報到成功！");
            pDialog.show();
        } else {
            Toast.makeText(getActivity(),"再試一次.....",Toast.LENGTH_SHORT).show();
        }
    }
    public void onAbandonTeamBClick (View v) {
        GamedayVO gamedayVO = new GamedayVO();
        gamedayVO.setGameday_id(gameday_id);
        gamedayVO.setTeam_b_check("棄賽");
        SendCheckTeamTask task = new SendCheckTeamTask();
        try {
            b = task.execute(gamedayVO).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (b) {
//            final Dialog myDialog = new Dialog(getActivity());
//            myDialog.setCancelable(true);
//            myDialog.setContentView(R.layout.dialog_team_abandon);
//            Window dialogWindow = myDialog.getWindow();
//            dialogWindow.setGravity(Gravity.CENTER);
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.width = 1000;
//            lp.alpha = 1.0f;
//            dialogWindow.setAttributes(lp);
//            Button btnPress = (Button)myDialog.findViewById(R.id.btnPress);
//            myDialog.show();
//            btnPress.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    myDialog.cancel();
//                }
//            });
            //sweet alert
            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("隊伍棄賽...");
            pDialog.setContentText(bundle.getString("teamBName")+"放棄比賽...");
            pDialog.show();
        } else {
            Toast.makeText(getActivity(),"再試一次.....",Toast.LENGTH_SHORT).show();
        }
    }

    public void showRosterRecyclerView(List<RosterVO> rosterVOList) {
        rvTeamB.setHasFixedSize(true);
        rvTeamB.setLayoutManager(new LinearLayoutManager(getContext()));
        teamCheckAdapter = new TeamCheckAdapter(getContext(),rosterVOList);
        rvTeamB.setAdapter(teamCheckAdapter);
    }

    private class TeamCheckAdapter extends RecyclerView.Adapter<TeamCheckAdapter.ViewHolder> implements ItemTouchHelperAdapter {
        private Context context;
        private List<RosterVO> rosterVOList;
//        private List<String> dataList = new ArrayList<>();
//        private int[] stick = new  int[dataList.size()];

        public TeamCheckAdapter(Context context,List<RosterVO> rosterVOList) {
            this.context = context;
            this.rosterVOList = rosterVOList;
//            String[] data = context.getResources().getStringArray(R.array.data);
//            dataList.addAll(Arrays.asList(data));
        }
        class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            private TextView tvPlayerName;
            private TextView tvPosition;
            private Spinner spFieldPosition;
            private ImageView ivPlayer;
            private float defaultZ;

            public ViewHolder (View view) {
                super(view);
                tvPlayerName = (TextView) view.findViewById(R.id.tvPlayerName);
                ivPlayer = (ImageView) view.findViewById(R.id.ivPlayer);
                tvPosition = (TextView) view.findViewById(R.id.tvPosition);
                spFieldPosition = (Spinner) view.findViewById(R.id.spFieldPosition);
                defaultZ = itemView.getTranslationZ();
            }

            //實作ItemTouchHelperViewHolder所複寫的方法
            public void onItemSelected() {
                itemView.setTranslationZ(15.0f);
            }

            public void onItemClear() {
                itemView.setTranslationZ(defaultZ);
                teamCheckAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.card_team_check,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final RosterVO rosterVO = rosterVOList.get(position);
            //球員照片
//            GetMemPicTask task = new GetMemPicTask();
//            try {
//                MembershipVO membershipVO = task.execute(rosterVO.getMem_id()).get();
//                Bitmap bitmap = BitmapFactory.decodeByteArray(membershipVO.getMem_pic(),0,membershipVO.getMem_pic().length);
//                holder.ivPlayer.setImageBitmap(bitmap);
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
            if (rosterVO.getMem_pic_Base64() != null) {
                byte[] mem_pic = Base64.decode(rosterVO.getMem_pic_Base64(),0);
                Bitmap bitmap = BitmapFactory.decodeByteArray(mem_pic,0,mem_pic.length);
                holder.ivPlayer.setImageBitmap(bitmap);
            }


            //球員名稱
            holder.tvPlayerName.setText("   "+rosterVO.getMem_name());
            //棒次
            holder.tvPosition.setText(String.valueOf(position+1));
            rosterVO.setBatting_order(String.valueOf(position+1));
            Log.d("Batting_order",rosterVO.getBatting_order());
            //守備位置
            holder.spFieldPosition.setSelection(transferFieldPositionToNum(rosterVO.getField_position()),true);
            holder.spFieldPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String name = rosterVO.getMem_name();
                    Log.d("rosterVO.getMem_name()",rosterVO.getMem_name());
                    //old fieldPosition
                    String oldFieldPosition = rosterVO.getField_position();
                    Log.d("oldFieldPosition",oldFieldPosition);
                    //new fieldPosition
                    String newFieldPosition = transferFieldPositionToStr(position);
                    Log.d("newFieldPosition",newFieldPosition);

                    //判斷若有重複的守備位置，將其改為FREE
//                    for (int i = 0; i < rosterVOList.size(); i++) {
//                        //find the rosterVO in rosterVOList which equals the rosterVO i haven't changed
//                        if (rosterVOList.get(i).getField_position().equals(newFieldPosition)
//                                && !rosterVOList.get(i).getMem_name().equals(name)) {
//                            //set fieldPosition
//                            rosterVOList.get(i).setField_position("FREE");
//                            Log.d("rosterVOList.get(i)",rosterVOList.get(i).getMem_name());
//                            holder.spFieldPosition.setSelection(transferFieldPositionToNum(rosterVOList.get(i).getField_position()),true);
//                            Log.d("setSelection",String.valueOf(transferFieldPositionToNum(rosterVOList.get(i).getField_position())));
//                            break;
//                        }
//                    }

                    //change the data in rostserVO
                    rosterVO.setField_position(newFieldPosition);
                    holder.spFieldPosition.setSelection(transferFieldPositionToNum(rosterVO.getField_position()),true);
                    Log.d("getField_position()",rosterVO.getField_position());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public String transferFieldPositionToStr(int position) {
            switch (position) {
                case 0:
                    return "P";
                case 1:
                    return "1B";
                case 2:
                    return "2B";
                case 3:
                    return "3B";
                case 4:
                    return "SS";
                case 5:
                    return "LF";
                case 6:
                    return "CF";
                case 7:
                    return "RF";
                case 8:
                    return "DH";
                case 9:
                    return "FREE";
                default:
                    return "FREE";
            }
        }

        public int transferFieldPositionToNum(String field_position) {
            switch (field_position) {
                case "P":
                    return 0;
                case "1B":
                    return 1;
                case "2B":
                    return 2;
                case "3B":
                    return 3;
                case "SS":
                    return 4;
                case "LF":
                    return 5;
                case "CF":
                    return 6;
                case "RF":
                    return 7;
                case "DH":
                    return 8;
                case "FREE":
                    return 9;
                default:
                    return 9;
            }
        }

        @Override
        public int getItemCount() {
            return rosterVOList.size();
        }


        //實作ItemTouchHelperAdapter 所複寫的方法
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(rosterVOList,fromPosition,toPosition);
            notifyItemMoved(fromPosition,toPosition);
            return  true;
        }

        public void onItemDismiss(int position) {
            rosterVOList.remove(position);
            notifyItemRemoved(position);
        }
    }

    private class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
        private ItemTouchHelperAdapter adapter;

        public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return super.isLongPressDragEnabled();
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return super.isItemViewSwipeEnabled();
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        //這個方法必須實作
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float width = (float) viewHolder.itemView.getWidth();
                float alpha = 1.0f - Math.abs(dX) / width;
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive);
            }

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof ItemTouchHelperViewHolder) {
                    ItemTouchHelperViewHolder itemViewHolder =
                            (ItemTouchHelperViewHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemClear();
            }
        }
    }

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
