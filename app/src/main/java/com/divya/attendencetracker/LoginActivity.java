package com.divya.attendencetracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
                if(userid.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Provide UserId And Password\nThen click SignUP",Toast.LENGTH_LONG).show();
                }
                else{
                    validate_signup(userid.getText().toString().trim(),password.getText().toString().trim());
                }
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

        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
    }
    public void showAlert() {
        final boolean[] u1 = {false};
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Enter UserName");

        final EditText input = new EditText(LoginActivity.this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
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
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final String user;
                                showDialog();
                                user = input.getText().toString();

                                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
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
                                        hashMap.put("UserName", user);
                                        return hashMap;
                                    }
                                };
                                requestQueue.add(request);

                            }
                        });
                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
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
    public void validate_signup(final String Username, String Password) {
        if (Username.isEmpty() || Password.isEmpty())
            toast("Ensure All details are Correct");
        else showAlert();
    }

}
