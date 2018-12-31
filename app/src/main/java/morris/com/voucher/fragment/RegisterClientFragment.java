package morris.com.voucher.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.enums.EducationStatus;
import morris.com.voucher.enums.MaritalStatus;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.IdentificationData;

import static morris.com.voucher.util.Tools.hasPermissions;

/**
 * Created by morris on 2018/12/17.
 */

public class RegisterClientFragment extends BaseFragment  {

    Toolbar toolbar;
    BroadcastReceiver lastKnownLocationReceiver;
    EditText lmp,firstName,lastName,birthDate,parity,
    identificationNumber,latitude,longitude;
    Context context;
    Activity activity;
    Spinner maritalStatus;
    Spinner educationStatus;
    Button setDOB,saveData,lmpDatePicker;
    private Calendar calendar;
    private int year, month, day;
    public VoucherDataBase database;



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
        database = VoucherDataBase.getDatabase(context);
        locationSettings = new LocationSettings(getActivity().getApplicationContext());

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
            getLastKnownLocation();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        toolbar = view.findViewById(R.id.toolbar);
        lmp = view.findViewById(R.id.lmp);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        maritalStatus = view.findViewById(R.id.maritalStatus);
        educationStatus = view.findViewById(R.id.educationStatus);
        birthDate = view.findViewById(R.id.birthDate);
        parity = view.findViewById(R.id.parity);
        identificationNumber = view.findViewById(R.id.identificationNumber);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
        lmpDatePicker = view.findViewById(R.id.lmpPicker);
        setDOB = view.findViewById(R.id.dobPicker);
        saveData = view.findViewById(R.id.saveData);


        maritalStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getMaritalStatuses()));

        educationStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getEducationalStatuses()));


        setDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(999);
            }
        });


        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLocalInstance();
            }
        });

        lmpDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(998);
            }
        });


        return view;

    }
    private DatePickerDialog.OnDateSetListener dobListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDOB(arg1, arg2+1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener lmpListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showLMP(arg1, arg2+1, arg3);
                }
            };
    @SuppressWarnings("deprecation")
    public void setDate(int code) {
        onCreateDialog(code).show();
    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog,
                    dobListener, year, month, day);
        }
        if(id==998){
            return new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog,
                    lmpListener, year, month, day);
        }
        return null;
    }
    private void showDOB(int year, int month, int day) {

            birthDate.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }

    private void showLMP(int year, int month, int day) {

        lmp.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    void saveLocalInstance(){
        IdentificationData identificationData = new IdentificationData();
        identificationData.setBirthDate(birthDate.getText().toString());
        identificationData.setCreatedBy("mbaradza");
        identificationData.setEducationStatus(educationStatus.getSelectedItem().toString());
        identificationData.setFirstName(firstName.getText().toString());
        identificationData.setLastName(lastName.getText().toString());
        identificationData.setMaritalStatus(maritalStatus.getSelectedItem().toString());
        identificationData.setIdentificationNumber(identificationNumber.getText().toString());
        identificationData.setLatitude(latitude.getText().toString());
        identificationData.setLongitude(longitude.getText().toString());

        Date currentDate = Calendar.getInstance().getTime();
        identificationData.setDateRegistered(currentDate.toString());

        database.identificationDataDAO().saveIdentificationData(identificationData);

        List<IdentificationData> data = database.identificationDataDAO().getAll();
        //TODO REMOVE AND GET ID DIRECTLY FROM SERVER
       for(IdentificationData id:data){
           id.setSentToServer(Boolean.TRUE);
           id.setIdFromServer(Integer.toString(id.getId()));
           database.identificationDataDAO().updateIdentificationData(id);
       }
        Bundle bundle = new Bundle();
        Fragment fragment= new FormsByUserFragment();
         fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).commit();

    }


    private String[] getEducationalStatuses(){

        EnumMap<EducationStatus,String> educationStatus = EducationStatus.getEnumMap();

        String[] educationalStatus = new String[educationStatus.size()+1];
        educationalStatus[0] = "Select Education Status";
        for(int i = 0; i<educationStatus.size();i++){

            educationalStatus[i+1] =educationStatus.get(EducationStatus.get(i));



        }

        return  educationalStatus;

    }
    private String[] getMaritalStatuses(){
        EnumMap<MaritalStatus,String> maritalStatuses = MaritalStatus.getEnumMap();

        String[] maritalStatus = new String[maritalStatuses.size()+1];
        maritalStatus[0] = "Select Marital Status";
        for(int i = 0; i<maritalStatuses.size();i++){

            maritalStatus[i+1] =maritalStatuses.get(MaritalStatus.get(i));

        }

        return  maritalStatus;

    }

    public void getLastKnownLocation() {
        Intent i = new Intent(getActivity().getApplicationContext(), LocationTracker.class);
        if (!locationSettings.locationServiceRunning()) {
            getActivity().getApplicationContext().startService(i);
        }


        lastKnownLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    latitude.setText(String.valueOf(intent.getExtras().get("latitude")));
                    longitude.setText(String.valueOf(intent.getExtras().get("longitude")));

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

    public void requestPermissions() {
        int PERMISSION_ALL = 1133;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(getActivity().getApplicationContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
    }


}
