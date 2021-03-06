package com.example.ramy.wayfare;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
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
import java.util.Locale;


public class ServerTask extends AsyncTask<String,Void,String> {

    static Context context;
    private AlertDialog alertDialog;
    private String user_url = "http://10.0.2.2:8000/auth/register/";
    private String login_url = "http://10.0.2.2:8000/auth/login/";
    private String profile_url = "http://10.0.2.2:8000/final/Profile/";
    private String profiles_url = "http://10.0.2.2:8000/final/Profiles/";
    private String imageup_url = "http://10.0.2.2:8000/final/uploadImage/";
    private String feed_url="http://10.0.2.2:8000/final/Feed/";
    private String addRegId_url = "http://10.0.2.2:8000/final/addRegId/";
    private String postText_url = "http://10.0.2.2:8000/final/postText/";
    private String editFollow_url = "http://10.0.2.2:8000/final/editFollow/";
    private String editLike_url = "http://10.0.2.2:8000/final/editLike/";
    private String getLikes_url = "http://10.0.2.2:8000/final/getLikes/";
    private String getPost_url = "http://10.0.2.2:8000/final/getPost/";
    private String editProfile_url = "http://10.0.2.2:8000/final/editProfile/";
    private String notifications_url = "http://10.0.2.2:8000/final/Notifications/";
    String result="";
    public String auth_token="";
    String token;
    ProgressDialog dialog;
    private Account[] mAccount;

