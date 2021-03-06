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
import morris.com.voucher.fragment.RegisterClientFragment;
import morris.com.voucher.model.AssessmentDataFromServer;
import morris.com.voucher.model.IdentificationData;

/**
 * Created by morris on 2018/12/29.
 */

public class FormsByUserAdapter  extends RecyclerView.Adapter<FormsByUserAdapter.ViewHolder>{


    List<IdentificationData> items = new ArrayList<>();
    List<AssessmentDataFromServer> serverList = new ArrayList<>();
    Bundle bundle = new Bundle();
    Bundle bundleFromPreviousPage;
    public  Button assess,edit,sts;

    public FormsByUserAdapter(List<IdentificationData> dataList) {
        this.items = dataList;

    }
    public FormsByUserAdapter(List<AssessmentDataFromServer> dataList,Bundle bundle) {
        this.serverList = dataList;
        this.bundle =bundle;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {

            if((bundle!=null&&bundle.getString("item")!=null
                    && bundle.getString("item").equals("assessment"))||(bundle!=null && bundle.getString("current")!=null
                    &&bundle.getString("current").equals("assessment"))){
                holder.mItem1 = serverList.get(position);
                holder.firstName.setText(serverList.get(position).getFname());
                holder.lastName.setText(serverList.get(position).getLname());
                holder.idNumber.setText(serverList.get(position).getIdNumber());
                holder.clientId.setText(serverList.get(position).getClientId());
                edit.setVisibility(View.GONE);
                sts.setVisibility(View.GONE);

            }else {

                holder.mItem = items.get(position);
                holder.firstName.setText(items.get(position).getFirstName());
                holder.lastName.setText(items.get(position).getLastName());
                holder.idNumber.setText(items.get(position).getIdentificationNumber());
                holder.clientId.setText(String.valueOf(items.get(position).getId()));
                if(items.get(position).isMarkAsFinalised() && !items.get(position).isSentToServer()){
                    holder.status.setText("F");
                    edit.setVisibility(View.VISIBLE);
                    sts.setVisibility(View.GONE);
                }else if(!items.get(position).isMarkAsFinalised()){
                    edit.setVisibility(View.VISIBLE);
                    holder.status.setText("NF");
                    sts.setVisibility(View.GONE);
                }else{
                    holder.status.setText("STS");
                    edit.setVisibility(View.GONE);

                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public FormsByUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_form_recycler_view, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView firstName;
        public final TextView lastName;
        public final TextView idNumber;
        public final TextView status;
        public final TextView clientId;
        public IdentificationData mItem;
        public AssessmentDataFromServer mItem1;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            AppCompatActivity activity = (AppCompatActivity) mView.getContext();

            firstName = view.findViewById(R.id.recycler_fname);
            lastName = view.findViewById(R.id.recycler_lname);
            idNumber = view.findViewById(R.id.recycler_idnum);
            assess= view.findViewById(R.id.assessClient);
            edit = view.findViewById(R.id.edit);
            status = view.findViewById(R.id.status);
            sts = view.findViewById(R.id.sentToServer);
            clientId = view.findViewById(R.id.benIdStub);
            Bundle savedInstance = getBundleFromPreviousPage();

            if(savedInstance==null){
                assess.setVisibility(View.GONE);
            }else if((savedInstance.getString("item")==null)||!savedInstance.getString("item").equals("assessment")){
                assess.setVisibility(View.GONE);
            }else{

            }

            assess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment= new AssessClientFragment();
                    bundle.putString("clientId",clientId.getText().toString());
                    bundle.putString("fname",firstName.getText().toString());
                    bundle.putString("lname",lastName.getText().toString());
                    bundle.putString("idNumber",idNumber.getText().toString());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.register_client_holder, fragment).addToBackStack(null).commit();

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new RegisterClientFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("updateClient","updateIdData");
                    bundle.putString("idDataId",clientId.getText().toString());
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
       if( bundle!=null&&bundle.getString("item")!=null
                && bundle.getString("item").equals("assessment")){
            return serverList.size();
        }
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

    public Bundle getBundleFromPreviousPage() {
        return bundleFromPreviousPage;
    }

    public void setBundleFromPreviousPage(Bundle bundleFromPreviousPage) {
        this.bundleFromPreviousPage = bundleFromPreviousPage;
    }
}
