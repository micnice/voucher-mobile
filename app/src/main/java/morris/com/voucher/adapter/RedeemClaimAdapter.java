package morris.com.voucher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import morris.com.voucher.R;
import morris.com.voucher.database.VoucherDataBase;
import morris.com.voucher.model.Claim;

/**
 * Created by morris on 2018/12/29.
 */

public class RedeemClaimAdapter extends RecyclerView.Adapter<RedeemClaimAdapter.ViewHolder>{


    List<Claim> items = new ArrayList<>();
    public Switch redeemedSwitch;
    public VoucherDataBase database;
    private Context context;
    Boolean verified = Boolean.FALSE;

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

           voucherType = view.findViewById(R.id.recycler_voucher_type);
           claimId = view.findViewById(R.id.claimIdStub);
           redeemed = view.findViewById(R.id.claimRedeemedStub);
            redeemedSwitch = view.findViewById(R.id.redeemedSwitch);


            redeemedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                   Claim claim = database.claimDAO().getByClaimId(claimId.getText().toString());
                   if(!claim.getHasOTP()) {
                       claim.setRedeemed(Boolean.TRUE);
                       database.claimDAO().updateClaim(claim);
                   }else{
                       activity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               final EditText input = new EditText(buttonView.getContext());
                               AlertDialog.Builder alertDialog = new AlertDialog.Builder(buttonView.getContext());
                               alertDialog.setTitle("VERIFICATION CODE");
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
                                               if (vcode.trim().compareTo("F1012") == 0) {
                                                   Toast.makeText(getContext(),
                                                           "Code Verified", Toast.LENGTH_SHORT).show();
                                                   verified =Boolean.TRUE;

                                               } else {
                                                   Toast.makeText(getContext(),
                                                           "Wrong Code!", Toast.LENGTH_SHORT).show();
                                                   verified =Boolean.FALSE;
                                               }
                                           }

                                       });

                               alertDialog.setNegativeButton("CANCEL",
                                       new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog, int which) {
                                               verified =Boolean.FALSE;
                                               dialog.cancel();
                                           }
                                       });
                             AlertDialog show = alertDialog.create();
                               show.show();


                           }
                       });


                   }
                    } else {
                        Claim claim = database.claimDAO().getByClaimId(claimId.getText().toString());
                        claim.setRedeemed(Boolean.FALSE);
                        database.claimDAO().updateClaim(claim);
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


    private Boolean getConfirmationCodeDialog(String codeFromServer){

        final EditText input = new EditText(getContext());
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
    alertDialog.setTitle("VERIFICATION CODE");
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
                        if (vcode.trim().compareTo(codeFromServer.trim()) == 0) {
                                Toast.makeText(getContext(),
                                        "Code Verified", Toast.LENGTH_SHORT).show();
                                verified =Boolean.TRUE;

                            } else {
                                Toast.makeText(getContext(),
                                        "Wrong Code!", Toast.LENGTH_SHORT).show();
                            verified =Boolean.FALSE;
                            }
                        }

                });

        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        verified =Boolean.FALSE;
                        dialog.cancel();
                    }
                });
        alertDialog.show();

        System.out.println("###---"+verified);
        return verified;
    }



}
