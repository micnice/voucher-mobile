package morris.com.voucher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.CreateBeneficiaryIdentificationMutation;
import morris.com.voucher.IdentificationAssessedAndPassedQuery;
import morris.com.voucher.IdentificationNotAssessedQuery;
import morris.com.voucher.R;
import morris.com.voucher.adapter.AccountingClientsAdapter;
import morris.com.voucher.adapter.AssessmentsByUserAdapter;
import morris.com.voucher.adapter.FormsByUserAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.AccountingClient;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.IdentificationData;
import morris.com.voucher.type.EducationStatus;
import morris.com.voucher.type.MaritalStatus;

/**
 * Created by morris on 2018/12/29.
 */

public class AccountingFormsFragment extends BaseFragment {

   public  static AccountingFormsFragment accountingFormsFragment;
    private RecyclerView recyclerView;
    Button getData;
    private List<AccountingClient> dataList = new ArrayList<>();
    AccountingClientsAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;

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

        database = VoucherDataBase.getDatabase(context);
        dataList = database.accountingClientDAO().getAll();
        adapter = new AccountingClientsAdapter(dataList);

        recyclerView.setAdapter(adapter);

        accountingFormsFragment = this;


        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GraphQL.getApolloClient().query(IdentificationAssessedAndPassedQuery.builder().build()).enqueue(new ApolloCall.Callback<IdentificationAssessedAndPassedQuery.Data>() {

                    @Override
                    public void onResponse(@Nonnull Response<IdentificationAssessedAndPassedQuery.Data> response) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<IdentificationAssessedAndPassedQuery.IdentificationAssessedAndPassed> dataList= response.data().identificationAssessedAndPassed();
                                database.accountingClientDAO().deleteAllWithoutSale();
                                if(!dataList.isEmpty()) {

                                    for (IdentificationAssessedAndPassedQuery.IdentificationAssessedAndPassed data : dataList) {
                                           AccountingClient client = new AccountingClient();
                                           client.setClientId(data.id());
                                           client.setFirstName(data.firstName());
                                           client.setLastName(data.lastName());
                                           client.setIdNumber(data.identificationNumber());
                                           client.setPovertyScore(Long.valueOf(data.povertyScore().toString()));
                                           database.accountingClientDAO().saveAccountingClientData(client);
                                            itemSaved =itemSaved+1;

                                    }

                                    if(itemSaved!=0){
                                        Bundle bundle = new Bundle();
                                        AccountingFormsFragment accountingFormsFragment = new AccountingFormsFragment();
                                        accountingFormsFragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.register_client_holder, accountingFormsFragment)
                                                .addToBackStack(null).commit();

                                    }else {
                                        Toast.makeText(context, "Phone DataBase Is Up To Date With Server.", Toast.LENGTH_LONG).show();
                                    }

                                }
                                else {
                                    Toast.makeText(context, "DataBase Is Empty.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
            }
        });

        return view;


    }



}
