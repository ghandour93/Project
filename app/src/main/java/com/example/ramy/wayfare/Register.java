package com.example.ramy.wayfare;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static com.example.ramy.wayfare.Login.PARAM_USER_PASS;

public class Register extends AppCompatActivity {

    private static final int MAX_PAGES = 3;
    private DynamicViewPager viewPager;
    String mAccountType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountType = "com.example.ramy.wayfare";
        setContentView(R.layout.activity_register);

        viewPager = (DynamicViewPager) findViewById(R.id.pager);
        viewPager.setMaxPages(MAX_PAGES);
        viewPager.setBackgroundAsset(R.drawable.yaya);
        viewPager.setAdapter(new MyPagerAdapter());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putInt("num_pages", num_pages);
        outState.putInt("current_page", viewPager.getCurrentItem());*/
    }


    private class MyPagerAdapter extends PagerAdapter {
        String email = null;
        String nameOfUser = null;
        String password = null;
        String passwordRepeat = null;

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

        public void setUserName(String name)
        {
            nameOfUser = name;
        }
        public String getUserName()
        {
            return nameOfUser;
        }

        public void setEmail(String e)
        {
            email = e;
        }
        public String getEmail()
        {
            return email;
        }

        public void setPassword(String pass)
        {
            password = pass;
        }

        public void setPasswordRepeat(String pass)
        {
            passwordRepeat = pass;
        }

        public String getPassword()
        {
            return password;
        }

        public String getPasswordRepeat()
        {
            return passwordRepeat;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.e("yy","instantiateItem");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_page, null);

            final TextView name = (TextView) view.findViewById(R.id.WhatsYourName);

            final TextView p = (TextView) view.findViewById(R.id.pass);
            final TextView r = (TextView) view.findViewById(R.id.repeat);
            final ImageView finish = (ImageView) view.findViewById(R.id.imageView);
            final EditText y = (EditText) view.findViewById(R.id.username);
            final EditText m = (EditText) view.findViewById(R.id.email);
            final EditText password = (EditText) view.findViewById(R.id.password);
            final EditText Reppass = (EditText) view.findViewById(R.id.RepeatPass);


            if (viewPager.getCurrentItem() == 0) {
                name.setVisibility(View.VISIBLE);
                y.setText(" ");
                y.setVisibility(View.VISIBLE);

                y.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        setUserName(s+"");
                        Log.e("name", getUserName());
                        Log.e("after",s.toString());
                    }
                });
            }

//            String pos = "This is page " + (position + 1);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                int currentPosition = 0;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int newPosition) {
                    switch (newPosition)
                    {
                        case 0:
                            y.setText(" ");
                            Log.e("case 0", currentPosition+""+ " " +newPosition+"");
                            name.setText("What's Your Name");
                            name.setVisibility(View.VISIBLE);
                            y.setVisibility(View.VISIBLE);
                            y.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    Log.e("strange", "strange");
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    setUserName(s+"");
                                }
                            });
                            break;

                        case 1:

                            Log.e("case 1", currentPosition+""+ " " +newPosition+"");
                            m.setText(" ");
                            name.setText("What's Your Email");
                            name.setVisibility(View.VISIBLE);
                            m.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            y.setVisibility(View.GONE);
                            m.setVisibility(View.VISIBLE);
                            m.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    setEmail(s+"");
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            break;

                        case 2:
                            Log.e("case 2", currentPosition+""+ " " +newPosition+"");
                            name.setText("Let's get you Secured !");
                            y.setVisibility(View.GONE);
                            p.setVisibility(View.VISIBLE);
                            r.setVisibility(View.VISIBLE);
                            password.setVisibility(View.VISIBLE);
                            Reppass.setVisibility(View.VISIBLE);
                            name.setVisibility(View.VISIBLE);
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            finish.setVisibility(View.VISIBLE);
                            finish.setImageResource(R.drawable.download_1);

                            password.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    setPassword(s+"");
                                    Log.e("name", getPassword());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });

                            Reppass.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    setPasswordRepeat(s+"");
                                    Log.e("name", getPasswordRepeat());
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });


                finish.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!getPassword().equals(getPasswordRepeat()))
                        {
                            Toast.makeText(getApplicationContext(), "Password Do not match", Toast.LENGTH_LONG ).show();
                            return;
                        }
                        new AsyncTask<String, Void, Intent>() {

                            String accountEmail = getEmail();
                            String username = getUserName();
                            String accountPassword = getPassword();

                            ServerTask s = new ServerTask(Register.this, "");

                            @Override
                            protected Intent doInBackground(String... params) {

                                Log.d("REGISTER BRO", "Register Activity" + "> Started authenticating");

                                String authtoken = null;
                                Bundle data = new Bundle();
                                try {
                                    authtoken = s.UserSignUp(accountEmail, username, accountPassword);

                                    data.putString(AccountManager.KEY_ACCOUNT_NAME, accountEmail);
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
                                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_LONG).show();
                                } else {
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        }.execute();

                    }

                });
//
//            }
                        default :
                            Log.e("Default case", currentPosition+"" + " " +newPosition+"");
                            break;
                    }

                    currentPosition = newPosition;
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }

            });
            container.addView(view);
            return view;
        }
    }
}
