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
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Button login, signup;
    EditText userid, password,username,classid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userid = findViewById(R.id.userid);
        password = findViewById(R.id.pass);
        username = findViewById(R.id.username);
        classid = findViewById(R.id.classid);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userid.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || classid.getText().toString().isEmpty() )
                    toast("Enter all details");
                else{
                    final boolean[] u1 = {false};
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://herbiest-shelf.000webhostapp.com/student_login.php/", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideDialog();
                            try {
                                JSONObject obj = new JSONObject(response);
                                JSONArray jsonArray = obj.getJSONArray("Details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    if (userid.getText().toString().equals(o.getString("User_ID")))
                                    {
                                        u1[0] = true;
                                        break;
                                    }
                                }
                                if(!u1[0]){
                                            showDialog();
                                            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                                            StringRequest request = new StringRequest(Request.Method.POST, "https://herbiest-shelf.000webhostapp.com/student_register.php/", new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    hideDialog();
                                                    toast("Successfully Registered");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    toast(error.getMessage());
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("UserID", userid.getText().toString());
                                                    hashMap.put("password", password.getText().toString());
                                                    hashMap.put("UserName", username.getText().toString());
                                                    hashMap.put("ClassID", classid.getText().toString());
                                                    return hashMap;
                                                }
                                            };
                                            requestQueue.add(request);
                                }
                                else toast("UserId unAvailable");

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
        });
    }
    public void showDialog() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Validating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Progress Dialog");
        progressDialog.show();
    }
    public void hideDialog() {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }
    public void toast(String Message) {
        Toast.makeText(RegisterActivity.this,Message,Toast.LENGTH_SHORT).show();
    }
    public void volley(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);
    }

}
