package com.example.ramy.wayfare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Fetch extends AppCompatActivity {

    EditText editText5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        editText5= (EditText) findViewById(R.id.editText5);
    }

    public void OnFet(View view) {
        String username = editText5.getText().toString();
        String type = "fetch";
        //BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        //HttpAsyncTask backgroundWorker = new HttpAsyncTask(this);
        //backgroundWorker.execute(type, username);
        ServerTask serverTask = new ServerTask(this);
        serverTask.execute("fetch");
    }
}
