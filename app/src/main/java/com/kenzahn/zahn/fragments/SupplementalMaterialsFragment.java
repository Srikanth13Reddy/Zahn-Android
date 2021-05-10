package com.kenzahn.zahn.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.adapter.SupplementAdapter;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.SupplementModel;
import com.kenzahn.zahn.rest.SaveImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupplementalMaterialsFragment extends Fragment implements SaveView
{

    private View progressLayout;
    private RecyclerView rv_supplementals;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public SupplementalMaterialsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_supplemental_materials, container, false);
        progressLayout =v.findViewById(R.id.progressLayout);
        rv_supplementals =v.findViewById(R.id.rv_supplementals);
        rv_supplementals.setLayoutManager(new LinearLayoutManager(context));
        getSupplementalData();
        return v;
    }

    private void getSupplementalData()
    {
        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/Checkout/GetSupplementalMaterials","GET");
    }

    @Override
    public void onSaveSucess(String code, String response) {
        progressLayout.setVisibility(View.GONE);
        if (code.equalsIgnoreCase("200"))
        {
            Log.e("Response",response);
            assignData(response);
        }
    }

    private void assignData(String response)
    {
        ArrayList<SupplementModel> al=new ArrayList<>();
        try {
            JSONObject js=new JSONObject(response);
           int status= js.optInt("status");
           if (status==1)
           {
               JSONArray ja=js.getJSONArray("response");
               for (int i=0;i<ja.length();i++)
               {
                  JSONObject json= ja.getJSONObject(i);
                   String CycleId=  json.optString("CycleId");
                   String Title=  json.optString("Title");
                   String TestDate=  json.optString("TestDate");
                   String ApplicationDueDate=  json.optString("ApplicationDueDate");
                   String LastClassDate=  json.optString("LastClassDate");
                   String LastExamDate=  json.optString("LastExamDate");
                   String LastTutorDate=  json.optString("LastTutorDate");
                   SupplementModel supplementModel=new SupplementModel(CycleId,Title,TestDate,ApplicationDueDate,LastClassDate,LastExamDate,LastTutorDate);
                   al.add(supplementModel);
               }
               SupplementAdapter supplementAdapter=new SupplementAdapter(al,context);
               rv_supplementals.setAdapter(supplementAdapter);
           }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(View.GONE);
           Log.e("Error",error);
    }
}
