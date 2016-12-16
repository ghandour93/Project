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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.net.URL;
import java.net.URLEncoder;

import static android.R.attr.bitmap;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class HttpAsyncTask extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;

    HttpAsyncTask(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String fetch_url = "http://10.0.2.2:8000/final/simplecheck/";
       // String user_url = "http://10.0.2.2:8000/user/register/";
        String user_url = "http://10.0.2.2:8000/auth/register/";
        String image_url = "http://10.0.2.2:8000/user/imageup/";
        String imagedown_url="";
        if(params[0].equals("register")) {
            try {
                String username = params[1];
                String password = params[2];
                String displayname = params[3];
                URL url = new URL(user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestMethod("POST");
                JSONObject user = new JSONObject();
                user.put("email", username);
                user.put("password", password);
                //user.put("displayname", displayname);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (params[0].equals("fetch")){
            try {
                String username = params[1];
                URL url = new URL(fetch_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestMethod("POST");
                JSONObject user = new JSONObject();
                user.put("username", username);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
               /* JSONArray jarray = new JSONArray(result);
                JSONObject json_data = jarray.getJSONObject(0);
                result=json_data.getString("username") + "\n" +
                        json_data.getString("password") + "\n" +
                        json_data.getString("displayname");*/
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (params[0].equals("imageup")) {
            try {
                String fileName = params[1];
//                DataOutputStream dos = null;
//                String lineEnd = "\r\n";
//                String twoHyphens = "--";
//                String boundary = "*****";
//                int bytesRead, bytesAvailable, bufferSize;
//                byte[] buffer;
//                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(params[1]);
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bmp = BitmapFactory.decodeFile(sourceFile.getAbsolutePath());
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

//                FileInputStream fis = new FileInputStream(sourceFile);
                URL url = new URL(image_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
//                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
//                httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                httpURLConnection.setRequestProperty("uploaded_file", fileName);
//                httpURLConnection.setRequestMethod("POST");

//                dos = new DataOutputStream(httpURLConnection.getOutputStream());
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename="+ fileName + lineEnd);
//                dos.writeBytes(lineEnd);
//
//                // create a buffer of  maximum size
//                bytesAvailable = fis.available();
//
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//
//                // read file and write it into form...
//                bytesRead = fis.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fis.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fis.read(buffer, 0, bufferSize);
//
//                }
//
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//                String result = "";
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    result += line;
//                }
//                bufferedReader.close();
//                inputStream.close();
//                fis.close();
//                dos.flush();
//                dos.close();
//                httpURLConnection.disconnect();
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
////            }
            }

        }else if(params[0].equals("imagedown")){
            try {
                String username = params[1];
                URL url = new URL(imagedown_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestMethod("POST");
                JSONObject user = new JSONObject();
                user.put("username", username);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(user.toString());
                wr.flush();


                InputStream inputStream = (InputStream) httpURLConnection.getContent();
                Bitmap pic = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}