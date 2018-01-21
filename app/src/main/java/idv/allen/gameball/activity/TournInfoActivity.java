package idv.allen.gameball.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import idv.allen.gameball.R;
import idv.allen.gameball.fragment.Page;
import idv.allen.gameball.fragment.TournGamedayFragment;
import idv.allen.gameball.fragment.TournInfoFragment;
import idv.allen.gameball.fragment.TournMapFragment;
import idv.allen.gameball.tournment.TournamentDAO;
import idv.allen.gameball.tournment.TournamentDAO_interface;
import idv.allen.gameball.tournment.TournamentVO;

public class TournInfoActivity extends AppCompatActivity {
    private final static String TAG = "TournInfoActivity";
    private static final int MY_REQUEST_CODE = 1;
    Page page1,page2,page3;

    class GetTournTask extends AsyncTask<String,Void,TournamentVO> {

        @Override
        protected TournamentVO doInBackground(String... params) {
            TournamentDAO_interface dao = new TournamentDAO();
            return dao.getOneTourn(params[0]);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourn_info);
        Bundle bundle = getIntent().getExtras();
//        if (networkConnected()) {
//            GetTournTask task = new GetTournTask();
//            try {
//                TournamentVO tournamentVO = task.execute(bundle.getString("tourn_id")).get();
//                String tournamentVOJSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(tournamentVO);
//                bundle.putString("tournamentVOJSON",tournamentVOJSON);
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }

        TournInfoFragment tournInfoFragment= new TournInfoFragment();
        tournInfoFragment.setData(bundle);
        TournMapFragment tournMapFragment = new TournMapFragment();
        tournMapFragment.setData(bundle);
        TournGamedayFragment tournGamedayFragment = new TournGamedayFragment();
        tournGamedayFragment.setData(bundle);

        page1 = new Page(tournInfoFragment,"賽事資訊");
        page3 = new Page(tournMapFragment,"賽事地點");
        page2 = new Page(tournGamedayFragment,"賽事內容");

        setTitle(bundle.getString("tourn_name"));
        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create viewpager
        ViewPager vpDataRecord =(ViewPager) findViewById(R.id.vpTournInfo);
        vpDataRecord.setAdapter(new TournInfoAdapter(getSupportFragmentManager()));
        //create tablayout
        TabLayout tabDataRecord = (TabLayout) findViewById(R.id.tabTournInfo);
        tabDataRecord.setupWithViewPager(vpDataRecord);
    }

    @Override
    protected void onStart() {
        super.onStart();
        askMapPermissions();
    }

    private class TournInfoAdapter extends FragmentPagerAdapter {
        List<Page> pageList;
        public TournInfoAdapter(FragmentManager fm) {
            super(fm);
            pageList = new ArrayList<>();
            pageList.add(page1);
            pageList.add(page2);
            pageList.add(page3);
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageList.get(position).getTitle();
        }
    }

    public void askMapPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
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

    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
