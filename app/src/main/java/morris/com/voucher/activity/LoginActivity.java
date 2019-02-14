package morris.com.voucher.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.json.JSONArray;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import morris.com.voucher.MutableViewModel.SharedViewModel;
import morris.com.voucher.R;
import morris.com.voucher.UserSignInMutation;
import morris.com.voucher.graphql.GraphQL;
import morris.com.voucher.model.LoginDetails;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity  {



    private UserLoginTask mAuthTask = null;
    public static Activity activity;
    private SharedViewModel sharedViewModel;
    private static SharedViewModel viewModelToOtherActivities;
    // UI references.
    private EditText userName;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        userName =  findViewById(R.id.username);
        activity = LoginActivity.this;
        mPasswordView = findViewById(R.id.password);

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras =getIntent().getExtras();
        if(extras!=null) {
            String failed = extras.getString("login");
            String uName = extras.getString("userName");
            String pw = extras.getString("pw");

            if (failed != null && failed.equals("failed")) {
                userName.setText(uName);
                mPasswordView.setText(pw);
                loginFailed(Boolean.TRUE);
            }
            else {
                String netWorkFailed = extras.getString("network");
                if(netWorkFailed!=null && netWorkFailed.equals("failed")){
                    userName.setText(uName);
                    mPasswordView.setText(pw);
                    netWorkFailure();
                }
            }
        }

                mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

private void loginFailed(Boolean failed){
        if(failed){
            mPasswordView.setError("Invalid UserName or Password");
            mPasswordView.requestFocus();

        }
        }
private  void netWorkFailure(){
    Snackbar.make(userName,
            "Login Failed Check Your Network",
            Snackbar.LENGTH_LONG)
            .show();
}

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = userName.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a not empty userName address.
        if (TextUtils.isEmpty(email)) {
            userName.setError(getString(R.string.error_field_required));
            focusView = userName;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUsername = userName;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            GraphQL.getApolloClient().mutate(UserSignInMutation.builder()
            .username(mUsername).password(mPassword).build()).enqueue(new ApolloCall.Callback<UserSignInMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<UserSignInMutation.Data> response) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           if (response.data().signin().token() != null) {

                                Set<String> roles = new HashSet<>();
                                if(!response.data().signin().roleSet().isEmpty()){
                                    for(UserSignInMutation.RoleSet role:response.data().signin().roleSet()){
                                        roles.add(role.name());
                                    }
                                }
                                LoginDetails loginDetails = new LoginDetails();
                                loginDetails.setUserName(response.data().signin().username());
                                loginDetails.setRoles(roles);
                                sharedViewModel.setLoginDetails(loginDetails);
                                setViewModelToOtherActivities(sharedViewModel);
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));


                            } else {
                                if(!hostAvailable()){
                                    Intent intent=new Intent(LoginActivity.this, LoginActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("network","failed");
                                    extras.putString("userName",mUsername);
                                    extras.putString("pw",mPassword);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }else {

                                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("login", "failed");
                                    extras.putString("userName", mUsername);
                                    extras.putString("pw", mPassword);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                }
                            }

                        }
                    });
                }


                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(LoginActivity.this, LoginActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("network","failed");
                            extras.putString("userName",mUsername);
                            extras.putString("pw",mPassword);
                            intent.putExtras(extras);
                            startActivity(intent);
                                 }
                    });

                }
            });



            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    boolean hostAvailable(){
        try (Socket socket = new Socket()) {

            socket.connect(new InetSocketAddress("www.google.com", 80), 5000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            System.out.println(e);
            return false;
        }
    }

    public static SharedViewModel getViewModelToOtherActivities() {
        return viewModelToOtherActivities;
    }

    public void setViewModelToOtherActivities(SharedViewModel viewModelToOtherActivities) {
        this.viewModelToOtherActivities = viewModelToOtherActivities;
    }

}

