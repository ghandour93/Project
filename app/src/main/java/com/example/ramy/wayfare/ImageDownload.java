package com.example.ramy.wayfare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class ImageDownload extends AsyncTask<String,Void,Bitmap> {
    Context context;
    AlertDialog alertDialog;
    ImageView imgview;

    ImageDownload(Context ctx, ImageView view) {
        context = ctx;
        imgview = view;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imagedown_url = "";
        try {
            URL url = new URL(imagedown_url);
            HttpURLConnection httpURLConnection = setupConnection(url);
            JSONObject user = new JSONObject();

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(user.toString());
            wr.flush();

            String result = getResult(httpURLConnection);


            JSONObject obj = new JSONObject(result);
            String image = obj.getString("encoded_file");
            Log.i("image", image);
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            httpURLConnection.disconnect();
            return decodedByte;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imgview.setImageBitmap(result);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    public HttpURLConnection setupConnection(URL url) {
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


    public String getResult(HttpURLConnection httpURLConnection) {
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
}