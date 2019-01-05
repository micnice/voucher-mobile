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

import morris.com.voucher.IdentificationNotAssessedQuery;
import morris.com.voucher.R;
import morris.com.voucher.adapter.AssessmentsByUserAdapter;
import morris.com.voucher.adapter.FormsByUserAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/29.
 */

public class AssessmentsByUserFragment extends BaseFragment {

   public  static AssessmentsByUserFragment assessmentsByUserFragment;
    private RecyclerView recyclerView;
    Button syncData;
    Bundle bundle;
    private List<ClientAssessment> dataList = new ArrayList<>();

    AssessmentsByUserAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;
    private TextView statusHeader;

    public AssessmentsByUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_by_user, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler_assessments_by_user);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        syncData = view.findViewById(R.id.syncData);
        statusHeader = view.findViewById(R.id.statusHeader);




        database = VoucherDataBase.getDatabase(context);
        bundle = getArguments();


           dataList = database.clientAssessmentDAO().getAll();
           adapter = new AssessmentsByUserAdapter(dataList);


       if(bundle!=null &&bundle.getString("searchQuery")!=null) {
           List<ClientAssessment> searchList = new ArrayList<>();
           String search = bundle.getString("searchQuery").trim();
               for (ClientAssessment data : dataList) {
                   if (data.getIdNumber().toLowerCase().contains(search.toLowerCase())
                           || data.getFname().toLowerCase().contains(search.toLowerCase())
                           || data.getLname().toLowerCase().contains(search.toLowerCase())) {
                       searchList.add(data);
                   }
               }
               dataList = searchList;
               adapter = new AssessmentsByUserAdapter(dataList);

           }


        recyclerView.setAdapter(adapter);

        assessmentsByUserFragment = this;



        syncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO REMEMBER TO SET SENT TO SERVER TRUE AND ID FROM SERVER ON SAVE
            }
        });


        return view;


    }



}