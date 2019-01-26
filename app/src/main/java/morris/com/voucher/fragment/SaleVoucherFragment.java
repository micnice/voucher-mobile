package morris.com.voucher.fragment;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Nonnull;

import morris.com.voucher.CreateVoucherSaleMutation;
import morris.com.voucher.IdentificationAssessedAndPassedQuery;
import morris.com.voucher.R;
import morris.com.voucher.VoucherListDataQuery;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.dto.VoucherSetDto;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.AccountingClient;

/**
 * Created by morris on 2018/12/31.
 */

public class SaleVoucherFragment extends BaseFragment {

    Toolbar toolbar;
    TextView clientIdNumber,clientLastName,clientFirstName;
    Button saveData;
    Bundle bundle;
    Context context;
    Spinner voucherSetSpinner;
    VoucherDataBase database;
    ArrayList<VoucherSetDto> voucherSetDtoArrayList = new ArrayList<>();



    public SaleVoucherFragment() {
    }

    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View  view = inflater.inflate(R.layout.fragment_sale_voucher, container, false);
        context = getContext();
        toolbar = view.findViewById(R.id.toolbar);
        bundle = getArguments();
        database = VoucherDataBase.getDatabase(context);
        voucherSetSpinner = view.findViewById(R.id.voucherSets);
        clientFirstName = view.findViewById(R.id.clientFName);
        clientLastName = view.findViewById(R.id.clientLName);
        clientIdNumber = view.findViewById(R.id.clientIdNum);
        saveData = view.findViewById(R.id.saveSaleData);
        getVoucherSets();
        clientLastName.setText(bundle.getString("accountingLName"));
        clientFirstName.setText(bundle.getString("accountingFName"));
        clientIdNumber.setText(bundle.getString("accountingIdNumber"));

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO GET THE CURRENTLY LOGGED IN USER AND USE ON SOLD BY
                if (validate()) {

                    Date currentDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00")).getTime();
                    System.out.println("$$$$$-----" + new SimpleDateFormat("d/M/yyyy").format(currentDate));
                    GraphQL.getApolloClient().mutate(CreateVoucherSaleMutation.builder()
                            .beneficiaryIdentityId(bundle.getString("accountingClientId"))
                            .saleDate(new SimpleDateFormat("d/M/yyyy").format(currentDate))
                            .soldBy("mbaradza")
                            .voucherSet(getVoucherSetFromName(voucherSetSpinner.getSelectedItem().toString()))
                            .build()).enqueue(new ApolloCall.Callback<CreateVoucherSaleMutation.Data>() {

                        @Override
                        public void onResponse(@Nonnull Response<CreateVoucherSaleMutation.Data> response) {
                            AccountingClient client = database.accountingClientDAO().getByClientId(response.data().createSales().beneficiaryIdentityId());
                            if (client != null) {
                                client.setSaleMade(Boolean.TRUE);
                                database.accountingClientDAO().updateAccountingData(client);
                                android.support.v4.app.Fragment fragment = new AccountingFormsFragment();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();
                            }


                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            e.printStackTrace();
                        }
                    });


                }
            }
        });



        return view;

    }

    private void getVoucherSets(){
        GraphQL.getApolloClient().query(VoucherListDataQuery.builder().build()).enqueue(new ApolloCall.Callback<VoucherListDataQuery.Data>() {


            @Override
            public void onResponse(@Nonnull Response<VoucherListDataQuery.Data> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<VoucherListDataQuery.VoucherSetList> responsList =response.data().voucherSetList();
                       String[] responseSet = new String[responsList.size() + 1];
                        responseSet[0] = "Select Voucher Package";
                        voucherSetDtoArrayList.add(0, null);
                        if (!responsList.isEmpty()) {
                            for (int i = 0; i < responsList.size(); i++) {
                                String name = responsList.get(i).name();
                                responseSet[i + 1] = name;
                                voucherSetDtoArrayList.add(new VoucherSetDto(responsList.get(i).id(), responsList.get(i).name()));

                            }
                            voucherSetSpinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , responseSet));
                        }

                    }
                });
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
               e.printStackTrace();
            }


        });

    }

    public String getVoucherSetFromName(String name){

        for (VoucherSetDto voucherSetDto:voucherSetDtoArrayList){
            if( voucherSetDto!=null && voucherSetDto.getName()!=null && voucherSetDto.getName().equals(name)){
                return  voucherSetDto.getId();

            }
        }
      return name;
    }

    private Boolean validate() {
      try{
            if(voucherSetSpinner.getSelectedItem().toString().equals("Select Voucher Package".trim())){
                voucherSetSpinner.requestFocus();
                String text ="Please Select Voucher Package";
                SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
                biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return false;
            }



        }catch (Exception ex){

        }



        return true;
    }



}

