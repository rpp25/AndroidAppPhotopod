package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectFriends extends AppCompatActivity {

    String currentUsername;
    String capsule_name;
    String question;
    String correct;
    String incorrect1;
    String incorrect2;
    String incorrect3;
    int totalTime;
    ArrayList<String> friends = new ArrayList<>();
    ArrayList<String> selectedFriends;
    ArrayList<String> failed = new ArrayList<>();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);
        displayFriends();
    }

    private void displayFriends() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
        }

        //
        // Get friend's usernames from DB
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    for(int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject jsonObject = jsonResponse.getJSONObject(i);
                        String name = jsonObject.getString("slave_username");
                        friends.add(name);
                    }

                    String[] items = new String[friends.size()];
                    for(int i = 0; i < friends.size(); i++) {
                        items[i] = friends.get(i);
                    }
                    ListView chl=(ListView) findViewById(R.id.checkable_list);
                    //set multiple selection mode
                    chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    //supply data itmes to ListView
                    ArrayAdapter<String> aa=new ArrayAdapter<String>(SelectFriends.this,R.layout.list_item, R.id.txt_title,items);
                    chl.setAdapter(aa);
                    //set OnItemClickListener
                    chl.setOnItemClickListener(new OnItemClickListener(){
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // selected item
                            String selectedItem = ((TextView) view).getText().toString();
                            if(selectedFriends.contains(selectedItem))
                                selectedFriends.remove(selectedItem); //remove deselected item from the list of selected items
                            else
                                selectedFriends.add(selectedItem); //add selected item to the list of selected items

                        }

                    });

                    selectedFriends = new ArrayList<>();

                    //
                    // TODO: All of the friends will be in the 'friends' list, so you need to display it and such.
                    //

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DisplayFriends display = new DisplayFriends(currentUsername, responseListener);
        RequestQueue queue = Volley.newRequestQueue(SelectFriends.this);
        queue.add(display);
    }

    public void backHome(View view) {
        //Get all the info from previous activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
            capsule_name = extras.getString("capsule_name");
            question = extras.getString("question");
            correct = extras.getString("correct");
            incorrect1 = extras.getString("incorrect1");
            incorrect2 = extras.getString("incorrect2");
            incorrect3 = extras.getString("incorrect3");
            totalTime = extras.getInt("time_to_open");
        }
        count = 0;
        //
        // Add to deets database
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String test = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                    JSONObject jsonResponse = new JSONObject(test);
                    boolean success = jsonResponse.getBoolean("success");
                    count++;

                    if(!success) {
                        failed.add(selectedFriends.get(count - 1));
                        System.out.println(failed.size());
                    }

                    if(count == selectedFriends.size()) {
                        if(failed.size() == 0) {
                            Intent intent = new Intent(SelectFriends.this, Tabs.class);
                            intent.putExtra("username", currentUsername);
                            startActivity(intent);
                            Context context = getApplicationContext();
                            CharSequence text = "Capsule shared!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        } else {
                            String failedFriends = "";
                            for (int i = 0; i < failed.size(); i++) {
                                failedFriends = failedFriends.concat(failed.get(i));
                                if (i != failed.size() - 1) {
                                    failedFriends = failedFriends.concat(", ");
                                }
                            }
                            Intent intent = new Intent(SelectFriends.this, Tabs.class);
                            intent.putExtra("username", currentUsername);
                            startActivity(intent);
                            Context context = getApplicationContext();
                            CharSequence text = "You've already shared your capsule with " + failedFriends + "! The capsule was sent to everybody else!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        for(int i = 0; i < selectedFriends.size(); i++) {
            AddDetailsToDatabase addDetails = new AddDetailsToDatabase(selectedFriends.get(i), capsule_name, currentUsername, totalTime, question, correct, incorrect1, incorrect2, incorrect3, responseListener);
            RequestQueue queue = Volley.newRequestQueue(SelectFriends.this);
            queue.add(addDetails);
        }
    }

    public void backCapsuleConfig(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
        }
        Intent intent = new Intent(this, capsule_config.class);
        intent.putExtra("username", currentUsername);
        startActivity(intent);
    }
}
