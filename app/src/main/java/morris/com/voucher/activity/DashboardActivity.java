package morris.com.voucher.activity;

import android.app.Activity;
import android.content.Context;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.fragment.RegisterClientFragment;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TabLayout dashboardTabs;
    ActionBar actionBar;
    public static Context context;
    public static Activity activity;
    public static DashboardActivity dashboardActivity;
    public VoucherDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

     Toolbar toolbar =  findViewById(R.id.toolbar);
       toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        this.actionBar = this.getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dashboardTabs =  findViewById(R.id.dashboard_tabs);


        dashboardTabs.addTab(dashboardTabs.newTab().setText("Data Entry"));
        dashboardTabs.addTab(dashboardTabs.newTab().setText("Assessment"));
        dashboardTabs.addTab(dashboardTabs.newTab().setText("Accounting"));

        context = DashboardActivity.this;
        activity = DashboardActivity.this;
        dashboardActivity = this;
        database = VoucherDataBase.getDatabase(this);



        dashboardTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            public void onTabSelected(TabLayout.Tab tab) {


                Fragment registerClient = new RegisterClientFragment();
                FragmentManager fManager = getSupportFragmentManager();
                int stab = tab.getPosition();

                if (stab == 0) {
                    for (int i = 0; i < fManager.getBackStackEntryCount(); i++) {
                        fManager.popBackStack();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, registerClient).commit();
                    return;
                }

               }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
    }

    void openFragment(Fragment fragment) {

        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_dashboard, fragment);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.replace(R.id.content_dashboard, fragment);
            ft.commit();
        }
    }

    public void setItemsFragment(Fragment fragment1, Fragment fragment2, Fragment fragment3){
        if(fragment1 != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, fragment1).commit();

    }


}