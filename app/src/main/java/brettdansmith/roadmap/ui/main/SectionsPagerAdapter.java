package brettdansmith.roadmap.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import brettdansmith.roadmap.MapsFragment;
import brettdansmith.roadmap.OverviewFragment;
import brettdansmith.roadmap.R;
import brettdansmith.roadmap.SettingsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_settings, R.string.tab_overview, R.string.tab_navigation};
    private SettingsFragment settingsFragment;
    private OverviewFragment overviewFragment;
    private MapsFragment mapsFragment;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    return settingsFragment;
                }else{
                    return settingsFragment;
                }
            case 1:
                if (overviewFragment == null) {
                    overviewFragment = new OverviewFragment();
                    return overviewFragment;
                }else{
                    return overviewFragment;
                }
            case 2:
                if (mapsFragment == null) {
                    mapsFragment = new MapsFragment();
                    return mapsFragment;
                }else{
                    return mapsFragment;
                }
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}