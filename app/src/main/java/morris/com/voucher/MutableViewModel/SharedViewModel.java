package morris.com.voucher.MutableViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import org.json.JSONArray;

import java.util.List;
import java.util.Set;

import morris.com.voucher.model.LoginDetails;

/**
 * Created by morris on 2019/02/13.
 */

public class SharedViewModel extends ViewModel {
    private MutableLiveData<LoginDetails> loginDetails = new MutableLiveData<>();

    public LiveData<LoginDetails> getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(LoginDetails loginDetails) {
        this.loginDetails.setValue(loginDetails);
    }
}
