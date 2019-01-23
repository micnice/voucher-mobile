package morris.com.voucher.fragment;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;

import morris.com.voucher.R;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.enums.MaritalStatus;
import morris.com.voucher.enums.PregnancyStatus;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;

import static morris.com.voucher.util.Tools.hasPermissions;

/**
 * Created by morris on 2018/12/31.
 */

public class AssessClientFragment extends BaseFragment {

    Toolbar toolbar;
    RadioGroup part1, part2,part3,part4,part5,part6,part7, part8,part9,part10,part11;
    TextView clientFirstName;
    TextView clientLastName;
    TextView clientIdNumber,longitude,latitude;
    Button saveData;
    Bundle bundle;
    Context context;
    Spinner pregnancyStatus;
    CheckBox markAsFinalised;
    VoucherDataBase database;
    BroadcastReceiver lastKnownLocationReceiver;
    LocationSettings locationSettings;


    public AssessClientFragment() {
    }

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View  view = inflater.inflate(R.layout.fragment_assess_client, container, false);
        context = getContext();
        toolbar = view.findViewById(R.id.toolbar);
        database = VoucherDataBase.getDatabase(context);
        ClientAssessment assessment = new ClientAssessment();
        locationSettings = new LocationSettings(getActivity().getApplicationContext());
        bundle =getArguments();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }
        getLastKnownLocation();

        longitude = view.findViewById(R.id.longitude);
        latitude = view.findViewById(R.id.latitude);
        markAsFinalised = view.findViewById(R.id.markAsFinalised);
        pregnancyStatus = view.findViewById(R.id.pregnancyStatus);
        part1 = view.findViewById(R.id.part1);
        part2 = view.findViewById(R.id.part2);
        part3 = view.findViewById(R.id.part3);
        part4 = view.findViewById(R.id.part4);
        part5 = view.findViewById(R.id.part5);
        part6 = view.findViewById(R.id.part6);
        part7 = view.findViewById(R.id.part7);
        part8 = view.findViewById(R.id.part8);
        part9 = view.findViewById(R.id.part8);
        part10 = view.findViewById(R.id.part10);
        part11 = view.findViewById(R.id.part11);
        clientFirstName = view.findViewById(R.id.clientFirstName);
        clientLastName = view.findViewById(R.id.clientLastName);
        clientIdNumber = view.findViewById(R.id.clientIdNumber);
        saveData = view.findViewById(R.id.saveData);

        pregnancyStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getPregnancyStatuses()));


        part1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part1.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part1_1).getId()){
                     assessment.setPart1(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part2.getCheckedRadioButtonId());
                   if (radioButton.getId()==view.findViewById(R.id.part2_1).getId()){
                        assessment.setPart2(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part3.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part3_1).getId()){
                        assessment.setPart3(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part4.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part4_1).getId()){
                        assessment.setPart4(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part5.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part5_1).getId()){
                        assessment.setPart5(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part6.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part6_1).getId()){
                        assessment.setPart6(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part6.getCheckedRadioButtonId());
                    if (radioButton.getId()==R.id.part6_1){
                        assessment.setPart6(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part7.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part7_1).getId()){
                        assessment.setPart7(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part8.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part8_1).getId()){
                        assessment.setPart8(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part9.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part9_1).getId()){
                        assessment.setPart9(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part10.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part10_1).getId()){
                        assessment.setPart10(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        part11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part11.getCheckedRadioButtonId());
                    if (radioButton.getId()==view.findViewById(R.id.part11_1).getId()){
                        assessment.setPart10(Boolean.TRUE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });
        clientLastName.setText(bundle.getString("lname"));
        clientFirstName.setText(bundle.getString("fname"));
        clientIdNumber.setText(bundle.getString("idNumber"));

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = Calendar.getInstance().getTime();
            assessment.setClientId(bundle.getString("clientId"));
            assessment.setDateAssesed(new SimpleDateFormat("d/M/yyyy").format(date));
            assessment.setFname(bundle.getString("fname"));
            assessment.setLname(bundle.getString("lname"));
            assessment.setPregnancyStatus(pregnancyStatus.getSelectedItem().toString());
            assessment.setLatitude(latitude.getText().toString());
            assessment.setLongitude(longitude.getText().toString());
            if(markAsFinalised.isChecked()){
                assessment.setMarkAsFinalised(Boolean.TRUE);
            }

            database.clientAssessmentDAO().saveClientAssessmentData(assessment);
                AssessmentDataFromServer assessmentDataFromServer =
                        database.assessmentDataFromServerDAO().getByIdFromServer(bundle.getString("clientId"));
                     assessmentDataFromServer.setAssessed(Boolean.TRUE);
                database.assessmentDataFromServerDAO().updateAssessmentDataFromServer(assessmentDataFromServer);

                Bundle bundle = new Bundle();
                Fragment fragment= new FormsByUserFragment();
                bundle.putString("item","assessment");
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

            }
        });



        return view;

    }
    private String[] getPregnancyStatuses(){
        EnumMap<PregnancyStatus,String> pregnancyStatusMap = PregnancyStatus.getEnumMap();

        String[] pregnancyStatus = new String[pregnancyStatusMap.size()+1];
        pregnancyStatus[0] = "Select Pregnancy Status";
        for(int i = 0; i<pregnancyStatusMap.size();i++){

            pregnancyStatus[i+1] =pregnancyStatusMap.get(PregnancyStatus.get(i));

        }

        return  pregnancyStatus;

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
