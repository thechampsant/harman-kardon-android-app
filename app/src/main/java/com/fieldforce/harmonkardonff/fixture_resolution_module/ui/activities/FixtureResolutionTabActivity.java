package com.fieldforce.harmonkardonff.fixture_resolution_module.ui.activities;

import androidx.fragment.app.Fragment;

import com.fieldforce.harmonkardonff.R;
import com.fieldforce.harmonkardonff.fixture_resolution_module.ui.fragments.RaiseFixtureComplaintFragment;
import com.fieldforce.harmonkardonff.fixture_resolution_module.ui.fragments.ViewFixtureLogsFragment;

import app.core.utils.Dialog;
import linq.ArrayList;
import tab.fragments.FragmentTabActivity;
import tabmenu.TabItem;

public class FixtureResolutionTabActivity extends FragmentTabActivity {

    @Override
    public ArrayList<TabItem> setTabItems() {
        ArrayList<TabItem> data = new ArrayList<>();
        try {
            data.add(new TabItem().setItem("Raise Complaint", RaiseFixtureComplaintFragment.class));
            data.add(new TabItem().setItem("View Logs", ViewFixtureLogsFragment.class));
        } catch (Exception exception) {
            new Dialog(this).setTitle("Exception")
                    .setMessage(exception.getMessage() + " in FixtureResolutionTabActivity")
                    .show();
        }
        return data;
    }

    @Override
    public Class setTabClass() {
        return FixtureResolutionTabActivity.class;
    }

    @Override
    public int setFragmentLayout() {
        return R.layout.tab_fragment;
    }

    @Override
    public void RegisterTableInfoForLocalDB() {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            refreshViewLogsTab();
        }
    }

    public void showViewLogsAfterSubmit() {
        setTabFinally(1);
        refreshViewLogsTab();
    }

    private void refreshViewLogsTab() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.viewpager + ":1");
        if (fragment instanceof ViewFixtureLogsFragment) {
            ((ViewFixtureLogsFragment) fragment).refreshLogs();
        }
    }
}
