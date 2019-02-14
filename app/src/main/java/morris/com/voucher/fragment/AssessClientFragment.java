package morris.com.voucher.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.TimeZone;

import morris.com.voucher.MutableViewModel.SharedViewModel;
import morris.com.voucher.R;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.enums.PregnancyStatus;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;

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
    private SharedViewModel sharedViewModel;
    LocationManager manager = null;
    private Intent locationIntent;
    ClientAssessment clientAssessment;
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
        DashboardActivity dashBoard = (DashboardActivity)getActivity();
        sharedViewModel = dashBoard.getSharedViewModel();

        bundle =getArguments();


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
        }


        if (bundle==null ||bundle.getString("update") == null) {
            locationIntent = new Intent(getActivity().getApplicationContext(), LocationTracker.class);
            checkGPS();
        }




        locationSettings = new LocationSettings(getActivity().getApplicationContext());




        bundle = getArguments();
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
        part9 = view.findViewById(R.id.part9);
        part10 = view.findViewById(R.id.part10);
        part11 = view.findViewById(R.id.part11);
        clientFirstName = view.findViewById(R.id.clientFirstName);
        clientLastName = view.findViewById(R.id.clientLastName);
        clientIdNumber = view.findViewById(R.id.clientIdNumber);
        saveData = view.findViewById(R.id.saveData);

        pregnancyStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getPregnancyStatuses()));



        if(bundle!=null) {

            if (bundle.getString("update") != null && bundle.getString("update").equals("update")) {
                clientAssessment = database.clientAssessmentDAO().getByClientId(bundle.getString("assClientId"));
                clientLastName.setText(clientAssessment.getLname());
                clientFirstName.setText(clientAssessment.getFname());
                clientIdNumber.setText(clientAssessment.getIdNumber());
                assessment.setId(clientAssessment.getId());
                assessment.setFname(clientFirstName.getText().toString());
                assessment.setLname(clientLastName.getText().toString());
                assessment.setIdNumber(clientIdNumber.getText().toString());
                assessment.setClientId(clientAssessment.getClientId());

                if (clientAssessment.isPart1()) {
                    part1.check(R.id.part1_1);
                } else {
                    part1.check(R.id.part1_0);
                }
                if (clientAssessment.isPart2()) {
                    part2.check(R.id.part2_1);
                } else {
                    part2.check(R.id.part2_0);
                }
                if (clientAssessment.isPart3()) {
                    part3.check(R.id.part3_1);
                } else {
                    part3.check(R.id.part3_0);
                }
                if (clientAssessment.isPart4()) {
                    part4.check(R.id.part4_1);
                } else {
                    part4.check(R.id.part4_0);
                }
                if (clientAssessment.isPart5()) {
                    part5.check(R.id.part5_1);
                } else {
                    part5.check(R.id.part5_0);
                }
                if (clientAssessment.isPart6()) {
                    part6.check(R.id.part6_1);
                } else {
                    part6.check(R.id.part6_0);
                }
                if (clientAssessment.isPart7()) {
                    part7.check(R.id.part7_1);
                } else {
                    part7.check(R.id.part7_0);
                }
                if (clientAssessment.isPart8()) {
                    part8.check(R.id.part8_1);
                } else {
                    part8.check(R.id.part8_0);
                }
                if (clientAssessment.isPart9()) {
                    part9.check(R.id.part9_1);
                } else {
                    part9.check(R.id.part9_0);
                }
                if (clientAssessment.isPart10()) {
                    part10.check(R.id.part10_1);
                } else {
                    part10.check(R.id.part10_0);
                }
                if (clientAssessment.isPart11()) {
                    part11.check(R.id.part11_1);
                } else {
                    part11.check(R.id.part11_0);
                }

                int pregStatusPosition = Integer.parseInt(String.valueOf(PregnancyStatus.get(clientAssessment.getPregnancyStatus()).getCode()));

                 pregnancyStatus.setSelection(pregStatusPosition+1);

                markAsFinalised.setChecked(clientAssessment.isMarkAsFinalised());
                latitude.setText(clientAssessment.getLatitude());
                longitude.setText(clientAssessment.getLongitude());
            } else {
                getLastKnownLocation();
                clientLastName.setText(bundle.getString("lname"));
                clientFirstName.setText(bundle.getString("fname"));
                clientIdNumber.setText(bundle.getString("idNumber"));
            }
        }
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
                                assessment.setPart11(Boolean.TRUE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });


        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {


                    if (bundle==null ||bundle.getString("update") == null || !bundle.getString("update").equals("update")) {

                        Date date = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00")).getTime();
                        assessment.setClientId(bundle.getString("clientId"));
                        assessment.setDateAssesed(new SimpleDateFormat("d/M/yyyy").format(date));
                        assessment.setFname(bundle.getString("fname"));
                        assessment.setLname(bundle.getString("lname"));
                        assessment.setIdNumber(bundle.getString("idNumber"));
                    }
                    if (markAsFinalised.isChecked()) {
                        assessment.setMarkAsFinalised(Boolean.TRUE);
                    }

                    if(sharedViewModel!=null && sharedViewModel.getLoginDetails()!=null && sharedViewModel.getLoginDetails().getValue()!=null){
                        assessment.setAssessedBy(sharedViewModel.getLoginDetails().getValue().getUserName());
                    }
                    assessment.setLatitude(latitude.getText().toString());
                    assessment.setLongitude(longitude.getText().toString());
                    assessment.setPregnancyStatus(pregnancyStatus.getSelectedItem().toString());


                    if (bundle!=null && bundle.getString("update") != null && bundle.getString("update").equals("update")) {
                        database.clientAssessmentDAO().updateClientAssessmentData(assessment);
                    } else {
                        database.clientAssessmentDAO().saveClientAssessmentData(assessment);
                        AssessmentDataFromServer assessmentDataFromServer =
                                database.assessmentDataFromServerDAO().getByIdFromServer(bundle.getString("clientId"));
                        assessmentDataFromServer.setAssessed(Boolean.TRUE);
                        database.assessmentDataFromServerDAO().updateAssessmentDataFromServer(assessmentDataFromServer);
                    }

                    Bundle newbundle = new Bundle();
                    Fragment fragment = new FormsByUserFragment();
                    newbundle.putString("item", "assessment");
                    fragment.setArguments(newbundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

                    bundle = new Bundle();
                }
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
    private Boolean validate() {
        Drawable error_indicator;
        int left = 0;
        int top = 0;
        error_indicator = getResources().getDrawable(android.R.drawable.ic_dialog_info);
        int right = error_indicator.getIntrinsicHeight();
        int bottom = error_indicator.getIntrinsicWidth();
        try {
            error_indicator.setBounds(new Rect(left, top, right, bottom));
            if (pregnancyStatus.getSelectedItem().toString().equals("Select Pregnancy Status".trim())) {
                pregnancyStatus.requestFocus();
                String text ="Please Select Pregnancy Status";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                return false;
            }
            if (latitude.getText().toString().trim().equals("Loading Latitude...".trim())) {
                latitude.requestFocus();
                latitude.setError("Please Check Locator Settings To Load Coordinates", error_indicator);
                return false;
            }
            if (part1.getCheckedRadioButtonId() == -1) {
                part1.requestFocus();
                String text ="Response For Question 1 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                return false;
            }
            if (part2.getCheckedRadioButtonId() == -1) {
                part2.requestFocus();
                String text ="Response For Question 2 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part3.getCheckedRadioButtonId() == -1) {
                part3.requestFocus();
                String text ="Response For Question 3 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part4.getCheckedRadioButtonId() == -1) {
                part4.requestFocus();
                String text ="Response For Question 4 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part5.getCheckedRadioButtonId() == -1) {
                part5.requestFocus();
                String text ="Response For Question 5 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part6.getCheckedRadioButtonId() == -1) {
                part6.requestFocus();
                String text ="Response For Question 6 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part7.getCheckedRadioButtonId() == -1) {
                part7.requestFocus();
                String text ="Response For Question 7 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }

            if (part8.getCheckedRadioButtonId() == -1) {
                part8.requestFocus();
                String text ="Response For Question 8 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part9.getCheckedRadioButtonId() == -1) {
                part9.requestFocus();
                String text ="Response For Question 9 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part10.getCheckedRadioButtonId() == -1) {
                part10.requestFocus();
                String text ="Response For Question 10 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }
            if (part11.getCheckedRadioButtonId() == -1) {
                part11.requestFocus();
                String text ="Response For Question 11 is Required";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }
    public void checkGPS() {
        int PERMISSION_ALL = 1122;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (!hasPermissions(getActivity().getApplicationContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        //Check if Gps is On
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                try {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 12344);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                }
                try {
                    getActivity().startService(locationIntent);
                }catch (Exception ex){
                ex.printStackTrace();
                }

            }
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 12344) {
            try {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                } else {
                    System.out.println("$$$$--I AM HERE");
                   getActivity(). startService(locationIntent);
                }
                System.out.println("&&&===%%%"+locationSettings.locationServiceRunning());

            } catch (Exception ex) {

            }
        }
    }

}
