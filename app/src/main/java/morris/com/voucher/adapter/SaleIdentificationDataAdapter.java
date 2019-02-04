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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.R;
import morris.com.voucher.VoucherClaimsBySaleIdQuery;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.fragment.RedeemClaimFragment;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.Claim;
import morris.com.voucher.model.SaleIdentificationData;

/**
 * Created by morris on 2018/12/29.
 */

public class SaleIdentificationDataAdapter extends RecyclerView.Adapter<SaleIdentificationDataAdapter.ViewHolder>{


    List<SaleIdentificationData> items = new ArrayList<>();
    public VoucherDataBase database;
    public  Button viewSale;

    public SaleIdentificationDataAdapter(List<SaleIdentificationData> dataList) {
        this.items = dataList;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            holder.mItem = items.get(position);
            holder.firstName.setText(items.get(position).getFirstName());
            holder.lastName.setText(items.get(position).getLastName());
            holder.idNumber.setText(items.get(position).getIdNumber());
            holder.packageName.setText(items.get(position).getPackageName());
            holder.saleId.setText(items.get(position).getSaleId());



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public SaleIdentificationDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_saledata_view, parent, false);
   database =VoucherDataBase.getDatabase(parent.getContext());

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView firstName;
        public final TextView lastName;
        public final TextView idNumber;
        public final TextView packageName;
        public final TextView saleId;
        public SaleIdentificationData mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            AppCompatActivity activity = (AppCompatActivity) mView.getContext();

            firstName = view.findViewById(R.id.sd_firstName);
            lastName = view.findViewById(R.id.sd_lastName);
            idNumber = view.findViewById(R.id.sd_idNum);
            packageName = view.findViewById(R.id.sd_package);
            viewSale= view.findViewById(R.id.viewSale);
            saleId = view.findViewById(R.id.saleIdStub);

            viewSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sdFirstName", firstName.getText().toString());
                    bundle.putString("sdLastName",lastName.getText().toString() );
                    bundle.putString("sdIdNumber",idNumber.getText().toString() );
                    database.claimDAO().deleteAll();
                    GraphQL.getApolloClient().query(VoucherClaimsBySaleIdQuery.builder()
                            .sales(saleId.getText().toString())
                            .build()).enqueue(new ApolloCall.Callback<VoucherClaimsBySaleIdQuery.Data>() {
                        @Override
                        public void onResponse(@Nonnull Response<VoucherClaimsBySaleIdQuery.Data> response) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<VoucherClaimsBySaleIdQuery.ClaimsBySaleId> responseList = response.data().claimsBySaleId();

                                    if(!responseList.isEmpty()){
                                        for(VoucherClaimsBySaleIdQuery.ClaimsBySaleId data:responseList) {
                                            Claim claim = new Claim();
                                            claim.setClaimId(data.id());
                                            claim.setRedeemStatusFromServer(data.redeemed());
                                            claim.setHasOTP(data.hasOTP());
                                            claim.setSaleId(data.sales().id());
                                            claim.setVoucherTypeName(data.voucherType().name());
                                            database.claimDAO().saveClaim(claim);
                                        }
                                        Fragment fragment= new RedeemClaimFragment();
                                        fragment.setArguments(bundle);
                                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

                                    }

                                }
                            });



                        }

                        @Override
                        public void onFailure(@Nonnull ApolloException e) {
                            DashboardActivity dashBoard = (DashboardActivity)activity;
                            dashBoard.noNetworkNotice();

                        }
                    });



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

