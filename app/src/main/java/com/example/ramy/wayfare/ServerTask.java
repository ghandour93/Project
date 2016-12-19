package com.example.ramy.wayfare;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class ServerTask extends AsyncTask<String,Void,String> {

    static Context context;
    private AlertDialog alertDialog;
    private String fetch_url = "http://10.0.2.2:8000/auth/me/";
    private String user_url = "http://10.0.2.2:8000/auth/register/";
    private String login_url = "http://10.0.2.2:8000/auth/login/";
    private String image_url = "http://10.0.2.2:8000/user/imageup/";
    private String check_url = "http://10.0.2.2:8000/final/simplecheck/";
    private String profile_url = "http://10.0.2.2:8000/final/getProfile/";
    private String imageup_url = "http://10.0.2.2:8000/final/uploadImage/";
    private String getfeed_url="http://10.0.2.2:8000/final/getFeed/";
    String result="";
    public static final String LOG_TAG = "myLogs";
    public String auth_token="";
    private Account[] mAccount;

    ServerTask(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        switch (params[0]){
            case "register":
                try {
                    String username = params[1];
                    String password = params[2];
                    String displayname = params[3];
                    URL url = new URL(user_url);
                    HttpURLConnection httpURLConnection = setupConnection(url);
                    JSONObject user = new JSONObject();
                    user.put("email", username);
                    user.put("password", password);
                    //user.put("displayname", displayname);

                    OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    wr.write(user.toString());
                    wr.flush();

                    result = getResult(httpURLConnection);

                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "login":
                try {
                    String username = params[1];
                    String password = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = setupConnection(url);
                    JSONObject user = new JSONObject();
                    user.put("email", username);
                    user.put("password", password);

                    OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    wr.write(user.toString());
                    wr.flush();

                    result = getResult(httpURLConnection);

                    httpURLConnection.disconnect();

                    JSONObject json_data = new JSONObject(result);
                    auth_token = json_data.getString("auth_token");
                    url = new URL(fetch_url);
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url.openConnection();
                    httpURLConnection1.setDoInput(true);
                    httpURLConnection1.addRequestProperty("Authorization", "Token "+auth_token);
                    httpURLConnection1.setRequestMethod("GET");
                    Log.i(LOG_TAG, auth_token);

                    result=getResult(httpURLConnection1);

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "fetch":
                try {
                    URL url = new URL(check_url);
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url.openConnection();
                    httpURLConnection1.setDoInput(true);
                    httpURLConnection1.addRequestProperty("Authorization", "Token 7e0c8166bd2f5233cb6cbaf55462a14a60be77b4");
                    httpURLConnection1.setRequestMethod("GET");



                    result=getResult(httpURLConnection1);

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case "imageup":
                try {
                    URL url = new URL(imageup_url);
                    File sourceFile = new File(params[1]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bmp = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    HttpURLConnection httpURLConnection = setupConnection(url);
                    JSONObject user = new JSONObject();
                    user.put("image", encodedImage);
                    Log.i("image", encodedImage);
                    //user.put("displayname", displayname);

                    OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    wr.write(user.toString());
                    wr.flush();

                    result = getResult(httpURLConnection);

                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case "imagedown":

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public HttpURLConnection setupConnection(URL url){
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    }
        return httpURLConnection;
    }


    public String getResult(HttpURLConnection httpURLConnection){
        InputStream inputStream = null;
        String result = "";
        String line = "";
        try {
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

	public String UserSignIn(String username, String password)
	{
	    try{	 URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = setupConnection(url);
                    JSONObject user = new JSONObject();
                    user.put("email", username);
                    user.put("password", password);

                    OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    wr.write(user.toString());
                    wr.flush();

                    result = getResult(httpURLConnection);

                    httpURLConnection.disconnect();

                    JSONObject json_data = new JSONObject(result);
                    auth_token = json_data.getString("auth_token");

					return auth_token;
        } catch (MalformedURLException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
        }
        return null;
	}

	public String UserSignUp(String email, String username, String password)
	{
		try{  URL url = new URL(user_url);
                    HttpURLConnection httpURLConnection = setupConnection(url);
                    JSONObject user = new JSONObject();
                    user.put("email", email);
                    user.put("username", username);
                    user.put("password", password);

                    OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    wr.write(user.toString());
                    wr.flush();

                    result = getResult(httpURLConnection);

                    httpURLConnection.disconnect();
					return UserSignIn(email, password);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    return null;
	}

    public JSONObject getProfile(String username)
    {
        try{
            URL url = new URL(profile_url);
            HttpURLConnection httpURLConnection = setupConnection(url);
            JSONObject user = new JSONObject();
            user.put("username", username);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            return new JSONObject(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getFeed(String username){
        try {
            URL url = new URL(getfeed_url);
            HttpURLConnection httpURLConnection = setupConnection(url);
            JSONObject user = new JSONObject();
            user.put("username", username);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            String result = getResult(httpURLConnection);


            JSONArray obj = new JSONArray(result);
//            String image = obj.getString("encoded_file");
//            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            httpURLConnection.disconnect();
            return obj;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getStoredAccount(String c) { //send ApplicationContext as a parameter
        Account[] accounts = null;
        String accountname = c;
        AccountManager accountManager = AccountManager.get(context);
        Account account = null;
        if(accountname != null) {
            Log.e("yahia","yahia");
            try {
            accounts = accountManager.getAccountsByType("com.example.ramy.myapplication");
            } catch (SecurityException e) {
                Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
            }
            for(Account a : accounts) {
                Log.e("yahia",a.name.toString());

                if(a.name.toString().equalsIgnoreCase(accountname)) {
                    Log.e("yahia","yahia123");
                    account = a;
                }
            }
            }
        return account;
    }

    @SuppressWarnings("deprecation")
    public static String getAuthToken(AccountManager a, String c) {
//yahia
        final AccountManager accountManager = a;
        AccountManagerFuture<Bundle> future = accountManager.getAuthToken(getStoredAccount(c),
                AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true, null, null);
        Bundle result = null;
        try {
            Log.e("yahia", "samir el bedan");
            result = future.getResult();
            Log.e("yahia", result.toString());
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
            toast.show();
        }
        String authToken = null;
        Log.e("yahia", "samir el bedan2");
        if (future.isDone() && !future.isCancelled()) {
            Log.e("yahia", "samir el bedan3");
            if (result.containsKey(AccountManager.KEY_INTENT)) {
                Log.e("yahia", "samir el bedan4");
                Intent intent = result.getParcelable(AccountManager.KEY_INTENT);
            }
            Log.e("yahia", "samir el bedan5");
            authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
        }
        Log.e("yahia", "samir el bedan6");
        if (authToken == null) {
            Log.e("yahia", "samir el bedan7");
            Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
            toast.show();
        }

        return authToken;
    }

}