package idv.allen.gameball.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import idv.allen.gameball.R;
import idv.allen.gameball.fragment.Page;
import idv.allen.gameball.fragment.PitchFragment;
import idv.allen.gameball.fragment.PlateFragment;
import idv.allen.gameball.util.Util;

public class DataRecordActivity extends AppCompatActivity {
    Page page1,page2;
    public PitchFragment pitchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_record);



        Bundle bundle = getIntent().getExtras();
        //建立ws連線
        Util.connectServer(this, bundle.getString("gameday_id"));

        pitchFragment = new PitchFragment();
        pitchFragment.setData(bundle);

        PlateFragment plateFragment = new PlateFragment();
        plateFragment.setData(bundle);

        page1 = new Page(pitchFragment,"投球內容");
        page2 = new Page(plateFragment,"打席結果");

        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create viewpager
        ViewPager vpDataRecord =(ViewPager) findViewById(R.id.vpDataRecord);
        vpDataRecord.setAdapter(new DataRecordAdapter(getSupportFragmentManager()));

        //create tablayout
        TabLayout tabDataRecord = (TabLayout) findViewById(R.id.tabDataRecord);
        tabDataRecord.setupWithViewPager(vpDataRecord);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.disconnectServer();
    }

    private class DataRecordAdapter extends FragmentPagerAdapter {
        List<Page> pageList;

        public DataRecordAdapter (FragmentManager fragmentManager) {
            super(fragmentManager);
            pageList = new ArrayList<>();
            pageList.add(page1);
            pageList.add(page2);
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
}
