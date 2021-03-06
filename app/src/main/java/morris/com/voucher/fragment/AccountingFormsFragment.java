package morris.com.voucher.fragment;

import android.arch.lifecycle.ViewModelProviders;
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

import morris.com.voucher.IdentificationAssessedAndPassedQuery;
import morris.com.voucher.MutableViewModel.SharedViewModel;
import morris.com.voucher.R;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.adapter.AccountingClientsAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.AccountingClient;

/**
 * Created by morris on 2018/12/29.
 */

public class AccountingFormsFragment extends BaseFragment {

   public  static AccountingFormsFragment accountingFormsFragment;
    private RecyclerView recyclerView;
    Button getData;
    ProgressBar waiting;
    private List<AccountingClient> dataList = new ArrayList<>();
    AccountingClientsAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;
    Bundle bundle;

    private int itemSaved;

    public AccountingFormsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounting_forms, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_accounting_clients);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        getData = view.findViewById(R.id.getFormAccountingData);
        waiting = view.findViewById(R.id.accountProgressBar);
        waiting.setVisibility(ProgressBar.GONE);
        DashboardActivity dashBoard = (DashboardActivity)getActivity();

        Bundle currentBundle = new Bundle();
        currentBundle.putString("current","sale");
         dashBoard.setCurrentFragmentBundle(currentBundle);
        database = VoucherDataBase.getDatabase(context);
        dataList = database.accountingClientDAO().getAll();
        adapter = new AccountingClientsAdapter(dataList);



        bundle = getArguments();

        if(bundle!=null &&bundle.getString("searchQuery")!=null) {
            List<AccountingClient> searchList = new ArrayList<>();
            String search = bundle.getString("searchQuery").trim();
            for (AccountingClient data : dataList) {
                if (data.getIdNumber()!=null &&data.getIdNumber().toLowerCase().contains(search.toLowerCase())
                        || data.getLastName()!=null && data.getLastName().toLowerCase().contains(search.toLowerCase())
                        || data.getFirstName()!=null && data.getFirstName().toLowerCase().contains(search.toLowerCase())) {
                    searchList.add(data);
                }
            }
            dataList = searchList;
            adapter = new AccountingClientsAdapter(dataList);

        }

        recyclerView.setAdapter(adapter);

        accountingFormsFragment = this;


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            waiting.setVisibility(ProgressBar.VISIBLE);
                GraphQL.getApolloClient().query(IdentificationAssessedAndPassedQuery.builder().build()).enqueue(new ApolloCall.Callback<IdentificationAssessedAndPassedQuery.Data>() {

                    @Override
                    public void onResponse(@Nonnull Response<IdentificationAssessedAndPassedQuery.Data> response) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<IdentificationAssessedAndPassedQuery.IdentificationAssessedAndPassed> dataList = response.data().identificationAssessedAndPassed();
                                database.accountingClientDAO().deleteAllWithoutSale();
                                if (!dataList.isEmpty()) {

                                    for (IdentificationAssessedAndPassedQuery.IdentificationAssessedAndPassed data : dataList) {
                                        AccountingClient client = new AccountingClient();
                                        client.setClientId(data.id());
                                        client.setFirstName(data.firstName());
                                        client.setLastName(data.lastName());
                                        client.setIdNumber(data.identificationNumber());
                                        client.setPovertyScore(Long.valueOf(data.povertyScore().toString()));
                                        database.accountingClientDAO().saveAccountingClientData(client);
                                        itemSaved = itemSaved + 1;

                                    }

                                    if (itemSaved != 0) {
                                        Bundle bundle = new Bundle();
                                        Toast.makeText(context, itemSaved + " New Forms Updated From Server.", Toast.LENGTH_LONG).show();
                                        AccountingFormsFragment accountingFormsFragment = new AccountingFormsFragment();
                                        accountingFormsFragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, accountingFormsFragment)
                                                .addToBackStack(null).commit();

                                    } else {
                                        Toast.makeText(context, "Phone DataBase Is Up To Date With Server.", Toast.LENGTH_LONG).show();
                                    }

                                } else {
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
