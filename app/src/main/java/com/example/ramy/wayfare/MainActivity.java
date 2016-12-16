package com.example.ramy.wayfare;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AccountAuthenticatorActivity {

    EditText editText1, editText2;
    Button button;
	public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";
    private String mAuthTokenType = "Full access";
	private final int REQ_SIGNUP = 1;
    private AccountManager mAccountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccountManager = AccountManager.get(getBaseContext());
        editText1= (EditText) findViewById(R.id.editText1);
        editText2= (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent register = new Intent(getBaseContext(), Register.class);
              //  register.putExtras(getIntent().getExtras());
                startActivityForResult(register, REQ_SIGNUP);
            }
        });
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnLogin(View view) {
       startActivity(new Intent(this,Login.class));
    }

	private void finishLogin(Intent intent) {
        Log.d("udinic", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d("BROS", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("BROS1", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
	
  /*  public void OnRegister(View view){
		Intent register = new Intent(getBaseContext(), Register.class);
        register.putExtras(getIntent().getExtras());
        startActivityForResult(register, REQ_SIGNUP);
    }*/

    public void OnFetch(View view){
        startActivity(new Intent(this,Fetch.class));
    }

    public void OnImage(View view) { startActivity(new Intent(this, Image.class));}
}
