package morris.com.voucher;

import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class DashActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TabLayout dashboardTabs;
    ActionBar actionBar;

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

        dashboardTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            public void onTabSelected(TabLayout.Tab tab) {

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {
    }


}