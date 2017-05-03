package edu.pitt.cs.cs1635.vsc5.group_project;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class RegisterRequest extends StringRequest {

    //private static final String REGISTER_REQUEST_URL = "http://cs1635macrosoft.atwebpages.com/Register.php";
    private static final String REGISTER_REQUEST_URL = "https://photopod.000webhostapp.com/Register.php";
    private Map<String, String> params;

    // Register with both email and username, although one of them can be blank
    RegisterRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}