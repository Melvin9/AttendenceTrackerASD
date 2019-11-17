package com.divya.attendencetracker.ui.home;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.divya.attendencetracker.LoginActivity;
import com.divya.attendencetracker.MainActivity;
import com.divya.attendencetracker.R;
import com.divya.attendencetracker.RegisterActivity;
import com.divya.attendencetracker.SubjectAdapter;
import com.divya.attendencetracker.SubjectData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    ProgressDialog progressDialog;
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<SubjectData> data_subject;
    String weekday_name;
    Button update;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        update=root.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                for(int i=0;i<SubjectAdapter.c;i++){
                    checkUpdated(SubjectAdapter.subjects[i]);

                }
            }
        });
        weekday_name = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(weekday_name+", "+ date_n);
        recyclerView=root.findViewById(R.id.rv);
        layoutManager=new GridLayoutManager(getActivity(),1);
        data_subject=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        loadFromServer();
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }
    public void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Progress Dialog");
        progressDialog.show();
    }
    public void hideDialog() {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }
    public void checkUpdated(final String sub) {

        java.util.Date date=new java.util.Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String dateString = df.format(date);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://herbiest-shelf.000webhostapp.com/student_login.php/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        if (LoginActivity.UserID.equals(o.getString("User_ID")) && !dateString.equals(o.getString("Date")) )
                        {
                            final int[] track = {0};
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://herbiest-shelf.000webhostapp.com/student_percentage.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        JSONArray jsonArray = obj.getJSONArray("Details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject o = jsonArray.getJSONObject(i);
                                            if (LoginActivity.UserID.equals(o.getString("User_ID")) && sub.equals(o.getString("SubjectID")) )
                                            {   track[0] =1;
                                            toast("updated");
                                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                                StringRequest request = new StringRequest(Request.Method.POST, "https://herbiest-shelf.000webhostapp.com/student_attendence_update.php", new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        hideDialog();
                                                        toast("Updated");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        hideDialog();
                                                        toast(error.getMessage());
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() {
                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("UserID", LoginActivity.UserID);
                                                        hashMap.put("SubID",sub);
                                                        hashMap.put("Date",dateString);
                                                        return hashMap;
                                                    }
                                                };
                                                requestQueue.add(request);

                                                break;
                                            }
                                            else track[0]=0;
                                        }
                                        if(track[0]==0){
                                            toast("inserted");
                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                            StringRequest request = new StringRequest(Request.Method.POST, "https://herbiest-shelf.000webhostapp.com/student_attendence_insert.php", new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    hideDialog();
                                                    toast("Updated");
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    hideDialog();
                                                    toast(error.getMessage());
                                                }
                                            }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("UserID", LoginActivity.UserID);
                                                    hashMap.put("SubID",sub);
                                                    hashMap.put("Date",dateString);
                                                    return hashMap;
                                                }
                                            };
                                            requestQueue.add(request);
                                        }
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
                            break;
                        }
                        if (LoginActivity.UserID.equals(o.getString("User_ID")) && dateString.equals(o.getString("Date")) )
                        {   hideDialog();
                            toast("Already Updated");
                        }

                    }
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
    public void toast(String Message) {
        Toast.makeText(getActivity(),Message,Toast.LENGTH_SHORT).show();
    }
    public void volley(StringRequest stringRequest) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadFromServer() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Details!!!");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, "https://herbiest-shelf.000webhostapp.com/subjects.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("details");
                    for(int i=0;i<=jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        data_subject.add(new SubjectData(o.getString("SUB1")));
                        data_subject.add(new SubjectData(o.getString("SUB2")));
                        data_subject.add(new SubjectData(o.getString("SUB3")));
                        data_subject.add(new SubjectData(o.getString("SUB4")));
                        data_subject.add(new SubjectData(o.getString("SUB5")));
                        data_subject.add(new SubjectData(o.getString("SUB6")));
                        data_subject.add(new SubjectData(o.getString("SUB7")));
                        adapter=new SubjectAdapter(data_subject,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("day", weekday_name);
                hashMap.put("classID",LoginActivity.ClassId);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
    }
