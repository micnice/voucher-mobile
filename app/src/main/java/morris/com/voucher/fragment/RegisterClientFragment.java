package morris.com.voucher.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import morris.com.voucher.R;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;

import static morris.com.voucher.util.Tools.hasPermissions;

/**
 * Created by morris on 2018/12/17.
 */

public class RegisterClientFragment extends BaseFragment  implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    FragmentActivity fragmentActivity;
    JSONObject identificationData = new JSONObject();
    Toolbar toolbar;
    Button saveData;
    BroadcastReceiver lastKnownLocationReceiver;
    EditText lmp,firstName,lastName,maritalStatus,birthDate,parity,
    identificationNumber,latitude,longitude;
    Context context;
    Activity activity;
    LocationSettings locationSettings;

    public RegisterClientFragment() {
    }
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){

      View  view = inflater.inflate(R.layout.fragment_register_client, container, false);
        context = getContext();
        activity = getActivity();
        locationSettings = new LocationSettings(getActivity().getApplicationContext());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
            getLastKnownLocation();

        lmp = view.findViewById(R.id.lmp);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        maritalStatus = view.findViewById(R.id.maritalStatus);
        birthDate = view.findViewById(R.id.birthDate);
        parity = view.findViewById(R.id.parity);
        identificationNumber = view.findViewById(R.id.identificationNumber);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);


        return view;

    }
    @Override
    public void onClick(View v) {

    }

    public void getLastKnownLocation() {
        Intent i = new Intent(getActivity().getApplicationContext(), LocationTracker.class);
        if (!locationSettings.locationServiceRunning()) {
            getActivity().getApplicationContext().startService(i);
        }


        lastKnownLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                System.out.println("##########TISVIKEWO----");
                try {
                    latitude.setText(String.valueOf(intent.getExtras().get("latitude")));
                    longitude.setText(String.valueOf(intent.getExtras().get("longitude")));
                    System.out.println("##########ZVAITA----+L-"+intent.getExtras().get("latitude"));
                    System.out.println("##########ZVAITA----+LO-"+intent.getExtras().get("longitude"));

                    if (lastKnownLocationReceiver != null) {
                        context.unregisterReceiver(lastKnownLocationReceiver);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        };
        getActivity().getApplicationContext().registerReceiver(lastKnownLocationReceiver, new IntentFilter("last_known_location"));

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void requestPermissions() {
        int PERMISSION_ALL = 1133;
        String[] PERMISSIONS = {
                //android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
              //  android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(getActivity().getApplicationContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }


}
