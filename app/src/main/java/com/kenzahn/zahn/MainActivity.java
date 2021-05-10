package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kenzahn.zahn.adapter.NavAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.database.UpdatePre;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.model.HomeDuckData2;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.model.PreferencesJsonRes;
import com.kenzahn.zahn.newactivities.ActivityHomeScreen2;
import com.kenzahn.zahn.receiver.MyBroadcastReceiver;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.utils.MyListView;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends  AppCompatActivity implements AdapterItemClickListener, ApiController.ApiCallBack, LifecycleObserver, SaveView
{
    private ActivityHomeScreen2 homeFragmeny;
    private AppPreference mAppPreference;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private LoginModel loginRes;
    public DatabaseHandler2 databaseHandler;
    private TextView textCartItemCount;
    private int mCartItemCount=0;
    private MenuItem menuItem;
    private int userId;
    RecyclerView recyclerView;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    boolean aBoolean=true;
    DrawerLayout drawer;
    View progressLayout;
    private TextView tvUserName,tvUserEmail;
    private Toolbar toolbar;
    ImageView iv_qa_back;
    SharedPrefs sharedPrefs;
    private TypeFaceTextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs=new SharedPrefs(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        iv_qa_back = findViewById(R.id.iv_qa_back);
        progressLayout = findViewById(R.id.progressLayout);
        databaseHandler = new DatabaseHandler2(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        startAlarm();
        this.mAppPreference = AppApplication.getPreferenceInstance();
        String userData = mAppPreference.readString(Constants.USER_DATA);

        if (userData != null)
        {
            Gson gson = new Gson();
            String userDataNew = mAppPreference.readString(Constants.USER_DATA);
            loginRes = gson.fromJson(userDataNew, LoginModel.class);
            userId= loginRes.getResponse().getUserid();
            CallService(loginRes.getResponse().getUserid());
        }
        iv_qa_back.setOnClickListener(v -> onBackPressed());

    }


    private void CallService(int userid) {
        getData();
        // Toast.makeText(this, "Hiii"+userId, Toast.LENGTH_LONG).show();
        //progressLayout.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        double heightDouble = (double) height;
        double widthDouble = (double) width;
        //loadFragment();
       // AppApplication.mApiController.getBookListDetails2(userid, widthDouble, heightDouble, 4, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.cart,menu);
        menuItem = menu.findItem(R.id.cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        else if (item.getItemId()==R.id.cart)
        {
            // Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,CartActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemClick(int var1, String var2, Object var3, View var4) {

    }
    @Override
    public void OnSuccess(int type, Object response)
    {

        progressLayout.setVisibility(View.GONE);
        if (type == 2)
        {
            try {
                if (response != null) {
                    progressLayout.setVisibility(View.GONE);
                    mAppPreference.clear();
                    Intent intent = new Intent(this, LoginRegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } catch (Exception ignored) {

            }
        }
        if (type == 3) {
            try {
                if (response != null) {
                    progressLayout.setVisibility(View.GONE);
                    // Log.e("responseUpdate", "res" + MainActivity.class.getSimpleName());
                    String getValue = mAppPreference.readString("activityCurrent");
                    Log.e("responseUpdate", "" + getValue);
                    if (getValue.equals("main")) {
                        databaseHandler.deleteAllTables();
                        Log.e("responseUpdate", "table delelete");
                    } else {
                        Log.e("responseUpdate", "not delete");
                    }
                    this.mAppPreference = AppApplication.getPreferenceInstance();
                    String userData = mAppPreference.readString(Constants.USER_DATA);
                    if (userData != null) {
                        Gson gson = new Gson();
                        loginRes = gson.fromJson(userData, LoginModel.class);
                        this.CallService(loginRes.getResponse().getUserid());
                    }
                }
            } catch (Exception ignored) {

            }
        }

        if (type == 4) {
            try {
                if (response != null)
                {
                    HomeDuckData flashCardsJson = (HomeDuckData) response;
                    loadFragment();
                    SaveDB(flashCardsJson.getResult(), flashCardsJson.getPreference().get(0).getPreferencesJson());
                    progressLayout.setVisibility(View.GONE);
                }
            } catch (Exception ignored)
            {

            }
        }

    }

    private void SaveDB(ArrayList<FlashcardJsonList> result , ArrayList<PreferencesJsonRes> preferencesJson)
    {
       // databaseHandler.insertFlashCard(result, preferencesJson);
    }

    private void loadFragment()
    {
        //getExamData();
        homeFragmeny = ActivityHomeScreen2.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, homeFragmeny).commit();
    }

    @Override
    public void OnErrorResponse(int var1, Object var2) {
        progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(int var1, Object var2) {
        progressLayout.setVisibility(View.GONE);
    }




    private void startAlarm() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (alarmManager != null) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0L, this.pendingIntent);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0L, this.pendingIntent);
            }
        } else {
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, 0L, this.pendingIntent);
            }
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        String oIsd= new SharedPrefs(this).getOrderID().get(SharedPrefs.KEY_ORDER_ID);
        mAppPreference.writeString("activityCurrent", "main");

    }


    void getExamData(String res)
    {
        ArrayList<FlashcardJsonList2> result=new ArrayList<>();
        try {
            JSONObject    js = new JSONObject(res);
             String status=js.optString("status");
             if (status.equalsIgnoreCase("Success"))
             {
                 sharedPrefs.setError("No Data Found" +
                         "");
                 JSONArray ja = js.getJSONArray("data");
                 Log.e("Array", "" + ja);
                 for (int i = 0; i < ja.length(); i++) {
                     ArrayList<CardContentRes2> ch_list = new ArrayList<CardContentRes2>();
                     JSONObject json = ja.getJSONObject(i);
                     String ExamQuestions = json.optString("ExamQuestionsDetails");
                     String ExamID = json.optString("ExamID");
                     String ExamTypeID = json.optString("ExamTypeID");
                     String Randomized = json.optString("Randomized");
                     String Timed = json.optString("LastExamDate");
                     String ExamHeader = json.optString("ExamHeader");
                     //  String  QuestionCount=json.optString("QuestionCount");
                     String SortOrder = json.optString("SortOrder");
                     String ExamName = json.optString("ExamName");
                     String Version = json.optString("Version");
                     String Active = json.optString("Active");
                     String ExamModuleID = json.optString("ExamModuleID");

                     if (!ExamQuestions.equalsIgnoreCase("null")) {
                         JSONArray jaa = new JSONArray(ExamQuestions);

                         for (int k = 0; k < jaa.length(); k++) {
                             JSONObject jss = jaa.getJSONObject(k);
                             Log.e("data", "" + jaa.getJSONObject(2));
                             String ExamQuestionID = jss.optString("ExamQuestionID");
                             String CreateDate = jss.optString("CreateDate");
                             String AnswerA = jss.optString("AnswerA");
                             String AnswerB = jss.optString("AnswerB");
                             String AnswerC = jss.optString("AnswerC");
                             String AnswerD = jss.optString("AnswerD");
                             String AnswerE = jss.optString("AnswerE");
                             String AnswerF = jss.optString("AnswerF");
                             String AnswerI = jss.optString("AnswerI");
                             String AnswerII = jss.optString("AnswerII");
                             String AnswerIII = jss.optString("AnswerIII");
                             String AnswerIV = jss.optString("AnswerIV");
                             String AnswerV = jss.optString("AnswerV");
                             String AnswerVI = jss.optString("AnswerVI");
                             String ExamID_ = jss.optString("ExamID");
                             String CorrectAnswer = jss.optString("CorrectAnswer");
                             String Explanation = jss.optString("Explanation");
                             String Question = jss.optString("Question");
                             String ExamCaseID = jss.optString("ExamCaseID");
                             String SortOrder_ = jss.optString("SortOrder");
                             String ExamSectionID = jss.optString("ExamSectionID");
                             String PageBreak = jss.optString("PageBreak");
                             String PageBreak2 = jss.optString("PageBreak2");
                             //String ExamSection= jss.optString("ExamSection");
                             CardContentRes2 cardContentRes = new CardContentRes2();
                             cardContentRes.setExamQuestionID(ExamQuestionID);
                             cardContentRes.setCreateDate(CreateDate);
                             cardContentRes.setAnswerA(AnswerA);
                             cardContentRes.setAnswerB(AnswerB);
                             cardContentRes.setAnswerC(AnswerC);
                             cardContentRes.setAnswerD(AnswerD);
                             cardContentRes.setAnswerE(AnswerE);
                             cardContentRes.setAnswerF(AnswerF);
                             cardContentRes.setAnswerI(AnswerI);
                             cardContentRes.setAnswerII(AnswerII);
                             cardContentRes.setAnswerIII(AnswerIII);
                             cardContentRes.setAnswerIV(AnswerIV);
                             cardContentRes.setAnswerV(AnswerV);
                             cardContentRes.setAnswerVI(AnswerVI);
                             cardContentRes.setExamID(ExamID_);
                             cardContentRes.setCorrectAnswer(CorrectAnswer);
                             cardContentRes.setExplanation(Explanation);
                             cardContentRes.setQuestion(Question);
                             cardContentRes.setExamCaseID(ExamCaseID);
                             cardContentRes.setSortOrder("" + (k));
                             cardContentRes.setExamSectionID(ExamSectionID);
                             cardContentRes.setPageBreak(PageBreak);
                             cardContentRes.setPageBreak2(PageBreak2);
                             cardContentRes.setExamSection("");
                             ch_list.add(cardContentRes);
                         }

                         FlashcardJsonList2 flashcardJsonList2 = new FlashcardJsonList2();
                         flashcardJsonList2.setExamID(ExamID);
                         flashcardJsonList2.setExamHeader(ExamHeader);
                         flashcardJsonList2.setExamTypeID(ExamTypeID);
                         flashcardJsonList2.setRandomized(Randomized);
                         flashcardJsonList2.setTimed(Timed);
                         flashcardJsonList2.setQuestionCount("" + ch_list.size());
                         flashcardJsonList2.setDeckSortOrder("" + (i + 1));
                         flashcardJsonList2.setExamName(ExamName);
                         flashcardJsonList2.setVersion(Version);
                         flashcardJsonList2.setActive(Active);
                         flashcardJsonList2.setExamModuleID(ExamModuleID);
                         flashcardJsonList2.setCardContent(ch_list);
                         result.add(flashcardJsonList2);

                     }


                 }

                 databaseHandler.insertFlashCard_(result);
             }else {
                 //Toast.makeText(this, ""+js.optString("errMsg"), Toast.LENGTH_SHORT).show();
                 sharedPrefs.setError(js.optString("errMsg"));
             }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onSaveSucess(String code, String response)
    {
        progressLayout.setVisibility(View.GONE);
        Log.e("Response",response);
        getExamData(response);
        loadFragment();


    }

    @Override
    public void onSaveFailure(String error) {
        progressLayout.setVisibility(View.GONE);
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    void getData()
    {
        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/AppUsers/GetExamsAndQuestions?ContactID="+userId,"GET");
    }
}
