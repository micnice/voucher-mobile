package morris.com.voucher.adapter;

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
import morris.com.voucher.model.Claim;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.util.CalculationsUtil;

/**
 * Created by morris on 2018/12/29.
 */

public class RedeemClaimAdapter extends RecyclerView.Adapter<RedeemClaimAdapter.ViewHolder>{


    List<Claim> items = new ArrayList<>();

    public RedeemClaimAdapter(List<Claim> dataList) {
        this.items = dataList;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            holder.mItem = items.get(position);
            holder.voucherType.setText(items.get(position).getVoucherTypeName());
            holder.claimId.setText(items.get(position).getClaimId());
            if(items.get(position).getRedeemed()){
                holder.redeemed.setText("yes");
            }else{
                holder.redeemed.setText("no");
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


}
