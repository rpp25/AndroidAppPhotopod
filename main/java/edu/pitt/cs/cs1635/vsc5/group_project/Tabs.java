package edu.pitt.cs.cs1635.vsc5.group_project;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Tabs extends TabActivity {

    String username = "";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("Capsules"); // Create a new TabSpec using tab host
        spec.setIndicator("CAPSULES"); // set the “HOME” as an indicator

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, Capsule.class);
        if (username.length() > 0) {
            intent.putExtra("username", username);
            System.out.println(username);
        }

        spec.setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs

        spec = tabHost.newTabSpec("Friends"); // Create a new TabSpec using tab host
        spec.setIndicator("FRIENDS"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, Friends.class);
        if (username.length() > 0) {
            intent.putExtra("username", username);
        }
        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Inbox"); // Create a new TabSpec using tab host
        spec.setIndicator("INBOX"); // set the “ABOUT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, Inbox.class);
        if (username.length() > 1) {
            intent.putExtra("username", username);
        }
        spec.setContent(intent);
        tabHost.addTab(spec);
        //set tab which one you want to open first time 0 or 1 or 2
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                // Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
