package idv.allen.gameball.fragment;


import android.support.v4.app.Fragment;

/**
 * Created by Java on 2017/12/24.
 */

public class Page {
    private Fragment fragment;
    private String title;

    public Page (Fragment fragment,String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
