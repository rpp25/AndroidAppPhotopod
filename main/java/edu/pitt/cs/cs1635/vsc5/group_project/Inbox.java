package edu.pitt.cs.cs1635.vsc5.group_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Inbox extends AppCompatActivity {
    // Array of strings...
    ListView listView;
    String currentUsername;
    ArrayList<Capsule> capsules = new ArrayList<>();
    ArrayList<String> selectedCapsules;
    HashMap<String, Capsule> detailsToCapsules = new HashMap<String, Capsule>();
    Button unlockButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        displayCapsules();
        unlockButton = (Button)findViewById(R.id.unlockButton);
        unlockButton.setEnabled(false);
    }
    private void displayCapsules() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentUsername = extras.getString("username");
        }

        //
        // Get capsules from DB
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    System.out.println(response);
                    for(int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject jsonObject = jsonResponse.getJSONObject(i);
                        // @TODO can move code around to make it easier for the next step
                        String capsule_owner = jsonObject.getString("capsule_owner");
                        String capsule_name = jsonObject.getString("capsule_name");
                        String time_to_open = jsonObject.getString("time_to_open");
                        String question = jsonObject.getString("question");
                        String answer = jsonObject.getString("answer");
                        String choice1 = jsonObject.getString("choice1");
                        String choice2 = jsonObject.getString("choice2");
                        String choice3 = jsonObject.getString("choice3");

                        int hours = Integer.parseInt(time_to_open);
                        int days = hours / 24;
                        int moreHours = hours % 24;
                        String capsule_details = capsule_name + " from " + capsule_owner + ": " + days + " days and " + moreHours + " hours";
                        Capsule capsule = new Capsule(capsule_owner, capsule_name, time_to_open, capsule_details, question, answer, choice1, choice2, choice3);
                        capsules.add(capsule);
                    }

                    String[] items = new String[capsules.size()];
                    for(int i = 0; i < capsules.size(); i++) {
                        items[i] = capsules.get(i).displayString;
                        detailsToCapsules.put(items[i], capsules.get(i));
                    }
                    ListView chl=(ListView) findViewById(R.id.inbox_list);
                    //set multiple selection mode
                    chl.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    //supply data itmes to ListView
                    ArrayAdapter<String> aa = new ArrayAdapter<String>(Inbox.this,R.layout.list_item, R.id.txt_title,items);
                    chl.setAdapter(aa);
                    //set OnItemClickListener
                    chl.setOnItemClickListener(new OnItemClickListener(){
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // selected item
                            String selectedItem = ((TextView) view).getText().toString();
                            if(selectedCapsules.contains(selectedItem)) {
                                selectedCapsules.remove(selectedItem); //remove deselected item from the list of selected items
                            } else {
                                selectedCapsules.add(selectedItem); //add selected item to the list of selected items
                                unlockButton.setEnabled(true);
                            }
                        }

                    });
                    selectedCapsules = new ArrayList<>();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ReceivedCapsules received = new ReceivedCapsules(currentUsername, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Inbox.this);
        queue.add(received);
    }

    public void toUnlock(View view){
        Capsule selected = detailsToCapsules.get(selectedCapsules.get(0));
        Intent intent = new Intent(this, Unlock.class);
        intent.putExtra("username", currentUsername);
        intent.putExtra("capsuleOwner", selected.capsuleOwner);
        intent.putExtra("capsuleName", selected.capsuleName);
        intent.putExtra("question", selected.question);
        intent.putExtra("answer", selected.answer);
        intent.putExtra("choice1", selected.choice1);
        intent.putExtra("choice2", selected.choice2);
        intent.putExtra("choice3", selected.choice3);
        startActivity(intent);
    }

    private class Capsule {
        String capsuleOwner;
        String capsuleName;
        String timeToOpen;
        String displayString;

        String question;
        String answer;
        String choice1;
        String choice2;
        String choice3;

        public Capsule(String owner, String name, String t, String dS,
                       String q, String a, String c1, String c2, String c3)
        {
            capsuleOwner = owner;
            capsuleName = name;
            timeToOpen = t;
            displayString = dS;
            question = q;
            answer = a;
            choice1 = c1;
            choice2 = c2;
            choice3 = c3;
        }
    }
}
