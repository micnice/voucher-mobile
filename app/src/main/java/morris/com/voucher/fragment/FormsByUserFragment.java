package morris.com.voucher.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
   //private int mColumnCount = 4;
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
        Bundle bundle;
        ////recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.setNestedScrollingEnabled(false);
        database = VoucherDataBase.getDatabase(context);
        bundle = getArguments();
       if(bundle.getString("item")!=null &&bundle.getString("item").equals("assessment")){
           dataList = database.identificationDataDAO().getAllNotAssessed();
       }else {
           dataList = database.identificationDataDAO().getAll();
       }
        adapter = new FormsByUserAdapter(dataList);
        recyclerView.setAdapter(adapter);

        formsByUserFragment = this;


        return view;
    }



}
