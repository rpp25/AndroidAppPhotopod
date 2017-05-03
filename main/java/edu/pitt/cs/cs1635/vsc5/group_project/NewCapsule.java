package edu.pitt.cs.cs1635.vsc5.group_project;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class NewCapsule extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://photopod.000webhostapp.com/Capsule.php";
    private Map<String, String> params;

    // Register with both email and username, although one of them can be blank
    NewCapsule(String username, String capsule_name, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("capsule_name", capsule_name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}