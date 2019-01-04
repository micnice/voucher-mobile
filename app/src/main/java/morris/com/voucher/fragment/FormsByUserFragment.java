package morris.com.voucher.fragment;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.IdentificationNotAssessedQuery;
import morris.com.voucher.R;
import morris.com.voucher.adapter.FormsByUserAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/29.
 */

public class FormsByUserFragment extends BaseFragment {

   public  static FormsByUserFragment formsByUserFragment;
    private RecyclerView recyclerView;
    Button addNew,getData,syncData,myAssessments;
    private List<IdentificationData> dataList = new ArrayList<>();
    private List<AssessmentDataFromServer> serverList = new ArrayList<>();
    FormsByUserAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;
    private TextView statusHeader;
    private int itemSaved;

    public FormsByUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forms_by_user, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_forms_by_user);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        addNew = view.findViewById(R.id.addNew);
        getData = view.findViewById(R.id.getForms);
        syncData = view.findViewById(R.id.syncData);
        statusHeader = view.findViewById(R.id.statusHeader);
        myAssessments = view.findViewById(R.id.myAssessments);


        Bundle bundle;
        database = VoucherDataBase.getDatabase(context);
        bundle = getArguments();
       if(bundle!=null && bundle.getString("item")!=null &&bundle.getString("item").equals("assessment")){
           serverList = database.assessmentDataFromServerDAO().getAllNotAssessed();
           addNew.setVisibility(View.GONE);
           statusHeader.setVisibility(View.GONE);
           syncData.setVisibility(View.GONE);
           adapter = new FormsByUserAdapter(serverList,bundle);

       }else {
           getData.setVisibility(View.GONE);
           myAssessments.setVisibility(View.GONE);
           dataList = database.identificationDataDAO().getAll();
           adapter = new FormsByUserAdapter(dataList);
       }

       if(bundle!= null && bundle.getString("current")!=null && bundle.getString("searchQuery")!=null) {
           List<IdentificationData> searchList = new ArrayList<>();
           List<AssessmentDataFromServer> searchListFromServer = new ArrayList<>();
           String search = bundle.getString("searchQuery").trim();

           if (bundle.getString("current").equals("assessment")) {
               for (AssessmentDataFromServer data : serverList) {
                   if (data.getIdNumber().toLowerCase().contains(search.toLowerCase())
                           || data.getFname().toLowerCase().contains(search.toLowerCase())
                           || data.getLname().toLowerCase().contains(search.toLowerCase())) {
                       searchListFromServer.add(data);
                   }
               }
               serverList = searchListFromServer;
               adapter = new FormsByUserAdapter(serverList, bundle);

           } else {
               for (IdentificationData data : dataList) {
                   if (data.getIdentificationNumber().toLowerCase().contains(search.toLowerCase())
                           || data.getFirstName().toLowerCase().contains(search.toLowerCase())
                           || data.getLastName().toLowerCase().contains(search.toLowerCase())) {
                       searchList.add(data);
                   }
               }
               dataList = searchList;
               adapter = new FormsByUserAdapter(dataList);
           }
       }





        adapter.setBundleFromPreviousPage(bundle);
        recyclerView.setAdapter(adapter);

        formsByUserFragment = this;

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                android.support.v4.app.Fragment fragment= new RegisterClientFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).commit();
            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphQL.getApolloClient().query(IdentificationNotAssessedQuery.builder().build()).enqueue(new ApolloCall.Callback<IdentificationNotAssessedQuery.Data>() {


                    @Override
                    public void onResponse(@Nonnull Response<IdentificationNotAssessedQuery.Data> response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<IdentificationNotAssessedQuery.IdentificationNotAssessed> dataList= response.data().identificationNotAssessed();
                                if(!dataList.isEmpty()) {
                                    for (IdentificationNotAssessedQuery.IdentificationNotAssessed data : dataList) {

                                        if(database.assessmentDataFromServerDAO().getByIdFromServer(data.id().toString())!=null){

                                        }else{
                                            AssessmentDataFromServer assessmentDataFromServer = new AssessmentDataFromServer();
                                            assessmentDataFromServer.setClientId(data.id());
                                            assessmentDataFromServer.setFname(data.firstName());
                                            assessmentDataFromServer.setIdNumber(data.identificationNumber());
                                            assessmentDataFromServer.setLname(data.lastName());
                                            database.assessmentDataFromServerDAO().saveAssessmentDataFromServer(assessmentDataFromServer);
                                            itemSaved =itemSaved+1;
                                            System.out.println("%%%%%%%%"+itemSaved);
                                        }
                                    }
                                    System.out.println("%%%%===OOH WHAT A MISS%%%"+itemSaved);
                                    if(itemSaved!=0){
                                        Bundle bundle = new Bundle();
                                        bundle.putString("item", "assessment");
                                        bundle.putString("current","assessment");

                                        FormsByUserFragment formsByUserFragment = new FormsByUserFragment();
                                        formsByUserFragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.register_client_holder, formsByUserFragment).commit();

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
        syncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO REMEMBER TO SET SENT TO SERVER TRUE AND ID FROM SERVER ON SAVE
            }
        });


        return view;


    }



}
