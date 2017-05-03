package edu.pitt.cs.cs1635.vsc5.group_project;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    //private static final String LOGIN_REQUEST_URL = "http://cs1635macrosoft.atwebpages.com/Login.php";
    private static final String LOGIN_REQUEST_URL = "https://photopod.000webhostapp.com/Login.php";
    private Map<String, String> params;

    // Login with username and password. Possibly support email later?
    LoginRequest(String username, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
