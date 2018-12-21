package morris.com.voucher.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import morris.com.voucher.R;

/**
 * Created by morris on 2018/12/17.
 */

public class RegisterClientFragment extends BaseFragment {

    FragmentActivity fragmentActivity;
    JSONObject identificationData = new JSONObject();
    Toolbar toolbar;
    Button saveData;
    EditText lmp,firstName,lastName,maritalStatus,birthDate,parity,
    identificationNumber,latitude,longitude;
    Context context;
    Activity activity;
    View view;

    public RegisterClientFragment() {
    }
    @Override
    public  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragment_register_client, container, false);

        context = getContext();
        activity = getActivity();
        lmp = view.findViewById(R.id.lmp);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        maritalStatus = view.findViewById(R.id.maritalStatus);
        birthDate = view.findViewById(R.id.birthDate);
        parity = view.findViewById(R.id.parity);
        identificationNumber = view.findViewById(R.id.identificationNumber);
        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);


        return view;

    }
}
