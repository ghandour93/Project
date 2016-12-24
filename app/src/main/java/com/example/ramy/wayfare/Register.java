package com.example.ramy.wayfare;

//import android.accounts.AccountManager;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
//import static com.example.ramy.wayfare.MainActivity.PARAM_USER_PASS;
//
//public class Register extends AppCompatActivity {
//
//    EditText editText, editText3, editText4;
//	private String mAccountType;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//		mAccountType = "com.example.ramy.wayfare";//getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
//        setContentView(R.layout.activity_register);
//        editText= (EditText) findViewById(R.id.editText);
//        editText3= (EditText) findViewById(R.id.editText3);
//        editText4= (EditText) findViewById(R.id.editText4);
//    }
//
//    public void OnReg(View view){
//
//       new AsyncTask<String, Void, Intent>() {
//
//            String accountName = ((TextView) findViewById(R.id.editText)).getText().toString().trim();
//            String username = ((TextView) findViewById(R.id.editText3)).getText().toString().trim();
//            String accountPassword = ((TextView) findViewById(R.id.editText4)).getText().toString().trim();
//
//			ServerTask s = new ServerTask(Register.this);
//            @Override
//            protected Intent doInBackground(String... params) {
//
//                //Log.d("REGISTER BRO", TAG + "> Started authenticating");
//
//                String authtoken = null;
//                Bundle data = new Bundle();
//                try {
//                    authtoken = s.UserSignUp(accountName, username, accountPassword);
//
//                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
//                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
//                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
//                    data.putString(PARAM_USER_PASS, accountPassword);
//                } catch (Exception e) {
//                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
//                }
//
//                final Intent res = new Intent();
//                res.putExtras(data);
//                return res;
//            }
//
//            @Override
//            protected void onPostExecute(Intent intent) {
//                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
//                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
//                } else {
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            }
//        }.execute();
//    }
//
//}


import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    private static final int MAX_PAGES = 3;
    private DynamicViewPager viewPager;
    String mAccountType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountType = "com.example.ramy.wayfare";
        setContentView(R.layout.activity_register);
//        this.getSupportActionBar().hide();
        viewPager = (DynamicViewPager) findViewById(R.id.pager);
        viewPager.setMaxPages(MAX_PAGES);
        viewPager.setBackgroundAsset(R.mipmap.yaya);
        viewPager.setAdapter(new MyPagerAdapter());

        /*if (savedInstanceState != null) {
            num_pages = savedInstanceState.getInt("num_pages");
            viewPager.setCurrentItem(savedInstanceState.getInt("current_page"), false);
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putInt("num_pages", num_pages);
        outState.putInt("current_page", viewPager.getCurrentItem());*/
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MAX_PAGES;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.layout_page, null);

            TextView name = (TextView) view.findViewById(R.id.WhatsYourName);
            TextView p = (TextView) view.findViewById(R.id.pass);
            TextView r = (TextView) view.findViewById(R.id.repeat);
            ImageView finish = (ImageView) view.findViewById(R.id.imageView);
            EditText y = (EditText) view.findViewById(R.id.username);
            EditText Reppass = (EditText) view.findViewById(R.id.RepeatPass);

             String email = null;
             String username = null;
             String password = null;
//            String pos = "This is page " + (position + 1);

            if (position == 0) {


                name.setVisibility(View.VISIBLE);
                y.setVisibility(View.VISIBLE);

            } else if (position == 1) {
                username = y.getText().toString().trim();
                Log.e("username", username);
                y.setText(" ");
                name.setText("What's Your Email");
                name.setVisibility(View.VISIBLE);
                y.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                y.setVisibility(View.VISIBLE);

            } else if (position == 2) {
                email = y.getText().toString().trim();
                Log.e("email", email);
                name.setText("Let's get you Secured !");
                y.setVisibility(View.VISIBLE);
                p.setVisibility(View.VISIBLE);
                r.setVisibility(View.VISIBLE);
                Reppass.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
//                y.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                y.setTransformationMethod(PasswordTransformationMethod.getInstance());
                finish.setVisibility(View.VISIBLE);
//                finish.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        new AsyncTask<String, Void, Intent>() {

//            String accountName = email;
//            String username = ((TextView) findViewById(R.id.editText3)).getText().toString().trim();
//            String accountPassword = ((TextView) findViewById(R.id.editText4)).getText().toString().trim();
//
//                            ServerTask s = new ServerTask(Register.this);
//
//                            @Override
//                            protected Intent doInBackground(String... params) {
//
//                                //Log.d("REGISTER BRO", TAG + "> Started authenticating");
//
//                                String authtoken = null;
//                                Bundle data = new Bundle();
//                                try {
//                                    authtoken = s.UserSignUp(email, username, password);
//
//                                    data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
//                                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
//                                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
//                                    data.putString(PARAM_USER_PASS, password);
//                                } catch (Exception e) {
//                                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
//                                }
//
//                                final Intent res = new Intent();
//                                res.putExtras(data);
//                                return res;
//                            }
//
//                            @Override
//                            protected void onPostExecute(Intent intent) {
//                                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
//                                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
//                                } else {
//                                    setResult(RESULT_OK, intent);
//                                    finish();
//                                }
//                            }
//                        }.execute();
//
//                    }
//
//                });

            }
            container.addView(view);
            return view;
        }
    }
}
