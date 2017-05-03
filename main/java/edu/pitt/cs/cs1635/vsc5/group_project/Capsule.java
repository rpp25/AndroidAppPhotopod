package edu.pitt.cs.cs1635.vsc5.group_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Capsule extends AppCompatActivity {

    private final String IMAGE_REQUEST_URL = "https://photopod.000webhostapp.com/Image.php";
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    ListView list;
    private List<String> List_file;
    View selected;
    View last;
    Button add, share;
    final Context context = this;
    String username;
    String newCap;

    private String encodedString, imageName;
    private Bitmap bitmap;

    ArrayList<String> capsules = new ArrayList<>();
    ArrayList<String> selectedCapsules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule);
        Button photoButton = (Button) this.findViewById(R.id.add_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // @TODO: Check if a capsule is selected
                if(selectedCapsules != null) {
                    if(selectedCapsules.size() > 0) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        imageName = ((Long) System.currentTimeMillis()).toString() + ".bmp"; // Get unique string for the image name
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    } else {
                        Log.d("COLIN", "NO CAPSULE SELECTED!!!!");
                    }
                }
            }
        });
        selected = null;
        List_file = new ArrayList<String>();
        list = (ListView)findViewById(R.id.capsule_list);
        list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        add = (Button)findViewById(R.id.add_photo);
        share = (Button)findViewById(R.id.share);
        add.setEnabled(false);
        share.setEnabled(false);
        createListView();
    }

    public void newCapsule(View view) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.new_capsule, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                newCap = userInput.getText().toString();
                                //
                                // Add name to database
                                //
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            String test = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                                            JSONObject jsonResponse = new JSONObject(test);
                                            boolean success = jsonResponse.getBoolean("success");

                                            // If successful, go back to login activity and show the toast
                                            if(success) {
                                                Intent intent = new Intent(Capsule.this, Tabs.class);
                                                intent.putExtra("username", username);
                                                Capsule.this.startActivity(intent);

                                                Context context = getApplicationContext();
                                                CharSequence text = "Your new capsule has been added!";
                                                int duration = Toast.LENGTH_SHORT;
                                                Toast toast = Toast.makeText(context, text, duration);
                                                toast.show();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Capsule.this);
                                                builder.setMessage("Your new capsule couldn't be added!")
                                                        .setNegativeButton("Retry", null).create().show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                NewCapsule newCapsuleReq = new NewCapsule(username, newCap, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Capsule.this);
                                queue.add(newCapsuleReq);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            new EncodeImage().execute();
        }
    }

    public void toShare(View view) {
        Intent intent = new Intent(this, capsule_config.class);
        intent.putExtra("username", username);
        //@TODO: ADD FUNCTIONALITY TO GET THE SELECTED CAPSULE
        String capsule_name = selectedCapsules.get(0);
        intent.putExtra("capsule_name", capsule_name);
        startActivity(intent);
    }


    public void toSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void createListView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        //
        // Get capsule names from database
        //
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    for(int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject jsonObject = jsonResponse.getJSONObject(i);
                        String name = jsonObject.getString("capsule_name");
                        capsules.add(name);
                    }

                    String[] items = new String[capsules.size()];
                    for(int i = 0; i < capsules.size(); i++) {
                        items[i] = capsules.get(i);
                    }
                    ListView chl=(ListView) findViewById(R.id.capsule_list);
                    //set multiple selection mode
                    chl.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    //supply data itmes to ListView
                    ArrayAdapter<String> aa=new ArrayAdapter<String>(Capsule.this,R.layout.list_item, R.id.txt_title,items);
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
                                add.setEnabled(true);
                                share.setEnabled(true);
                            }
                        }

                    });

                    selectedCapsules = new ArrayList<>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DisplayCapsule display = new DisplayCapsule(username, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Capsule.this);
        queue.add(display);
    }

    private class EncodeImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
            byte[] array = stream.toByteArray();
            encodedString = Base64.encodeToString(array, 0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }

        private void makeRequest() {
            RequestQueue requestQueue = Volley.newRequestQueue(Capsule.this);
            StringRequest request = new StringRequest(Request.Method.POST, IMAGE_REQUEST_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //imageView.setImageBitmap(photo);
                            //Increase # of photos in capsule by 1
                            Intent intent = new Intent(Capsule.this, Tabs.class);
                            intent.putExtra("username", username);
                            startActivity(intent);

                            Context context = getApplicationContext();
                            CharSequence text = "Your photo has been added to the capsule!";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("encoded_string", encodedString);
                    map.put("image_name", imageName);
                    map.put("username", username);
                    map.put("capsule_name", selectedCapsules.get(0));
                    return map;
                }
            };

            requestQueue.add(request);
        }
    }


    /*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        ListActivity.onListItemClick(l, v, position, id);

        new AlertDialog.Builder(this)
                .setTitle("Hello")
                .setMessage("from " + getListView().getItemAtPosition(position))
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                .show();

        Toast.makeText(this,
                "ListView: " + l.toString() + "\n" +
                        "View: " + v.toString() + "\n" +
                        "position: " + String.valueOf(position) + "\n" +
                        "id: " + String.valueOf(id),
                Toast.LENGTH_LONG).show();
    } */
}
