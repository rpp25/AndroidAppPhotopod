package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    // Array of strings...
    String[] mobileArray = {"User Info","Help","Log Out"};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listView = (ListView) findViewById(R.id.settings_list);
        String[] values = new String[]{"User Info" ,"Help", "Log Out"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    Intent myIntent = new Intent(view.getContext(), UserInfo.class);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 1) {
                    Intent myIntent = new Intent(view.getContext(), Help.class);
                    startActivityForResult(myIntent, 0);
                }

                if (position == 2) {
                    Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                    startActivityForResult(myIntent, 0);
                    Context context = getApplicationContext();
                    CharSequence text = "You have been logged out!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }
}