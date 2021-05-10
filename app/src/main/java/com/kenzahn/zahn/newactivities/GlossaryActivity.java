package com.kenzahn.zahn.newactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.fragments.ActivityHomeGlossary;
import com.kenzahn.zahn.widget.TypeFaceTextView;

public class GlossaryActivity extends AppCompatActivity
{
    private ActivityHomeGlossary homeFragmeny;
    private TypeFaceTextView toolbarTitle;
    ImageView iv_qa_back;
    String type,titile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Glossary");
        iv_qa_back = findViewById(R.id.iv_qa_back);
        iv_qa_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle b=getIntent().getExtras();
        if (b!=null)
        {
            titile= b.getString("name");
            type= b.getString("type");
            toolbarTitle.setText(titile);
        }
        loadFragment();
    }

    private void loadFragment()
    {
        //getExamData();
        homeFragmeny = ActivityHomeGlossary.newInstance(type,type);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, homeFragmeny).commit();
    }
}