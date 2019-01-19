package morris.com.voucher.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.fragment.AssessClientFragment;
import morris.com.voucher.fragment.SaleVoucherFragment;
import morris.com.voucher.model.AccountingClient;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;
import morris.com.voucher.util.CalculationsUtil;

/**
 * Created by morris on 2018/12/29.
 */

public class AccountingClientsAdapter extends RecyclerView.Adapter<AccountingClientsAdapter.ViewHolder>{


    List<AccountingClient> items = new ArrayList<>();
    public VoucherDataBase database;
    public  Button makeSale,sm;

    public AccountingClientsAdapter(List<AccountingClient> dataList) {
        this.items = dataList;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            holder.mItem = items.get(position);
            holder.firstName.setText(items.get(position).getFirstName());
            holder.lastName.setText(items.get(position).getLastName());
            holder.idNumber.setText(items.get(position).getIdNumber());
            holder.povertyScore.setText(Long.toString(items.get(position).getPovertyScore()));

            if (items.get(position).isSaleMade()) {
                sm.setVisibility(View.VISIBLE);
                makeSale.setVisibility(View.GONE);
            } else {
                makeSale.setVisibility(View.VISIBLE);
                sm.setVisibility(View.GONE);
            }





        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public AccountingClientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_accounting_client_view, parent, false);
   database =VoucherDataBase.getDatabase(parent.getContext());

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView firstName;
        public final TextView lastName;
        public final TextView idNumber;
        public final TextView povertyScore;
        public AccountingClient mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            AppCompatActivity activity = (AppCompatActivity) mView.getContext();

            firstName = view.findViewById(R.id.rec_firstName);
            lastName = view.findViewById(R.id.rec_lastName);
            idNumber = view.findViewById(R.id.rec_idNum);
            povertyScore = view.findViewById(R.id.rec_povertyScore);
            makeSale= view.findViewById(R.id.makeSale);
            sm = view.findViewById(R.id.saleMade);

            makeSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Bundle bundle = new Bundle();
                   String nameLast =lastName.getText().toString();
                   String numberId = idNumber.getText().toString();
                   AccountingClient client = database.accountingClientDAO().getByIdNumberAndLastName(numberId,nameLast);

                    bundle.putString("accountingFName", firstName.getText().toString());
                    bundle.putString("accountingLName",nameLast );
                    bundle.putString("accountingIdNumber",numberId );
                    bundle.putString("accountingClientId",client.getClientId());

                    Fragment fragment= new SaleVoucherFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + firstName.getText().toString() + " "+lastName.getText().toString()+"'";
        }
    }
    @Override
    public int getItemCount() {

        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

