package com.example.employeesdetailapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.employeesdetailapp.Adapter.MyRecyclerViewAdapter;
import com.example.employeesdetailapp.EmployInfo;
import com.example.employeesdetailapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String Url = "http://dummy.restapiexample.com/api/v1/employees";
    private RecyclerView RCData;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    ArrayList<EmployInfo> list;
    private Object Context;
    private android.database.Cursor Cursor;
    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RCData = findViewById(R.id.id_RecycleView);

        list = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter(getApplicationContext(), list);
        linearLayoutManager=new LinearLayoutManager(this);
        RCData.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(RCData.getContext(), linearLayoutManager.getOrientation());

        RCData.setHasFixedSize(true);
        RCData.setLayoutManager(linearLayoutManager);
        RCData.addItemDecoration(dividerItemDecoration);
        RCData.setAdapter(adapter);
        getData();

    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loader......");
        progressDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        EmployInfo employInfo = new EmployInfo();
                        employInfo.setName(jsonObject.getString("employee_name"));
                        list.add(employInfo);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
