package morris.com.voucher.fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/31.
 */

public class AssessClientFragment extends BaseFragment {

    Toolbar toolbar;
    RadioGroup part1, part2,part3,part4,part5,part6,part7, part8,part9,part10,part11;
    TextView clientFirstName;
    TextView clientLastName;
    TextView clientIdNumber;
    Button saveData;
    Bundle bundle;
    Context context;
    VoucherDataBase database;


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
        bundle =getArguments();

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


        part1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    RadioButton radioButton = view.findViewById(part1.getCheckedRadioButtonId());
                    if (radioButton.getId()==R.id.part1_1){
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
                    if (radioButton.getId()==R.id.part2_1){
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
                    if (radioButton.getId()==R.id.part3_1){
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
                    if (radioButton.getId()==R.id.part4_1){
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
                    if (radioButton.getId()==R.id.part5_1){
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
                    if (radioButton.getId()==R.id.part7_1){
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
                    if (radioButton.getId()==R.id.part8_1){
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
                    if (radioButton.getId()==R.id.part9_1){
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
                    if (radioButton.getId()==R.id.part10_1){
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
                    if (radioButton.getId()==R.id.part11_1){
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
            assessment.setDateAssesed(date.toString());
            assessment.setFname(bundle.getString("fname"));
            assessment.setLname(bundle.getString("lname"));
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
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).commit();

            }
        });



        return view;

    }

}
