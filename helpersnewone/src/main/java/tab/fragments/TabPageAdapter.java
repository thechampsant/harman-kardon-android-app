package tab.fragments;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import linq.ArrayList;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;*/
import app.core.base.IFragment;

public class TabPageAdapter extends FragmentPagerAdapter {
    private ArrayList<IFragment> fragments;

    public TabPageAdapter(FragmentManager fm, ArrayList<IFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

   

	@Override
    public Fragment getItem(int position) {
        return  this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
