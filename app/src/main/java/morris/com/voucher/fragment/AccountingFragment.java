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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounting, container, false);
        Context context = view.getContext();

        sellVoucher = view.findViewById(R.id.sellVoucher);
        claimVoucher = view.findViewById(R.id.claimVoucher);

        accountingFragment = this;

        sellVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO ADD VOUCHER SELL CODE
            }
        });

        claimVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO ADD VOUCHER CLAIM CODE
            }
        });


        return view;
    }



}
