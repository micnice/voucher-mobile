package morris.com.voucher.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.fragment.FormsByUserFragment;
import morris.com.voucher.fragment.RegisterClientFragment;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.IdentificationData;

import static morris.com.voucher.util.Tools.hasPermissions;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TabLayout dashboardTabs;
    ActionBar actionBar;
    public static Context context;
    public static Activity activity;
    public static DashboardActivity dashboardActivity;
    public static boolean gpsOn = false;
    private Intent locationIntent;
    static LocationManager locationManager;
    static LocationListener locationListener;
    LocationManager manager = null;
    LocationSettings locationSettings = new LocationSettings(this);
    public VoucherDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        locationIntent = new Intent(getApplicationContext(), LocationTracker.class);

        checkGPS();

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
                Bundle bundle = new Bundle();
                int stab = tab.getPosition();

                if (stab == 0) {
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, registerClient).commit();
                    return;
                } else if (stab == 1) {
                    bundle.putString("item", "assessment");
                    FormsByUserFragment formsByUserFragment = new FormsByUserFragment();
                    formsByUserFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();

                }else {

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
    protected void onDestroy() {
        super.onDestroy();
        if (locationSettings.locationServiceRunning()) {
            stopService(locationIntent);
        }

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

    public void checkGPS() {
        int PERMISSION_ALL = 1122;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(DashboardActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        //Check if Gps is On
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                new AlertDialog.Builder(this).setTitle("GPS Message").setMessage("Data Entry Requires Turning On GPS " +
                        "Are you going to do Data Entry?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 12344);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else {
                gpsOn= true;
                startService(locationIntent);

            }
        } else {
            Toast.makeText(context, "Your phone seems not to have gps location.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12344) {
            try {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gpsOn = false;
                } else {
                    gpsOn = true;
                    startService(locationIntent);
                }

            } catch (Exception ex) {
                gpsOn = false;
            }
        }
    }
    @SuppressLint("MissingPermission")
    public static void configureLocationManager() {
        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
    }

}