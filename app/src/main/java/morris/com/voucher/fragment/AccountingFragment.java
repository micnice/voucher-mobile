package morris.com.voucher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import morris.com.voucher.R;
import morris.com.voucher.adapter.FormsByUserAdapter;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2019/01/02.
 */

public class AccountingFragment extends BaseFragment {

   public  static AccountingFragment accountingFragment;
    private RecyclerView recyclerView;
    Button sellVoucher,claimVoucher;
    private List<IdentificationData> dataList = new ArrayList<>();
    FormsByUserAdapter adapter;

    public VoucherDataBase database;

    public AccountingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu,inflater);
    menu.clear();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounting, container, false);
        Context context = view.getContext();

        sellVoucher = view.findViewById(R.id.sellVoucher);
        claimVoucher = view.findViewById(R.id.claimVoucher);
       getActivity().findViewById(R.id.search).setVisibility(View.GONE);

        accountingFragment = this;

        sellVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment= new AccountingFormsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

            }
        });

        claimVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.Fragment fragment= new SaleIdentificationDataFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();
            }
        });


        return view;
    }



}
