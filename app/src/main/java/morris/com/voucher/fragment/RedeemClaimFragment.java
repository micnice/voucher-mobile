package morris.com.voucher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.CityDataListQuery;
import morris.com.voucher.ProviderListByCityIdQuery;
import morris.com.voucher.R;
import morris.com.voucher.RedeemVoucherClaimMutation;
import morris.com.voucher.adapter.RedeemClaimAdapter;
import morris.com.voucher.adapter.TwentyDollarClaimAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.dto.GenericDto;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.Claim;

/**
 * Created by morris on 2019/1/24.
 */

public class RedeemClaimFragment extends BaseFragment {

    public  static RedeemClaimFragment redeemClaimFragment;
    private RecyclerView recyclerView;
    private RecyclerView twentyRecycler;
    private RecyclerView tenRecycler;
    private LinearLayoutManager layoutManager;
    private List<Claim> dataListMain = new ArrayList<>();
    private List<Claim> dataListTen = new ArrayList<>();
    private List<Claim> dataListTwenty = new ArrayList<>();
    ArrayList<GenericDto> cityList = new ArrayList<>();
    ArrayList<GenericDto> providerList = new ArrayList<>();
    RedeemClaimAdapter redeemClaimAdapter;
    TwentyDollarClaimAdapter twentyDollarClaimAdapter;
    Toolbar toolbar;
    TextView clientFirstName;
    TextView clientLastName;
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
        //Main Recycler View
        recyclerView = view.findViewById(R.id.recycler_single_claim);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        database = VoucherDataBase.getDatabase(context);
        dataListMain = database.claimDAO().getAll();
        List<Claim> trimmedData = new ArrayList<>();
        for(Claim c:dataListMain){
            if(!c.getVoucherTypeName().equals("$20 Token")){
                trimmedData.add(c);
            }
        }
        redeemClaimAdapter = new RedeemClaimAdapter(trimmedData);
        recyclerView.setAdapter(redeemClaimAdapter);

        //Twenty RecyclerView
        twentyRecycler = view.findViewById(R.id.twenty_recycler);
        twentyRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        twentyRecycler.setLayoutManager(layoutManager);
        dataListTwenty = database.claimDAO().getByVoucherName("$20 Token");
        twentyDollarClaimAdapter= new TwentyDollarClaimAdapter(dataListTwenty);
        twentyRecycler.setAdapter(twentyDollarClaimAdapter);


        redeemClaimFragment = this;

        toolbar = view.findViewById(R.id.toolbar);
        database = VoucherDataBase.getDatabase(context);

        bundle =getArguments();


        clientFirstName = view.findViewById(R.id.claimFirstName);
        clientLastName = view.findViewById(R.id.claimLastName);
        citySpinner = view.findViewById(R.id.city);
        providerSpinner = view.findViewById(R.id.provider);
        saveData = view.findViewById(R.id.saveClaim);
        getCityList();


        clientLastName.setText(bundle.getString("sdLastName"));
        clientFirstName.setText(bundle.getString("sdFirstName"));

        Animation anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(2000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        saveData.setAnimation(anim);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Claim> syncDataList = database.claimDAO().getAllRedeemed();

                if(validate(syncDataList)){

                    for(Claim data:syncDataList){

                        GraphQL.getApolloClient().mutate(RedeemVoucherClaimMutation.builder()
                                .serviceProvider(getIdFromName(providerSpinner.getSelectedItem().toString(),providerList))
                                .id(data.getClaimId())
                                .build()).enqueue(new ApolloCall.Callback<RedeemVoucherClaimMutation.Data>() {
                            @Override
                            public void onResponse(@Nonnull Response<RedeemVoucherClaimMutation.Data> response) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(context, "Claim Successfully Redeemed.", Toast.LENGTH_LONG).show();
                                        SaleIdentificationDataFragment saleIdentificationDataFragment = new SaleIdentificationDataFragment();
                                        Bundle newBundle = new Bundle();
                                        saleIdentificationDataFragment.setArguments(newBundle);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, saleIdentificationDataFragment)
                                                .addToBackStack(null).commit();
                                    }
                                });

                            }

                            @Override
                            public void onFailure(@Nonnull ApolloException e) {
                                Toast.makeText(context, "No Connection To Server.", Toast.LENGTH_LONG).show();
                            }
                        });


                    }



                }

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               try {
                   providerList = new ArrayList<>();
                   providerSpinner.setSelection(0);

                String cityId = getIdFromName(citySpinner.getSelectedItem().toString(),cityList);

                   if (cityId != null && cityId != "Select City") {

                       GraphQL.getApolloClient().query(ProviderListByCityIdQuery.builder()
                               .city(cityId)
                               .build()).enqueue(new ApolloCall.Callback<ProviderListByCityIdQuery.Data>() {


                           @Override
                           public void onResponse(@Nonnull Response<ProviderListByCityIdQuery.Data> response) {
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {

                                       List<ProviderListByCityIdQuery.ServiceProviderByCity> responsList =response.data().serviceProviderByCity();
                                       String[] responseSet = new String[responsList.size() + 1];
                                       responseSet[0] = "Select Service Provider";
                                       providerList.add(0, null);
                                       if (!responsList.isEmpty()) {
                                           for (int i = 0; i < responsList.size(); i++) {
                                               String name = responsList.get(i).name();
                                               responseSet[i + 1] = name;
                                               providerList.add(new GenericDto(responsList.get(i).id(), responsList.get(i).name()));

                                           }
                                           providerSpinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , responseSet));
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

               }catch (Exception ex){
                   ex.printStackTrace();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return view;

    }
    private void getCityList(){
        GraphQL.getApolloClient().query(CityDataListQuery.builder().build()).enqueue(new ApolloCall.Callback<CityDataListQuery.Data>() {


            @Override
            public void onResponse(@Nonnull Response<CityDataListQuery.Data> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        List<CityDataListQuery.CityList> responsList =response.data().cityList();
                        String[] responseSet = new String[responsList.size() + 1];
                        responseSet[0] = "Select City";
                        cityList.add(0, null);
                        if (!responsList.isEmpty()) {
                            for (int i = 0; i < responsList.size(); i++) {
                                String name = responsList.get(i).name();
                                responseSet[i + 1] = name;
                                cityList.add(new GenericDto(responsList.get(i).id(), responsList.get(i).name()));

                            }
                            citySpinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_custom_color , responseSet));
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

    public String getIdFromName(String name,List<GenericDto> genericDtoList){

        for (GenericDto dto:genericDtoList){
            if( dto!=null && dto.getName()!=null && dto.getName().equals(name)){
                return  dto.getId();

            }
        }
        return name;
    }

public Boolean validate(List<Claim> claims){

    if(citySpinner.getSelectedItem().toString().equals("Select City".trim())){
        citySpinner.requestFocus();
        String text ="Please Select City";
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return false;
    }
    if(providerSpinner.getSelectedItem().toString().equals("Select Service Provider".trim())){
        providerSpinner.requestFocus();
        String text ="Please Select Service Provider";
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        return false;
    }
    if(claims.isEmpty()){
        String text ="Please Select At Least One Service";
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
        Toast toast = Toast.makeText(getActivity().getApplicationContext(),biggerText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return false;
    }

return  true;
}

}
