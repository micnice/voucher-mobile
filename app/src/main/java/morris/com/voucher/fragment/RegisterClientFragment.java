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
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.enums.EducationStatus;
import morris.com.voucher.enums.MaritalStatus;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.IdentificationData;
import morris.com.voucher.util.Tools;

import static morris.com.voucher.util.Tools.getDateFromString;
import static morris.com.voucher.util.Tools.hasPermissions;

/**
 * Created by morris on 2018/12/17.
 */

public class RegisterClientFragment extends BaseFragment  {

    Toolbar toolbar;
    BroadcastReceiver lastKnownLocationReceiver;
    EditText lmp,firstName,lastName,birthDate,parity,
    identificationNumber,latitude,longitude;
    Bundle bundle;
    Context context;
    Activity activity;
    Spinner maritalStatus;
    Spinner educationStatus;
    Button setDOB,saveData,lmpDatePicker;
    CheckBox markAsFinalised;
    String age,lmpAge;
    private Calendar calendar;
    private int year, month, day;
    public VoucherDataBase database;
    LocationSettings locationSettings;
    private IdentificationData updateData;

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

        if(bundle== null ||bundle.getString("updateClient")==null) {
            getLastKnownLocation();
        }
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
        markAsFinalised = view.findViewById(R.id.markAsFinalised);

       bundle = getArguments();


        maritalStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getMaritalStatuses()));

        educationStatus.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getEducationalStatuses()));


        if(bundle!= null &&bundle.getString("updateClient")!=null && bundle.getString("updateClient").equals("updateIdData".trim())){
            updateData = database.identificationDataDAO().getByClientId(Integer.parseInt(bundle.getString("idDataId")));

            if(updateData!=null){
                lmp.setText(updateData.getLmp());
                firstName.setText(updateData.getFirstName());
                lastName.setText(updateData.getLastName());
                int mStatusPosition = Integer.parseInt(String.valueOf(MaritalStatus.get(updateData.getMaritalStatus()).getCode()));
                maritalStatus.setSelection(mStatusPosition+1);
                int eduStatusPosition = Integer.parseInt(String.valueOf(EducationStatus.get(updateData.getEducationStatus()).getCode()));
                educationStatus.setSelection(eduStatusPosition+1);
                birthDate.setText(updateData.getBirthDate());
                parity.setText(updateData.getParity().toString());
                identificationNumber.setText(updateData.getIdentificationNumber());
                latitude.setText(updateData.getLatitude());
                longitude.setText(updateData.getLongitude());
                markAsFinalised.setChecked(updateData.isMarkAsFinalised());
            }
        }



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
            setAge(Tools.setAge(year,month,day,new Date()));
        }

    private void showLMP(int year, int month, int day) {

        lmp.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        setLmpAge(Tools.setAge(year,month,day,getDateFromString(birthDate.getText().toString())).replace("-","").trim());
    }

    void saveLocalInstance() {
        if (validate()) {
            IdentificationData identificationData = new IdentificationData();
           if( bundle!= null && bundle.getString("updateClient")!=null&&bundle.getString("updateClient").equals("updateIdData")){
               identificationData = updateData;

           }else {
               Date currentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00")).getTime();
               identificationData.setDateRegistered(new SimpleDateFormat("d/M/yyyy").format(currentDate));

               identificationData.setCreatedBy("mbaradza");
           }


            identificationData.setBirthDate(birthDate.getText().toString());
            //TODO USE LOGGED IN USER

            identificationData.setEducationStatus(educationStatus.getSelectedItem().toString());
            identificationData.setFirstName(firstName.getText().toString());
            identificationData.setLastName(lastName.getText().toString());
            identificationData.setMaritalStatus(maritalStatus.getSelectedItem().toString());
            identificationData.setIdentificationNumber(identificationNumber.getText().toString());
            identificationData.setLatitude(latitude.getText().toString());
            identificationData.setLongitude(longitude.getText().toString());
            identificationData.setLmp(lmp.getText().toString());
            identificationData.setParity(Long.valueOf(parity.getText().toString()));
            if (markAsFinalised.isChecked()) {
                identificationData.setMarkAsFinalised(Boolean.TRUE);
            }

            if( bundle!= null && bundle.getString("updateClient")!=null && bundle.getString("updateClient").equals("updateIdData")){
                database.identificationDataDAO().updateIdentificationData(identificationData);
            }else {

                database.identificationDataDAO().saveIdentificationData(identificationData);
            }

            Bundle newBundle = new Bundle();
            Fragment fragment = new FormsByUserFragment();
            fragment.setArguments(newBundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment)
                    .addToBackStack(null).commit();

            bundle = new Bundle();

        }
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

    private Boolean validate() {
        Drawable error_indicator;
        int left = 0;
        int top = 0;
        error_indicator = getResources().getDrawable(android.R.drawable.ic_dialog_info);
        int right = error_indicator.getIntrinsicHeight();
        int bottom = error_indicator.getIntrinsicWidth();
        try{
            error_indicator.setBounds(new Rect(left, top, right, bottom));
            if(firstName.getText().toString().trim().isEmpty()){
                firstName.requestFocus();
                firstName.setError("Please Enter First Name",error_indicator);
                return  false;
            }
            if(lastName.getText().toString().trim().isEmpty()){
                lastName.requestFocus();
                lastName.setError("Please Enter Last Name",error_indicator);
                return false;
            }
            if(maritalStatus.getSelectedItem().toString().trim().equals("Select Marital Status".trim())){
                maritalStatus.requestFocus();
                String text ="Please Select Marital Status";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                return false;
            }
            if(educationStatus.getSelectedItem().toString().trim().equals("Select Education Status".trim())){
              educationStatus.requestFocus();
                String text ="Please Select Education Status";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }

            if(identificationNumber.getText().toString().trim().isEmpty()){
                identificationNumber.requestFocus();
                identificationNumber.setError("Please Enter Client ID Number",error_indicator);
                return false;
            }

            if(!identificationNumber.getText().toString().trim().matches(Tools.ZIMBABWE)){
                identificationNumber.requestFocus();
                identificationNumber.setError("Invalid Zimbabwe National ID");
                return false;
            }
            if(birthDate.getText().toString().trim().isEmpty()){
                birthDate.requestFocus();
                birthDate.setError("Please Enter Date Of Birth",error_indicator);
                return false;
            }
            if(Tools.getDateFromString(birthDate.getText().toString()).after(new Date())){
                birthDate.requestFocus();
                birthDate.setError("D.O.B Can not Be A Future Date",error_indicator);
                return false;
            }
             if(Integer.parseInt(age)<13){
                birthDate.requestFocus();
                birthDate.setError("Age Too Low Please Verify",error_indicator);
                return false;
            }

            if(parity.getText().toString().trim().isEmpty()){
                parity.requestFocus();
                parity.setError("Please Enter Parity",error_indicator);
                return false;
            }
            if(lmp.getText().toString().trim().isEmpty()){
                lmp.requestFocus();
                lmp.setError("Please Enter Date Of LMP",error_indicator);
                return false;
            }

            System.out.println("#####-LMPAge--"+lmpAge);
            if(Tools.getDateFromString(lmp.getText().toString()).after(new Date())){
                lmp.requestFocus();
                lmp.setError("LMP Can not Be A Future Date",error_indicator);
                return false;
            }

            if(getDateFromString(lmp.getText().toString()).before(getDateFromString(birthDate.getText().toString()))){
                lmp.requestFocus();
                lmp.setError("LMP Can Not Be Before Birth Date",error_indicator);
                return false;
            }
            if(Integer.parseInt(lmpAge)<13){
                lmp.requestFocus();
                lmp.setError("LMP Too Ridiculous compared To Age Please Verify",error_indicator);
                return false;
            }



            if(longitude.getText().toString().trim().equals("Loading Longitude...".trim())){
                longitude.requestFocus();
                longitude.setError("Turn On Locator To Collect Coordinates",error_indicator);
                return false;
            }


        }catch (Exception ex){
            ex.printStackTrace();

        }



        return true;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLmpAge() {
        return lmpAge;
    }

    public void setLmpAge(String lmpAge) {
        this.lmpAge = lmpAge;
    }


}
