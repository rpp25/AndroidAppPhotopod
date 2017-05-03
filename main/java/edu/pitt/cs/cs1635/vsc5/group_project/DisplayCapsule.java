package edu.pitt.cs.cs1635.vsc5.group_project;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class DisplayCapsule extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "https://photopod.000webhostapp.com/DisplayCapsules.php";
    private Map<String, String> params;

    // Register with both email and username, although one of them can be blank
    DisplayCapsule(String username, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}