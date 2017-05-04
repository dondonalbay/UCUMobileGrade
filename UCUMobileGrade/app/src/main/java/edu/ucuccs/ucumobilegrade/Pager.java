package edu.ucuccs.ucumobilegrade;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Admin on 6/20/2016.
 */
public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BlockRankingFragment blockTab = new BlockRankingFragment();
                return blockTab;
            case 1:
                OverallRankingFragment overallTab = new OverallRankingFragment();
                return overallTab;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
