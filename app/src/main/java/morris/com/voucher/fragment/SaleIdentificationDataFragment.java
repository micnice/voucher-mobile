package morris.com.voucher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.R;
import morris.com.voucher.VoucherSaleIdentificationListQuery;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.adapter.SaleIdentificationDataAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.SaleIdentificationData;

/**
 * Created by morris on 2019/1/23.
 */

public class SaleIdentificationDataFragment extends BaseFragment {

   public  static SaleIdentificationDataFragment saleIdentificationDataFragment;
    private RecyclerView recyclerView;
    Button getData;
    private List<SaleIdentificationData> dataList = new ArrayList<>();
    SaleIdentificationDataAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;
    Bundle bundle;
    ProgressBar waiting;

    private int itemSaved;

    public SaleIdentificationDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_identification_data, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_saledata_clients);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        getData = view.findViewById(R.id.getSaleIdentificationData);
        waiting = view.findViewById(R.id.saleProgressBar);
        waiting.setVisibility(ProgressBar.GONE);
        DashboardActivity dashBoard = (DashboardActivity)getActivity();
        Bundle currentBundle = new Bundle();
        currentBundle.putString("current","claim");
        dashBoard.setCurrentFragmentBundle(currentBundle);

        bundle = getArguments();
        database = VoucherDataBase.getDatabase(context);
        dataList = database.saleIdentificationDataDAO().getAll();
        adapter = new SaleIdentificationDataAdapter(dataList);


        if(bundle!=null &&bundle.getString("searchQuery")!=null) {
            List<SaleIdentificationData> searchList = new ArrayList<>();
            String search = bundle.getString("searchQuery").trim();
            for (SaleIdentificationData data : dataList) {
                if (data.getIdNumber()!=null &&data.getIdNumber().toLowerCase().contains(search.toLowerCase())
                        || data.getFirstName()!=null && data.getFirstName().toLowerCase().contains(search.toLowerCase())
                        || data.getLastName()!=null &&data.getLastName().toLowerCase().contains(search.toLowerCase())) {
                    searchList.add(data);
                }
            }
            dataList = searchList;
            adapter = new SaleIdentificationDataAdapter(dataList);

        }


        recyclerView.setAdapter(adapter);

        saleIdentificationDataFragment = this;


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              waiting.setVisibility(ProgressBar.VISIBLE);
                GraphQL.getApolloClient().query(VoucherSaleIdentificationListQuery.builder().build()).enqueue(new ApolloCall.Callback<VoucherSaleIdentificationListQuery.Data>() {

                    @Override
                    public void onResponse(@Nonnull Response<VoucherSaleIdentificationListQuery.Data> response) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<VoucherSaleIdentificationListQuery.VoucherSaleIdentificationDataList> dataList= response.data().voucherSaleIdentificationDataList();

                                if(!dataList.isEmpty()) {

                                    for (VoucherSaleIdentificationListQuery.VoucherSaleIdentificationDataList data : dataList) {


                                        if(database.saleIdentificationDataDAO().getSaleId(data.saleId())!=null){

                                        }else {

                                            SaleIdentificationData client = new SaleIdentificationData();
                                            client.setClientId(data.beneficiaryIdentityId());
                                            client.setFirstName(data.firstName());
                                            client.setLastName(data.lastName());
                                            client.setIdNumber(data.identificationNumber());
                                            client.setSerialNumber(data.voucherSerialNumber());
                                            client.setPackageName(data.packageName());
                                            client.setSaleId(data.saleId());
                                            database.saleIdentificationDataDAO().saveSaleIdentificationData(client);
                                            itemSaved = itemSaved + 1;
                                        }

                                    }
                                    Bundle bundle = new Bundle();
                                    SaleIdentificationDataFragment saleIdentificationDataFragment = new SaleIdentificationDataFragment();
                                    saleIdentificationDataFragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, saleIdentificationDataFragment)
                                            .addToBackStack(null).commit();

                                    if(itemSaved!=0){

                                        Toast.makeText(context, itemSaved+" New Forms Updated From Server.", Toast.LENGTH_LONG).show();


                                    }else {
                                        Toast.makeText(context, "Phone DataBase Is Up To Date With Server.", Toast.LENGTH_LONG).show();
                                    }

                                }
                                else {
                                    Toast.makeText(context, "No New Forms From Server.", Toast.LENGTH_LONG).show();
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        waiting.setVisibility(ProgressBar.GONE);
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        DashboardActivity dashBoard = (DashboardActivity)getActivity();
                        dashBoard.noNetworkNotice();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waiting.setVisibility(ProgressBar.GONE);
                            }
                        });

                    }
                });

            }
        });

        return view;


    }


}
