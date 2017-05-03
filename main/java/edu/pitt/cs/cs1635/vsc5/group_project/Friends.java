package edu.pitt.cs.cs1635.vsc5.group_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Friends extends AppCompatActivity {

    ListView list;

    private String currentUsername;
    private String friendToAdd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        list = (ListView)findViewById(R.id.friends_list);
        //list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        updateListView();
    }


    public void updateListView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
        }

        final ArrayList<String> friends = new ArrayList<>();

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

                    //list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, List_file));
                    list.setAdapter(new ArrayAdapter< String >(Friends.this, android.R.layout.simple_list_item_1, friends));
                    list.setTextFilterEnabled(true);
//                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            if(selected != null)
//                            {
//                                last = selected;
//                                last.setBackgroundColor(Color.parseColor("#ffffff"));
//                            }
//                            add.setEnabled(true);
//                            share.setEnabled(true);
//                            selected = view;
//
//                            selected.setBackgroundColor(Color.parseColor("#BCBDB7"));
//                        }
//
//                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DisplayFriends display = new DisplayFriends(currentUsername, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Friends.this);
        queue.add(display);
    }

    public void addFriendButtonListener(View view) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(Friends.this);
        View promptsView = li.inflate(R.layout.add_friend, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Friends.this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.addFriendTextInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                friendToAdd = userInput.getText().toString();

                                //
                                // Check if username exists
                                //
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");

                                            if(friendToAdd.equals(currentUsername)) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Friends.this);
                                                builder.setMessage("You can't add yourself!")
                                                        .setNegativeButton("OK", null).create().show();
                                            } else if(success) {
                                                addFriendToDatabase(friendToAdd);
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Friends.this);
                                                builder.setMessage("No users with this username were found!")
                                                        .setNegativeButton("OK", null).create().show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                GetUser request = new GetUser(friendToAdd, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Friends.this);
                                queue.add(request);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void addFriendToDatabase(String slaveUsername)
    {
        if(slaveUsername != friendToAdd)
        {
            Log.d("COLIN", "THIS SHOULD NEVER HAPPEN!!!");
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
        }

        //
        // Add friend pair
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        Context context = getApplicationContext();
                        CharSequence text = friendToAdd + " has been added to your friends list!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        updateListView();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Friends.this);
                        builder.setMessage("Error adding friend to database. Sorry :(")
                                .setNegativeButton("OK", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        AddFriendToDatabase request = new AddFriendToDatabase(currentUsername, friendToAdd, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Friends.this);
        queue.add(request);
    }
}
