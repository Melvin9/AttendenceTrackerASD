package com.divya.attendencetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private boolean u=true,pa=true,p=true;
    ProgressDialog progressDialog;
    Button login, signup;
    EditText userid, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userid = findViewById(R.id.username);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                validate_login(userid.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }

    public void success() {

        Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
    }
    public void showDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Validating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Progress Dialog");
        progressDialog.show();
    }
    public void hideDialog() {
        if(progressDialog!=null)
        progressDialog.dismiss();
    }
    public void usernameError() {
        userid.setError("Wrong UserName");
    }
    public void passwordError() {
        password.setError("Wrong Password");
    }
    public void toast(String Message) {
        Toast.makeText(LoginActivity.this,Message,Toast.LENGTH_SHORT).show();
    }
    public void volley(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
    public void validate_login(final String Username, final String Password) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://herbiest-shelf.000webhostapp.com/student_login.php/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        if (Username.equals(o.getString("User_ID"))) u = false;
                        if (Username.equals(o.getString("User_ID")) && Password.equals(o.getString("Password")))
                            pa = false;
                        if (Username.equals(o.getString("User_ID")) && Password.equals(o.getString("Password"))) {
                            p = false;
                            success();
                            break;
                        } else p = true;
                    }
                    if (p) toast("Login Unsuccessful");
                    if (u) usernameError();
                    if (pa) passwordError();
                } catch (JSONException e) {
                    toast(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                toast(error.getMessage());
            }
        });
        volley(stringRequest);
    }

}
