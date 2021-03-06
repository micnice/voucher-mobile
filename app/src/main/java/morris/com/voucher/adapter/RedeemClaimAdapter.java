package morris.com.voucher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import morris.com.voucher.MutableViewModel.SharedViewModel;
import morris.com.voucher.R;
import morris.com.voucher.TenDollarVoucherMutation;
import morris.com.voucher.activity.DashboardActivity;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.Claim;

/**
 * Created by morris on 2018/12/29.
 */

public class RedeemClaimAdapter extends RecyclerView.Adapter<RedeemClaimAdapter.ViewHolder>{


    List<Claim> items = new ArrayList<>();
    public Switch redeemedSwitch;
    public VoucherDataBase database;
    private Context context;
    private SharedViewModel sharedViewModel;
    public RedeemClaimAdapter(List<Claim> dataList) {
        this.items = dataList;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            holder.mItem = items.get(position);
            holder.voucherType.setText(items.get(position).getVoucherTypeName());
            holder.claimId.setText(items.get(position).getClaimId());
            if(items.get(position).getRedeemStatusFromServer()){
                redeemedSwitch.setChecked(Boolean.TRUE);
                redeemedSwitch.setClickable(Boolean.FALSE);
            }


    }catch (Exception e){
            e.printStackTrace();
        }

    }

    public RedeemClaimAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_claim_recycler_view, parent, false);


        database =VoucherDataBase.getDatabase(parent.getContext());
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView voucherType;
        public final TextView claimId;
        public final TextView redeemed;

        public Claim mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            AppCompatActivity activity = (AppCompatActivity) mView.getContext();
            DashboardActivity dashBoard = (DashboardActivity) activity;
            sharedViewModel = dashBoard.getSharedViewModel();


           voucherType = view.findViewById(R.id.recycler_voucher_type);
           claimId = view.findViewById(R.id.claimIdStub);
           redeemed = view.findViewById(R.id.claimRedeemedStub);
            redeemedSwitch = view.findViewById(R.id.redeemedSwitch);


                                                     redeemedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                             if (buttonView.isPressed()) {
                                                                 if (isChecked) {
                                                                     String redeemedBy="";
                                                                     if(sharedViewModel!=null && sharedViewModel.getLoginDetails()!=null && sharedViewModel.getLoginDetails().getValue()!=null){
                                                                         redeemedBy=sharedViewModel.getLoginDetails().getValue().getUserName();
                                                                     }
                                                                     Claim claim = database.claimDAO().getByClaimId(claimId.getText().toString());
                                                                     claim.setRedeemed(Boolean.TRUE);
                                                                     claim.setProcessedBy(redeemedBy);

                                                                     if (claim.getVoucherTypeName().equals("$10 Token")) {
                                                                         GraphQL.getApolloClient().mutate(TenDollarVoucherMutation.builder()
                                                                                 .saleId(claim.getSaleId()).build()).enqueue(new ApolloCall.Callback<TenDollarVoucherMutation.Data>() {

                                                                             @Override
                                                                             public void onResponse(@Nonnull Response<TenDollarVoucherMutation.Data> response) {
                                                                                 activity.runOnUiThread(new Runnable() {
                                                                                     @Override
                                                                                     public void run() {
                                                                                         getConfirmationTenDialog(response.data().createTenDollarOTP().oTP(), buttonView.getContext(), claim);
                                                                                     }
                                                                                 });

                                                                             }

                                                                             @Override
                                                                             public void onFailure(@Nonnull ApolloException e) {
                                                                                 activity.runOnUiThread(new Runnable() {
                                                                                     @Override
                                                                                     public void run() {
                                                                                         Toast.makeText(buttonView.getContext(),
                                                                                                 "Failed To Access Server!", Toast.LENGTH_LONG).show();
                                                                                         claim.setRedeemed(Boolean.FALSE);
                                                                                         database.claimDAO().updateClaim(claim);
                                                                                         redeemedSwitch.setChecked(Boolean.FALSE);
                                                                                     }
                                                                                 });

                                                                             }
                                                                         });
                                                                     } else {
                                                                         database.claimDAO().updateClaim(claim);
                                                                     }

                                                                 } else {
                                                                     Claim claim = database.claimDAO().getByClaimId(claimId.getText().toString());
                                                                     claim.setRedeemed(Boolean.FALSE);
                                                                     database.claimDAO().updateClaim(claim);
                                                                 }
                                                             }
                                                         }
                                                     });

        }

        @Override
        public String toString() {
            return super.toString() + " '"+"'";
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private void getConfirmationTenDialog(String codeFromServer,Context newContext,Claim claim){
        final EditText input = new EditText(newContext);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(newContext);
        alertDialog.setTitle("VERIFICATION $10 CODE");
        alertDialog.setMessage("Enter Verification Code");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("VERIFY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String  vcode = input.getText().toString();
                        if (vcode.trim().compareTo(codeFromServer) == 0) {
                            Toast.makeText(newContext,
                                    "Code Verified", Toast.LENGTH_LONG).show();
                            claim.setRedeemed(Boolean.TRUE);
                            database.claimDAO().updateClaim(claim);
                            redeemedSwitch.setClickable(Boolean.FALSE);

                        } else {
                            Toast.makeText(newContext,
                                    "Wrong Code!", Toast.LENGTH_LONG).show();
                            redeemedSwitch.setChecked(Boolean.FALSE);
                            claim.setRedeemed(Boolean.FALSE);
                            database.claimDAO().updateClaim(claim);
                        }
                    }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        redeemedSwitch.setChecked(Boolean.FALSE);
                        claim.setRedeemed(Boolean.FALSE);
                        database.claimDAO().updateClaim(claim);
                        dialog.cancel();
                    }
                });
        alertDialog.show();

    }

}
