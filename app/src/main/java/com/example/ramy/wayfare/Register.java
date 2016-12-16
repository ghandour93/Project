package com.example.ramy.wayfare;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static com.example.ramy.wayfare.MainActivity.ARG_ACCOUNT_TYPE;
import static com.example.ramy.wayfare.MainActivity.PARAM_USER_PASS;

public class Register extends AppCompatActivity {

    EditText editText, editText3, editText4;
	private String mAccountType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		mAccountType = "com.example.ramy.wayfare";//getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        setContentView(R.layout.activity_register);
        editText= (EditText) findViewById(R.id.editText);
        editText3= (EditText) findViewById(R.id.editText3);
        editText4= (EditText) findViewById(R.id.editText4);
    }

    public void OnReg(View view){
		
       new AsyncTask<String, Void, Intent>() {

            String accountName = ((TextView) findViewById(R.id.editText)).getText().toString().trim();
            String username = ((TextView) findViewById(R.id.editText3)).getText().toString().trim();
            String accountPassword = ((TextView) findViewById(R.id.editText4)).getText().toString().trim();
			
			ServerTask s = new ServerTask(Register.this);
            @Override
            protected Intent doInBackground(String... params) {

                //Log.d("REGISTER BRO", TAG + "> Started authenticating");

                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    authtoken = s.UserSignUp(accountName, username, accountPassword);

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, accountPassword);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

}
