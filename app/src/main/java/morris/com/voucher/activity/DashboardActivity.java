package morris.com.voucher.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import morris.com.voucher.MutableViewModel.SharedViewModel;
import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.fragment.AccountingFormsFragment;
import morris.com.voucher.fragment.AccountingFragment;
import morris.com.voucher.fragment.AssessmentsByUserFragment;
import morris.com.voucher.fragment.FormsByUserFragment;
import morris.com.voucher.fragment.SaleIdentificationDataFragment;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;

import static morris.com.voucher.util.Tools.hasPermissions;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    TabLayout dashboardTabs;
    ActionBar actionBar;
    public static Context context;
    public static Activity activity;
    public Menu topMenu;
   private SharedViewModel sharedViewModel;
    public static SearchView searchView;
    public static DashboardActivity dashboardActivity;
    public static boolean gpsOn = false;
    private Intent locationIntent;
    public static int numberOfTabs;
    Snackbar snackbar;
    private Bundle currentFragmentBundle;
    LocationManager manager = null;
    LocationSettings locationSettings = new LocationSettings(this);
    public VoucherDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        locationIntent = new Intent(getApplicationContext(), LocationTracker.class);

        checkGPS();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(toolbar);
        this.actionBar = this.getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        dashboardTabs = findViewById(R.id.dashboard_tabs);
        sharedViewModel = LoginActivity.getViewModelToOtherActivities();






        snackbar = Snackbar.make(findViewById(R.id.content_dashboard), "", Snackbar.LENGTH_INDEFINITE);
        context = DashboardActivity.this;
        activity = DashboardActivity.this;
        dashboardActivity = this;
        database = VoucherDataBase.getDatabase(this);

        if((sharedViewModel!=null) &&(sharedViewModel.getLoginDetails()!=null)
                &&(sharedViewModel.getLoginDetails().getValue()!=null)&& !sharedViewModel.getLoginDetails().getValue().getRoles().isEmpty()){

            if(sharedViewModel.getLoginDetails().getValue().getRoles().contains("ADMINISTRATION")
                    || sharedViewModel.getLoginDetails().getValue().getRoles().contains("BENEFICIARY_IDENTIFIER")) {
                if(sharedViewModel.getLoginDetails().getValue().getRoles().contains("ADMINISTRATION")){
                    dashboardTabs.addTab(dashboardTabs.newTab().setText("Data Entry"));
                    dashboardTabs.addTab(dashboardTabs.newTab().setText("Assessment"));
                    dashboardTabs.addTab(dashboardTabs.newTab().setText("Accounting"));
                    numberOfTabs=3;
                }else{
                    dashboardTabs.addTab(dashboardTabs.newTab().setText("Data Entry"));
                    numberOfTabs=0;
                }
                Fragment formsByUserFragment = new FormsByUserFragment();
                Bundle bundle = new Bundle();
                bundle.putString("current", "registration");
                formsByUserFragment.setArguments(bundle);
                setCurrentFragmentBundle(bundle);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
            }else if(sharedViewModel.getLoginDetails().getValue().getRoles().contains("BENEFICIARY_ASSESSOR")){
                dashboardTabs.addTab(dashboardTabs.newTab().setText("Assessment"));
                dashboardTabs.addTab(dashboardTabs.newTab().setText("Accounting"));
                numberOfTabs=2;
                Fragment formsByUserFragment1 = new FormsByUserFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("current", "assessment");
                bundle1.putString("item", "assessment");
                formsByUserFragment1.setArguments(bundle1);
                setCurrentFragmentBundle(bundle1);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment1).commit();

            }else if(sharedViewModel.getLoginDetails().getValue().getRoles().contains("DATA_ENTRY_CLERK")){
                dashboardTabs.addTab(dashboardTabs.newTab().setText("Accounting"));
                numberOfTabs=1;
                Fragment accountingFragment = new AccountingFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();

            }
        }


        dashboardTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            public void onTabSelected(TabLayout.Tab tab) {


                int stab = tab.getPosition();

                if (stab == 0) {

                    if(numberOfTabs==2){
                        Bundle bundle = new Bundle();
                        Fragment formsByUserFragment = new FormsByUserFragment();
                        bundle.putString("item", "assessment");
                        bundle.putString("current","assessment");
                        formsByUserFragment.setArguments(bundle);
                        setCurrentFragmentBundle(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                        return;
                    }else if(numberOfTabs==0 || numberOfTabs ==3) {
                        Bundle bundle = new Bundle();
                        Fragment formsByUserFragment = new FormsByUserFragment();
                        bundle.putString("current", "registration");
                        formsByUserFragment.setArguments(bundle);
                        setCurrentFragmentBundle(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                        return;
                    }
                    else if(numberOfTabs==1){
                        Fragment accountingFragment = new AccountingFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();

                    }

                } else if (stab == 1) {
                    if(numberOfTabs==2){
                        Fragment accountingFragment = new AccountingFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();

                    }else if(numberOfTabs==3) {
                        Bundle bundle = new Bundle();
                        bundle.putString("item", "assessment");
                        bundle.putString("current", "assessment");
                        setCurrentFragmentBundle(bundle);
                        FormsByUserFragment formsByUserFragment = new FormsByUserFragment();
                        formsByUserFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                    }
                } else {
                    Fragment accountingFragment = new AccountingFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int stab = tab.getPosition();

                if (stab == 0) {
                    System.out.println("%%%%%-"+numberOfTabs);
                    if(numberOfTabs==2){
                        Bundle bundle = new Bundle();
                        Fragment formsByUserFragment = new FormsByUserFragment();
                        bundle.putString("item", "assessment");
                        bundle.putString("current","assessment");
                        formsByUserFragment.setArguments(bundle);
                        setCurrentFragmentBundle(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                        return;
                    }else if(numberOfTabs==0 ||numberOfTabs ==3) {
                        Bundle bundle = new Bundle();
                        Fragment formsByUserFragment = new FormsByUserFragment();
                        bundle.putString("current", "registration");
                        formsByUserFragment.setArguments(bundle);
                        setCurrentFragmentBundle(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                        return;
                    }
                    else if(numberOfTabs==1){
                        Fragment accountingFragment = new AccountingFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();

                    }

                } else if (stab == 1) {
                    if(numberOfTabs==2){
                        Fragment accountingFragment = new AccountingFragment();
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();

                    }else if(numberOfTabs==3) {
                        Bundle bundle = new Bundle();
                        bundle.putString("item", "assessment");
                        bundle.putString("current", "assessment");
                        setCurrentFragmentBundle(bundle);
                        FormsByUserFragment formsByUserFragment = new FormsByUserFragment();
                        formsByUserFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();
                    }
                } else {
                    Fragment accountingFragment = new AccountingFragment();
                    getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, accountingFragment).commit();
                }
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

    public void setItemsFragment(Fragment fragment1, Fragment fragment2, Fragment fragment3) {
        if (fragment1 != null)
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
                new AlertDialog.Builder(this).setTitle("GPS Message").setMessage("Data Entry or Assessment Requires Turning On GPS." +
                        "Are you going to do any of the two?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                gpsOn = true;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

            searchView.setIconifiedByDefault(true);

        topMenu = menu;


            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getSearchbarQuery();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;

                }
            });

            return true;
        }


    public String getSearchbarQuery() {
        String query = searchView.getQuery().toString();
        showSearchFragment(query);
        return query;
    }
    public void showSearchFragment(String searchQuery) {

        Bundle bundle = getCurrentFragmentBundle();
        if(bundle!=null &&bundle.getString("current")!=null &&(bundle.getString("current").equals("registration")||bundle.getString("current").equals("assessment"))) {
            FormsByUserFragment searchResultsFragment = new FormsByUserFragment();
            bundle.putString("searchQuery", searchQuery);
            searchResultsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, searchResultsFragment).commit();
        }else if(bundle!=null &&bundle.getString("current")!=null &&bundle.getString("current").equals("claim")){
             SaleIdentificationDataFragment searchResultsFragment = new SaleIdentificationDataFragment();
            bundle.putString("searchQuery", searchQuery);
            searchResultsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, searchResultsFragment).commit();
        }
        else if(bundle!=null &&bundle.getString("current")!=null &&bundle.getString("current").equals("sale")){
            AccountingFormsFragment searchResultsFragment = new AccountingFormsFragment();
            bundle.putString("searchQuery", searchQuery);
            searchResultsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, searchResultsFragment).commit();
        }
        else if(bundle!=null &&bundle.getString("current")!=null &&bundle.getString("current").equals("assess")){
            AssessmentsByUserFragment searchResultsFragment = new AssessmentsByUserFragment();
            bundle.putString("searchQuery", searchQuery);
            searchResultsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, searchResultsFragment).commit();
        }

    }


    public Bundle getCurrentFragmentBundle() {
        return currentFragmentBundle;
    }

    public void setCurrentFragmentBundle(Bundle currentFragmentBundle) {
        this.currentFragmentBundle = currentFragmentBundle;
    }

    public void noNetworkNotice(){

        if(!snackbar.isShown()) {
            snackbar.setText("Server Not Accessible. Check your connection");
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }
    public SharedViewModel getSharedViewModel(){
        return sharedViewModel;
    }


}

