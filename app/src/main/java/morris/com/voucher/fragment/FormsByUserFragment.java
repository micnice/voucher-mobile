package morris.com.voucher.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import morris.com.voucher.R;
import morris.com.voucher.adapter.FormsByUserAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/29.
 */

public class FormsByUserFragment extends BaseFragment {

   public  static FormsByUserFragment formsByUserFragment;
    private RecyclerView recyclerView;
    Button addNew;
    private List<IdentificationData> dataList = new ArrayList<>();
    FormsByUserAdapter adapter;
    private LinearLayoutManager layoutManager;
    public VoucherDataBase database;

    public FormsByUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Bundle bundle;
        database = VoucherDataBase.getDatabase(context);
        bundle = getArguments();
       if(bundle!=null && bundle.getString("item")!=null &&bundle.getString("item").equals("assessment")){
           dataList = database.identificationDataDAO().getAllNotAssessed();
           addNew.setVisibility(View.GONE);
       }else {
           dataList = database.identificationDataDAO().getAll();
       }
        adapter = new FormsByUserAdapter(dataList);
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


        return view;
    }



}
