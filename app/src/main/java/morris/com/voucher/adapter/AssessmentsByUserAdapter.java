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
import morris.com.voucher.fragment.AssessClientFragment;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.ClientAssessment;
import morris.com.voucher.model.IdentificationData;
import morris.com.voucher.util.CalculationsUtil;

/**
 * Created by morris on 2018/12/29.
 */

public class AssessmentsByUserAdapter extends RecyclerView.Adapter<AssessmentsByUserAdapter.ViewHolder>{


    List<ClientAssessment> items = new ArrayList<>();
    public  Button edit,sts;

    public AssessmentsByUserAdapter(List<ClientAssessment> dataList) {
        this.items = dataList;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {


            holder.mItem = items.get(position);
            holder.name.setText(items.get(position).getFname()+" "+items.get(position).getLname());
            holder.prenancyStatus.setText(items.get(position).getPregnancyStatus());
            holder.povertyScore.setText(Integer.toString(CalculationsUtil.getPovertyScore(items.get(position))));

            System.out.println("###--"+items.get(position).isPart2());

            if(!items.get(position).isPart2()){
                System.out.println("###HOHOHOHO");
            }
            if (items.get(position).isMarkAsFinalised() && !items.get(position).isSentToServer()) {
                holder.status.setText("F");
                edit.setVisibility(View.VISIBLE);
                sts.setVisibility(View.GONE);
            } else if (!items.get(position).isMarkAsFinalised()) {
                edit.setVisibility(View.VISIBLE);
                holder.status.setText("NF");
                sts.setVisibility(View.GONE);
            } else {
                holder.status.setText("STS");
                edit.setVisibility(View.GONE);

            }


    }catch (Exception e){
            e.printStackTrace();
        }

    }

    public AssessmentsByUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_assessment_recycler_view, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView name;
        public final TextView prenancyStatus;
        public final TextView status;
        public final TextView povertyScore;
        public ClientAssessment mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            AppCompatActivity activity = (AppCompatActivity) mView.getContext();

            name = view.findViewById(R.id.recycler_name);
            prenancyStatus = view.findViewById(R.id.recycler_pregnancy);
            edit = view.findViewById(R.id.editAssessment);
            status = view.findViewById(R.id.assessment_status);
            sts = view.findViewById(R.id.sentAssessmentToServer);
            povertyScore = view.findViewById(R.id.recycler_score);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO Put Working Edit Code

                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + name+"'";
        }
    }
    @Override
    public int getItemCount() {

        return items.size();
    }


}
