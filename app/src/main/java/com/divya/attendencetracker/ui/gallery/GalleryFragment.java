package com.divya.attendencetracker.ui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.divya.attendencetracker.AttendenceAdapter;
import com.divya.attendencetracker.AttendenceData;
import com.divya.attendencetracker.LoginActivity;
import com.divya.attendencetracker.R;
import com.divya.attendencetracker.SubjectAdapter;
import com.divya.attendencetracker.SubjectData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<AttendenceData> attendenceData;
    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView=root.findViewById(R.id.rv1);
        layoutManager=new GridLayoutManager(getActivity(),1);
        attendenceData=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Percentage");
        loadFromServer();
        recyclerView.setLayoutManager(layoutManager);
        return root;


    }
    private void loadFromServer() {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Details!!!");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, "https://herbiest-shelf.000webhostapp.com/get_attendence.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("details");
                    for(int i=0;i<=jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        attendenceData.add(new AttendenceData(o.getString("SubjectID"),o.getString("SUBNAME"),o.getInt("Percentage"),o.getInt("Tot")));
                        adapter=new AttendenceAdapter(attendenceData,getActivity());
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
                hashMap.put("UID", LoginActivity.UserID);
                return hashMap;
            }
        };
        requestQueue.add(request);
    }
}