    ServerTask(Context ctx, String tkn) {
        context = ctx;
        token=tkn;
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
                    HttpURLConnection httpURLConnection = setupConnection(url, "POST", false);
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
            case "imageup":
                try {
                    URL url = new URL(imageup_url);
                    File sourceFile = new File(params[1]);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bmp = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
                    JSONObject user = new JSONObject();
                    user.put("image", encodedImage);

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

    public HttpURLConnection setupConnection(URL url, String method, Boolean auth){
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (method.equals("POST")){
                httpURLConnection.setDoOutput(true);}
            httpURLConnection.setDoInput(true);
            if (auth){
                httpURLConnection.addRequestProperty("Authorization", "Token "+token);}
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod(method);
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

	public String UserSignIn(String email, String password) {
	    try{	 URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = setupConnection(url, "POST", false);
                    JSONObject user = new JSONObject();
                    user.put("email", email);
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

	public String UserSignUp(String email, String username, String password) {
		try{  URL url = new URL(user_url);
                    HttpURLConnection httpURLConnection = setupConnection(url, "POST", false);
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

    public boolean editProfile(String name, String username, String bio){
        try{
            URL url = new URL(editProfile_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
            JSONObject user = new JSONObject();
            user.put("name", name);
            user.put("username", username);
            user.put("bio", bio);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            if (result.equals("success"))
                return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean postText(String text, Location location) {
        try{
            Log.d("ramy","task");
            JSONObject user = new JSONObject();
            if (location!=null) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                String cityName = addresses.get(0).getAddressLine(0);
//                String countryName = addresses.get(0).getAddressLine(1);

//                Log.d("city", cityName);
//                Log.d("country", countryName);
                user.put("long", location.getLongitude());
                user.put("lat", location.getLatitude());
//                user.put("location", cityName + ", " + countryName);
            }
            URL url = new URL(postText_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
            user.put("text", text);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            if (result.equals("success"))
                return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String editFollow(String username) {
        try{
            URL url = new URL(editFollow_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
                JSONObject user = new JSONObject();
                user.put("username", username);

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
        return null;
    }

    public boolean editLike(String username, int postId) {
        try{
            URL url = new URL(editLike_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
            JSONObject user = new JSONObject();
            user.put("username", username);
            user.put("post_id", postId);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            if (result.equals("success"))
                return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getLikes(String username, int postId) {
        try{
            URL url = new URL(getLikes_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
            JSONObject user = new JSONObject();
            user.put("username", username);
            user.put("post_id", postId);

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

    public JSONArray getPost(int postId) {
        try{
            URL url = new URL(getPost_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
            JSONObject user = new JSONObject();
            user.put("post_id", postId);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            return new JSONArray().put(new JSONObject(result));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getNotifications(String username, String method){
        try{
            URL url = new URL(notifications_url);
            HttpURLConnection httpURLConnection = setupConnection(url, method, true);

            result = getResult(httpURLConnection);

            httpURLConnection.disconnect();

            return new JSONArray(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getProfile(String username, String method) {
        try{
            URL url = new URL(profile_url);
            HttpURLConnection httpURLConnection = setupConnection(url, method, true);
            if (method.equals("POST")){
            JSONObject user = new JSONObject();
            user.put("username", username);

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();
            }

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

    public JSONArray getProfiles(int id, String username, int postId, String method){
        try {
            URL url = new URL(profiles_url);
            HttpURLConnection httpURLConnection = setupConnection(url, method, true);
            if (method.equals("POST")) {
                JSONObject user = new JSONObject();
                user.put("id", id);
                user.put("username", username);
                user.put("post_id", postId);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();
            }

            String result = getResult(httpURLConnection);


            JSONArray obj = new JSONArray(result);

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

    public JSONArray getFeed(String username, boolean myfeed, boolean personal, int itemNumber){
        try {
            URL url = new URL(feed_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
                JSONObject user = new JSONObject();
                user.put("username", username);
                user.put("personal", personal);
                user.put("items", itemNumber);
                user.put("myfeed", myfeed);
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();

            String result = getResult(httpURLConnection);


            JSONArray obj = new JSONArray(result);

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

    public void addRegId(String reg_id_new, String reg_id_old) {
        try {
            URL url = new URL(addRegId_url);
            HttpURLConnection httpURLConnection = setupConnection(url, "POST", true);
                JSONObject user = new JSONObject();
                user.put("reg_id_new", reg_id_new);
                if (reg_id_old!=null)
                    user.put("reg_id_old", reg_id_old);
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();

            String result = getResult(httpURLConnection);
            Log.d("result",result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Account getStoredAccount(String c, AccountManager acc) { //send ApplicationContext as a parameter
        Log.e("ramy", "result0");
        Account[] accounts = null;
        String accountname = c;
        Account account = null;
        if(accountname != null) {
            Log.e("ramy", "resultnot");
            try {
            accounts = acc.getAccountsByType("com.example.ramy.wayfare");
                Log.e("ramy", "result1");
            } catch (SecurityException e) {
                Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
            }
            for(Account a : accounts) {
                if(a.name.toString().equalsIgnoreCase(accountname)) {
                    Log.e("test", a.toString());
                    account = a;
                }
            }
            }
        Log.e("ramy", "resultnull");
        return account;
    }

    public static String getAuthToken(AccountManager a, String c) {
        Log.e("ramy", "result4");
        final AccountManager accountManager = a;
//        String x = SavedPreference.getUserName(context);
//        Log.e("ramy", x);
        AccountManagerFuture<Bundle> future = accountManager.getAuthToken(getStoredAccount(c, a),
                AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, true, null, null);
        Bundle result = null;
        Log.e("ramy", "result");
        try {
            result = future.getResult();
            Log.e("ramy", "result");

        } catch (Exception e) {
            Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
            toast.show();
        }
        String authToken = null;
        if (future.isDone() && !future.isCancelled()) {
            if (result.containsKey(AccountManager.KEY_INTENT)) {
                Log.e("test", "7");
                Intent intent = result.getParcelable(AccountManager.KEY_INTENT);
            }
            authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
        }
//        if (authToken == null) {
//            Toast toast = Toast.makeText(context, "problem with authentication", Toast.LENGTH_LONG);
//            toast.show();
//        }

        return authToken;
    }

}