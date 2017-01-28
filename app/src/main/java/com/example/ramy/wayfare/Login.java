package com.example.ramy.wayfare;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class Login extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";
    public static String  accountName = null;
    private static Context context;
    private final int REQ_SIGNUP = 1;
    final Intent res = new Intent();

    private final String TAG = this.getClass().getSimpleName();

    public static AccountManager mAccountManager;
    public static String userName;
    private String mAuthTokenType;

    SharedPreferences sharedpreferences;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        Log.d("udinic", TAG + accountName);

        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            ((EditText)findViewById(R.id.accountName)).setText(accountName);
        }

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Since there can only be one AuthenticatorActivity, we call the sign up activity, get his results,
                // and return them in setAccountAuthenticatorResult(). See finishLogin().
                Intent signup = new Intent(getBaseContext(), Register.class);
//                signup.putExtras(getIntent().getExtras());
                startActivityForResult(signup, REQ_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            Log.e("finish finish", "login");
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void submit() {

        userName = ((EditText) findViewById(R.id.accountName)).getText().toString();
        final String userPass = ((EditText) findViewById(R.id.accountPassword)).getText().toString();

        final String accountType = "com.example.ramy.wayfare";//getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        Log.d("udinic", TAG + accountType);

        new ServerTask(Login.this,""){

            @Override
            protected void onPreExecute() {
                setContentView(R.layout.progressbar_large);
            }

            @Override
            protected String doInBackground(String... params) {
                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    authtoken = UserSignIn(userName, userPass);
                    Log.d("SALEM", TAG + authtoken);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, userPass);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }
                res.putExtras(data);
                publishProgress();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                finishLogin(res);
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    public void getToken()
    {
        new AsyncTask<String, Void, Intent>() {
                    @Override
                    protected Intent doInBackground(String... params) {
                        Log.w("TOKEN", "yahia1245");
                        try {
                            Log.w("TOKEN", "yahia1234");
                            Log.w("TOKEN12","sioufy");
                            String y = ServerTask.getAuthToken(mAccountManager, accountName);
                            Log.w("TOKEN", y);//
                            return null;
                        } catch (Exception e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Intent intent) {
                        Log.w("TOKEN", "yahia124");
                        }
                }.execute();
    }


    private void finishLogin(Intent intent) {
        Log.d("udinic", TAG + "> finishLogin");

        accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

        Log.d("udinic", TAG + accountName);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        Log.d("udinic", TAG + intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("accountName", accountName);
        editor.commit();

        boolean available = checkAccount();
        Log.e("Check Account", available+"");
        if (!available) {
            Log.d("udinic", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)

            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("udinic", TAG + "> finishLogin > setPassword");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;
            mAccountManager.setPassword(account, accountPassword);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
//        finish();
    }
    public static boolean checkAccount() { //send ApplicationContext as a parameter
        Account[] accounts = null;
        boolean isPresent = false;
        String accountname = accountName;
//        AccountManager accountManager = AccountManager.get(context);
        Account account = null;
        if(accountname != null) {
            try {
                Log.e("name", accountname);
                accounts = mAccountManager.getAccountsByType("com.example.ramy.com.wayfare");
            } catch (SecurityException e) {
                Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
                toast.show();
            }
            for(Account a : accounts) {
                Log.e("yahia",a.name.toString());

                if(a.name.toString().equalsIgnoreCase(accountname)) {
                    Log.e("yahia","yahia123");
                    isPresent = true;
                }
            }
        }
        return isPresent;
    }
}