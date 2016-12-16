package com.example.ramy.wayfare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class List extends AppCompatActivity {

    String item;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        b=new Bundle();

        final ListView listView = (ListView) findViewById(R.id.listview);

        b = getIntent().getExtras();
        ArrayList<String> following = b.getStringArrayList("following");
        ArrayList<String> followers = b.getStringArrayList("followers");

        String[] values = new String[] { "Android Example ListActivity", "Adapter implementation", "Simple List View With ListActivity",
                "ListActivity Android", "Android Example", "ListActivity Source Code", "ListView ListActivity Array Adapter", "Android Example ListActivity" };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third - the Array of data
        ArrayAdapter<String> adapter;
    if (b.getInt("id")==1) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, following);
    }else {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, followers);
    }

        // Assign adapter to List
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item=(String) listView.getItemAtPosition(position);
                b.clear();
                b.putString("userprofile", item);
                b.putBoolean("notmine", true);
                Intent intent = new Intent(List.this, Profile.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
