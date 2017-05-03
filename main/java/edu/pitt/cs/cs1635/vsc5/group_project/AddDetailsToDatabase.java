package edu.pitt.cs.cs1635.vsc5.group_project;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class AddDetailsToDatabase extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://photopod.000webhostapp.com/AddDetailsToDatabase.php";
    private Map<String, String> params;

    // Register with both email and username, although one of them can be blank
    AddDetailsToDatabase(String username, String capsule_name, String capsuleOwner, int timeToOpen, String question, String correct, String incorrect1, String incorrect2, String incorrect3, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("capsule_name", capsule_name);
        params.put("capsule_owner", capsuleOwner);
        params.put("time_to_open", Integer.toString(timeToOpen));
        params.put("question", question);
        params.put("correct", correct);
        params.put("incorrect1", incorrect1);
        params.put("incorrect2", incorrect2);
        params.put("incorrect3", incorrect3);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}