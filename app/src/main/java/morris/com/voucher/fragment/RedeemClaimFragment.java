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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.TimeZone;

import morris.com.voucher.R;
import morris.com.voucher.adapter.AccountingClientsAdapter;
import morris.com.voucher.adapter.RedeemClaimAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.enums.PregnancyStatus;
import morris.com.voucher.location.LocationSettings;
import morris.com.voucher.location.LocationTracker;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.Claim;
import morris.com.voucher.model.ClientAssessment;

import static morris.com.voucher.util.Tools.hasPermissions;

/**
 * Created by morris on 2019/1/24.
 */

public class RedeemClaimFragment extends BaseFragment {

    public  static RedeemClaimFragment redeemClaimFragment;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private List<Claim> dataList = new ArrayList<>();
    RedeemClaimAdapter adapter;
    Toolbar toolbar;
    TextView clientFirstName;
    TextView clientLastName;
    TextView clientIdNumber;
    Button saveData;
    Bundle bundle;
    Context context;
    Spinner citySpinner,providerSpinner;
    VoucherDataBase database;



    public RedeemClaimFragment() {
    }

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View  view = inflater.inflate(R.layout.fragment_redeem_claim, container, false);
        context = getContext();

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_single_claim);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        database = VoucherDataBase.getDatabase(context);
        dataList = database.claimDAO().getAll();
        adapter = new RedeemClaimAdapter(dataList);

        recyclerView.setAdapter(adapter);


        redeemClaimFragment = this;

        toolbar = view.findViewById(R.id.toolbar);
        database = VoucherDataBase.getDatabase(context);

        bundle =getArguments();


        clientFirstName = view.findViewById(R.id.claimFirstName);
        clientLastName = view.findViewById(R.id.claimLastName);
        clientIdNumber = view.findViewById(R.id.claimIdNumber);
        citySpinner = view.findViewById(R.id.city);
        providerSpinner = view.findViewById(R.id.provider);
        saveData = view.findViewById(R.id.saveClaim);


        citySpinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getPregnancyStatuses()));
        providerSpinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , getPregnancyStatuses()));

        clientLastName.setText(bundle.getString("sdLastName"));
        clientFirstName.setText(bundle.getString("sdFirstName"));
        clientIdNumber.setText(bundle.getString("sdIdNumber"));

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00")).getTime();
                System.out.println("%%%%%%%%==="+date.toInstant().toString());

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



}
