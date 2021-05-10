package com.kenzahn.zahn;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kenzahn.zahn.adapter.NavAdapter;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.database.DatabaseHandler2;
import com.kenzahn.zahn.database.UpdatePre;
import com.kenzahn.zahn.fragments.ActivityHomeScreen;
import com.kenzahn.zahn.fragments.HomeFragment;
import com.kenzahn.zahn.interfaces.AdapterItemClickListener;
import com.kenzahn.zahn.interfaces.CallBackListener;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.CardContentRes2;
import com.kenzahn.zahn.model.FlashcardJsonList;
import com.kenzahn.zahn.model.FlashcardJsonList2;
import com.kenzahn.zahn.model.HomeDuckData;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.model.PreferencesJsonRes;
import com.kenzahn.zahn.newactivities.AcronymsActivity;
import com.kenzahn.zahn.newactivities.ActivityHomeScreen2;
import com.kenzahn.zahn.newactivities.GlossaryActivity;
import com.kenzahn.zahn.receiver.MyBroadcastReceiver;
import com.kenzahn.zahn.rest.ApiClient;
import com.kenzahn.zahn.rest.ApiController;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.utils.MyListView;
import com.kenzahn.zahn.utils.SharedPrefs;
import com.kenzahn.zahn.widget.TypeFaceTextView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.view.View.GONE;

public class HomeActivity extends AppCompatActivity implements AdapterItemClickListener, ApiController.ApiCallBack, LifecycleObserver , SaveView, CallBackListener
{


    private ActivityHomeScreen homeFragmeny;
    private AppPreference mAppPreference;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private LoginModel loginRes;
    public DatabaseHandler databaseHandler;
    public DatabaseHandler2 databaseHandler2;
    private TextView textCartItemCount;
    private int mCartItemCount=0;
    private MenuItem menuItem;
    private int userId;
    RelativeLayout rl_img;
    RecyclerView recyclerView;
    LinearLayout ll_qa;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    boolean aBoolean=true;
    MyListView listView;
    DrawerLayout drawer;
    View progressLayout;
    View nav_v;
    private TextView tvUserName,tvUserEmail;
    private Toolbar toolbar;
    SharedPrefs sharedPrefs;
    private TypeFaceTextView toolbarTitle;
    private String isShowQA="yes";
    private boolean doubleBackToExitPressedOnce=false;
    private String userstatus;
    private LinearLayout ll_online;
    private String isBoth = "yes";
    View nav_fv;
    View nav_pv;
    private LinearLayout ll_pod;
    public String isPodcast = "yes";
    private String is_flashcard="yes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        sharedPrefs=new SharedPrefs(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        setupDrawer();
        navigationView = findViewById(R.id.nav_view);
        navHeader();
        progressLayout = findViewById(R.id.progressLayout);
        databaseHandler = new DatabaseHandler(this);
        databaseHandler2 = new DatabaseHandler2(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        startAlarm();
        this.mAppPreference = AppApplication.getPreferenceInstance();
        String userData = mAppPreference.readString(Constants.USER_DATA);
        toolbar.setVisibility(GONE);
        if (userData != null)
        {
            Gson gson = new Gson();
            String userDataNew = mAppPreference.readString(Constants.USER_DATA);
            loginRes = gson.fromJson(userDataNew, LoginModel.class);
            this.userstatus = this.loginRes.getResponse().getUserstatus();
            tvUserName.setText("Welcome "+loginRes.getResponse().getUsername());
            tvUserEmail.setText("Status : "+loginRes.getResponse().getUserstatus());
            userId= loginRes.getResponse().getUserid();
            if (this.userstatus.equalsIgnoreCase("Continuing Education") || this.userstatus.equalsIgnoreCase("Dropout") || this.userstatus.equalsIgnoreCase("Licensing")) {
                this.ll_qa.setVisibility(GONE);
                this.ll_online.setVisibility(GONE);
                this.nav_v.setVisibility(GONE);
                this.nav_fv.setVisibility(GONE);
                this.isBoth = "no";
            } else {
                this.nav_v.setVisibility(View.VISIBLE);
                this.nav_fv.setVisibility(View.VISIBLE);
                this.ll_qa.setVisibility(View.VISIBLE);
                this.ll_online.setVisibility(View.VISIBLE);
                this.isBoth = "yes";
            }
            if (this.userstatus.equalsIgnoreCase("Dropout") || this.userstatus.equalsIgnoreCase("Administrator")) {
                this.ll_pod.setVisibility(GONE);
                this.nav_pv.setVisibility(GONE);
                this.isPodcast = "no";
            } else {
                this.ll_pod.setVisibility(View.VISIBLE);
                this.nav_pv.setVisibility(View.VISIBLE);
                this.isPodcast = "yes";
            }
            getPodcasts();
        }

    }

    private void getPodcasts() {
        this.progressLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(0, "http://api.kenzahn.com/api/AppUsers/GetPodcasts?ContactID=" + this.userId, new Response.Listener<String>() {
            public void onResponse(String response) {
                HomeActivity.this.progressLayout.setVisibility(GONE);
                Log.e("RES", response);
                try {
                    if (new JSONObject(response).optString("status").equalsIgnoreCase("Success")) {
                        HomeActivity.this.ll_pod.setVisibility(View.VISIBLE);
                        HomeActivity.this.nav_pv.setVisibility(View.VISIBLE);
                        isPodcast = "yes";
                        if (!HomeActivity.this.userstatus.equalsIgnoreCase("Dropout")) {
                            if (!HomeActivity.this.userstatus.equalsIgnoreCase("Administrator")) {
                                HomeActivity.this.ll_pod.setVisibility(View.VISIBLE);
                                HomeActivity.this.nav_pv.setVisibility(View.VISIBLE);
                                isPodcast = "yes";
                            }
                        }else
                        {
                            HomeActivity.this.ll_pod.setVisibility(GONE);
                            HomeActivity.this.nav_pv.setVisibility(GONE);
                            HomeActivity.this.isPodcast = "no";
                        }
                    } else {
                        HomeActivity.this.ll_pod.setVisibility(GONE);
                        HomeActivity.this.nav_pv.setVisibility(GONE);
                        HomeActivity.this.isPodcast = "no";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HomeActivity.this.getData();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                HomeActivity.this.progressLayout.setVisibility(GONE);
                Log.e("RES", "" + error);
                HomeActivity.this.getData();
            }
        }));
    }


    private void CallService(int userid) {
        // Toast.makeText(this, "Hiii"+userId, Toast.LENGTH_LONG).show();
        progressLayout.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        double heightDouble = (double) height;
        double widthDouble = (double) width;
        AppApplication.mApiController.getBookListDetails2(userid, widthDouble, heightDouble, 4, this);
    }

    private void navHeader()
    {
        View headerLayout = navigationView.getHeaderView(0);
         ll_online= headerLayout.findViewById(R.id.ll_online);
        this.ll_pod = (LinearLayout) headerLayout.findViewById(R.id.ll_pod);
        this.nav_fv = headerLayout.findViewById(R.id.nav_vv);
        this.nav_pv = headerLayout.findViewById(R.id.nav_pv);
        LinearLayout ll_logout= headerLayout.findViewById(R.id.ll_logout);
        LinearLayout ll_home= headerLayout.findViewById(R.id.ll_home);
        LinearLayout ll_acronyms= headerLayout.findViewById(R.id.ll_acronyms);
        LinearLayout ll_glossary= headerLayout.findViewById(R.id.ll_glossary);
        listView= headerLayout.findViewById(R.id.online_lv);
        final View  view= headerLayout.findViewById(R.id.nav_v);
        rl_img= headerLayout.findViewById(R.id.rl_img);
        LinearLayout ll_purchase= headerLayout.findViewById(R.id.ll_purchase);
        ll_qa= headerLayout.findViewById(R.id.ll_qa);
        LinearLayout ll_pod= headerLayout.findViewById(R.id.ll_pod);
        nav_v=headerLayout.findViewById(R.id.nav_v);
        final ImageView nav_image= headerLayout.findViewById(R.id.nav_iv);
        tvUserName = headerLayout.findViewById(R.id.tvUserName);
        tvUserEmail = headerLayout.findViewById(R.id.tvUserEmail);


        NavAdapter adapter=new NavAdapter(this);
        listView.setAdapter(adapter);
        ll_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                closeDrawyer();
                loadFragment();
                if (aBoolean)
                {
                    nav_image.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    listView.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,0);
                    // view.setLayoutParams(params);
                    aBoolean=false;
                }
                else {
                    listView.setVisibility(GONE);
                    nav_image.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,16,0,16);
                    // view.setLayoutParams(params);
                    aBoolean=true;
                }
            }
        });
        rl_img.setOnClickListener(v -> {
            // Toast.makeText(HomeActivity.this, "Hiii", Toast.LENGTH_SHORT).show();
            if (aBoolean)
            {
                nav_image.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                listView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,0);
                // view.setLayoutParams(params);
                aBoolean=false;
            }
            else {
                listView.setVisibility(GONE);
                nav_image.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,16,0,16);
                // view.setLayoutParams(params);
                aBoolean=true;
            }
        });

        ll_purchase.setOnClickListener(v -> {
            this.closeDrawyer();
            Intent i=new Intent(this,SupplementMaterialActivity.class);
            startActivity(i);
//            Fragment fragment = new SupplementalMaterialsFragment();
//            loadFragment(fragment);
//            toolbarTitle.setText("Supplemental Materials");
//            menuItem.setVisible(true);
        });

        ll_acronyms.setOnClickListener(v -> {
            this.closeDrawyer();
            callAcronyms();
        });
        ll_glossary.setOnClickListener(v -> {
            this.closeDrawyer();
            callGlossary();

        });

        ll_pod.setOnClickListener(v -> {
            this.closeDrawyer();
            Intent i=new Intent(this,PodcastActivity.class);
            startActivity(i);
//            Fragment fragment = new SupplementalMaterialsFragment();
//            loadFragment(fragment);
//            toolbarTitle.setText("Supplemental Materials");
//            menuItem.setVisible(true);
        });

        ll_qa.setOnClickListener(v -> {
            this.closeDrawyer();
            loadFragmentQA();
//            Intent i=new Intent(this,MainActivity.class);
//            startActivity(i);
//            Fragment fragment = new SupplementalMaterialsFragment();
//            loadFragment(fragment);
//            toolbarTitle.setText("Supplemental Materials");
//            menuItem.setVisible(true);
        });

        ll_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (isNetworkAvailable())
                {
                    String message=isShowQA.equalsIgnoreCase("yes")?"Do you want to sign out from Application"+"\n\n"+
                            "Note : Q&A Quick Prep data will be reset on logout. ":
                            "Do you want to sign out from Application" ;

                    AlertDialog.Builder alb=new AlertDialog.Builder(HomeActivity.this);
                    alb.setMessage(isShowQA.equalsIgnoreCase("yes")?"Do you want to sign out from Application"+"\n\n"+
                            "Note : Q&A Quick Prep data will be reset on logout. ":
                            "Do you want to sign out from Application" );
                    alb.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            closeDrawyer();
                            callLogout();
                        }
                    });
                    alb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alb.create().show();
                }
                else {
                    logout();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                closeDrawyer();
                switch (position)
                {
                    case 0:
                        // Objects.requireNonNull(tabLayout.getTabAt(0)).select();

                        callFragment(0);
                        menuItem.setVisible(false);
                        toolbarTitle.setText("Flashcards Online");

                        break;
                    case 1:
                        // Objects.requireNonNull(tabLayout.getTabAt(1)).select();
                        callFragment(1);
                        menuItem.setVisible(false);
                        toolbarTitle.setText("Flashcards Online");
                        break;
                    case 2:
                        callFragment(2);
                        menuItem.setVisible(false);
                        toolbarTitle.setText("Flashcards Online");
                        // Objects.requireNonNull(tabLayout.getTabAt(2)).select();
                        break;
                    case 3:
                        callFragment(3);
                        menuItem.setVisible(false);
                        toolbarTitle.setText("Flashcards Online");
                        // Objects.requireNonNull(tabLayout.getTabAt(3)).select();
                        break;


                }
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                closeDrawyer();
                loadFragmentHome();
                menuItem.setVisible(false);
                toolbarTitle.setText("Flashcards Online");
            }
        });

    }

    private void logout()
    {
        AlertDialog.Builder alb=new AlertDialog.Builder(HomeActivity.this);
        // alb.setTitle("Do you want to sign out from Application");
        alb.setMessage("Please connect to the Internet in order to save the changes else data will be lost");
        alb.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // callLogout();
                mAppPreference.clear();
                sharedPrefs.logout();
                deleteDatabase(DatabaseHandler.DATABASE_NAME);
                deleteDatabase(DatabaseHandler2.DATABASE_NAME);
                finish();
                Intent i=new Intent(HomeActivity.this,LoginRegisterActivity.class);
                startActivity(i);
            }
        });
        alb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alb.create().show();
    }

    private void callLogout()
    {
        sharedPrefs.logout();

        if (loginRes != null)
        {
            progressLayout.setVisibility(View.VISIBLE);
            UpdatePre updatePre = databaseHandler.getUserPrefernce(loginRes.getResponse().getUserid());
            if (updatePre.getPreferencesJson().size() > 0 && updatePre.getPreferencesJson() != null)
            {
                Log.e("Data",""+updatePre.getPreferencesJson());
                AppApplication.mApiController.getUpdatePre(updatePre, 2, this);
                deleteDatabase(DatabaseHandler.DATABASE_NAME);
                deleteDatabase(DatabaseHandler2.DATABASE_NAME);
            } else {
                mAppPreference.clear();
                Intent intent = new Intent(this.getApplicationContext(), LoginRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(intent);
                deleteDatabase(DatabaseHandler.DATABASE_NAME);
                deleteDatabase(DatabaseHandler2.DATABASE_NAME);
            }


        }

    }

    private void loadFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
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
                if (textCartItemCount.getVisibility() != GONE) {
                    textCartItemCount.setVisibility(GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setupDrawer()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbarTitle.setText("sbjj");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }


    }


    private void closeDrawyer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onItemClick(int var1, String var2, Object var3, View var4) {

    }
    @Override
    public void OnSuccess(int type, Object response)
    {
        Log.e("ResponseData",""+response.toString());
        progressLayout.setVisibility(GONE);
        if (type == 2)
        {
            try {
                if (response != null) {
                    progressLayout.setVisibility(GONE);
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
                    progressLayout.setVisibility(GONE);
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
                    isShowFlashCard(flashCardsJson.getResult());
                    loadFragmentHome();
                    SaveDB(flashCardsJson.getResult(), flashCardsJson.getPreference().get(0).getPreferencesJson());
                    this.progressLayout.setVisibility(GONE);
                }
            } catch (Exception ignored)
            {

            }
        }

    }

    private void isShowFlashCard(ArrayList<FlashcardJsonList> result) {
        ArrayList<Integer> count = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getFlashCardTypeID().equalsIgnoreCase("2")) {
                count.add(Integer.valueOf(i));
            }
        }
        if (count.size() > 0) {
            this.ll_online.setVisibility(View.VISIBLE);
            this.nav_fv.setVisibility(View.VISIBLE);
            this.is_flashcard = "yes";
            if (this.userstatus.equalsIgnoreCase("Continuing Education") || this.userstatus.equalsIgnoreCase("Dropouts") || this.userstatus.equalsIgnoreCase("Licensing")) {
                this.ll_online.setVisibility(GONE);
                this.is_flashcard = "no";
                this.nav_fv.setVisibility(GONE);
                return;
            }
            this.ll_online.setVisibility(View.VISIBLE);
            this.nav_fv.setVisibility(View.VISIBLE);
            this.is_flashcard = "yes";
            return;
        }
        this.ll_online.setVisibility(GONE);
        this.nav_fv.setVisibility(GONE);
        this.is_flashcard = "no";
    }

    private void SaveDB(ArrayList<FlashcardJsonList> result , ArrayList<PreferencesJsonRes> preferencesJson)
    {
        Collections.sort(result, new Comparator<FlashcardJsonList>() {
            @Override
            public int compare(FlashcardJsonList lhs, FlashcardJsonList rhs) {
                return lhs.getFlashCardSetID().compareTo(rhs.getFlashCardSetID());
            }
        });



        Collections.sort(preferencesJson, new Comparator<PreferencesJsonRes>() {
            @Override
            public int compare(PreferencesJsonRes lhs, PreferencesJsonRes rhs) {
                return lhs.getFlashCardSetID().compareTo(rhs.getFlashCardSetID());
            }
        });

        for (int i=0;i<result.size();i++)
        {
            Log.e("RESULTDATA",""+result.get(i).getFlashCardSetID());
        }

        for (int i=0;i<preferencesJson.size();i++)
        {
            Log.e("PRE",""+preferencesJson.get(i).getFlashCardSetID());
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Collections.sort(result, Comparator.comparing(FlashcardJsonList::getFlashCardSetID));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Collections.sort(preferencesJson, Comparator.comparing(PreferencesJsonRes::getFlashCardSetID));
//        }
        databaseHandler.insertFlashCard(result, preferencesJson);
        Log.e("Mainsize",""+result.size());

    }

    private void loadFragment()
    {
        toolbarTitle.setText("Flashcards Online");
        toolbar.setVisibility(View.VISIBLE);
        homeFragmeny = ActivityHomeScreen.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, homeFragmeny).commit();
        //  rl_img.setVisibility(View.VISIBLE);
    }

    private void loadFragmentHome()
    {

        this.toolbarTitle.setText("Flashcards Online");
        this.toolbar.setVisibility(GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, HomeFragment.newInstance(this.loginRes.getResponse().getUsername(), this.isBoth, this.isShowQA, this.is_flashcard, this.isPodcast)).commit();
        this.listView.setVisibility(GONE);
    }

    private void loadFragmentQA()
    {
//        toolbarTitle.setText("Q&A Quick Prep");
//        toolbar.setVisibility(View.VISIBLE);
//        ActivityHomeScreen2 homeFragmeny = ActivityHomeScreen2.newInstance();
//        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, homeFragmeny).commit();
//        rl_img.setVisibility(View.GONE);
//        listView.setVisibility(View.GONE);
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }

    @Override
    public void OnErrorResponse(int var1, Object var2) {
        progressLayout.setVisibility(GONE);
    }

    @Override
    public void onFailure(int var1, Object var2) {
        progressLayout.setVisibility(GONE);
    }


    private void callFragment(int pos)
    {
        new SharedPrefs(this).logout();
        homeFragmeny.callSetUpCurrent(pos);
        closeDrawyer();
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
        getCount(oIsd);
        mAppPreference.writeString("activityCurrent", "main");

    }
    public void getCount(String orderId)
    {
        //progressLayout.setVisibility(View.VISIBLE);
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, ApiClient.API_BASE_URL + "api/Checkout/LoadCart?orderId="+orderId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                // progressLayout.setVisibility(View.GONE);

                try {
                    JSONObject js=new JSONObject(response);
                    JSONArray ja=  js.getJSONArray("orderItemList");
                    Log.e("Response",response);
                    mCartItemCount=ja.length();
                    setupBadge();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   textCartItemCount.setText("5");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // progressLayout.setVisibility(View.GONE);
                Log.e("Response",""+error);

            }
        });
        requestQueue.add(stringRequest);
    }


    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void getData()
    {
        deleteDatabase(DatabaseHandler.DATABASE_NAME);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int versioncode=pInfo.versionCode;
            Log.e("version",""+version);
            Log.e("versioncode",""+versioncode);
            if (versioncode>2)
            {
                if (sharedPrefs.isFirst())
                {
                    deleteDatabase(DatabaseHandler2.DATABASE_NAME);
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        progressLayout.setVisibility(View.VISIBLE);
        new SaveImpl(this).handleSave(new JSONObject(),"api/AppUsers/GetExamsAndQuestions?ContactID="+userId,"GET");
        //  new SaveImpl(this).handleSave(new JSONObject(),"api/FlashCards/GetFlashCardsList?userName="+userId+"&isIos=false&width=200.0&height=300.0","GET");
    }

    @Override
    public void onSaveSucess(String code, String response)
    {
        this.progressLayout.setVisibility(GONE);
        try {
            if (new JSONObject(response).optString("status").equalsIgnoreCase("Success")) {
                this.ll_qa.setVisibility(View.VISIBLE);
                this.nav_v.setVisibility(View.VISIBLE);
                this.isShowQA = "yes";
                if (!this.userstatus.equalsIgnoreCase("Continuing Education") && !this.userstatus.equalsIgnoreCase("Dropouts")) {
                    if (!this.userstatus.equalsIgnoreCase("Licensing")) {
                        this.ll_qa.setVisibility(View.VISIBLE);
                        this.nav_v.setVisibility(View.VISIBLE);
                        this.isShowQA = "yes";
                    }
                }
                this.ll_qa.setVisibility(GONE);
                this.nav_v.setVisibility(GONE);
                this.isShowQA = "no";
            } else {
                this.isShowQA = "no";
                this.ll_qa.setVisibility(GONE);
                this.nav_v.setVisibility(GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CallService(this.loginRes.getResponse().getUserid());
    }

    @Override
    public void onSaveFailure(String error)
    {
        progressLayout.setVisibility(GONE);
        deleteDatabase(DatabaseHandler.DATABASE_NAME);
        CallService(loginRes.getResponse().getUserid());
    }

    @Override
    public void onCallBack(String type)
    {
        if (type.equalsIgnoreCase("open"))
        {
            toolbar.setVisibility(GONE);
            drawer.openDrawer(Gravity.LEFT);
        }else if (type.equalsIgnoreCase("qa"))
        {

            loadFragmentQA();

        }else if (type.equalsIgnoreCase("flash"))
        {
            loadFragment();

        }else if (type.equalsIgnoreCase("glossary"))
        {
            callGlossary();
        }

        else if (type.equalsIgnoreCase("acronys"))
        {
            callAcronyms();
        }
        else {
            Intent i=new Intent(this,PodcastActivity.class);
            startActivity(i);
        }

    }

//    void getExamData(String res)
//    {
//        ArrayList<FlashcardJsonList2> result=new ArrayList<>();
//        try {
//            JSONObject    js = new JSONObject(res);
//            String status=js.optString("status");
//            if (status.equalsIgnoreCase("Success"))
//            {
//                sharedPrefs.setError("No Data Found" +
//                        "");
//                JSONArray ja = js.getJSONArray("data");
//                Log.e("Array", "" + ja);
//                for (int i = 0; i < ja.length(); i++) {
//                    ArrayList<CardContentRes2> ch_list = new ArrayList<CardContentRes2>();
//                    JSONObject json = ja.getJSONObject(i);
//                    String ExamQuestions = json.optString("ExamQuestionsDetails");
//                    String ExamID = json.optString("ExamID");
//                    String ExamTypeID = json.optString("ExamTypeID");
//                    String Randomized = json.optString("Randomized");
//                    String Timed = json.optString("LastExamDate");
//                    String ExamHeader = json.optString("ExamHeader");
//                    //  String  QuestionCount=json.optString("QuestionCount");
//                    String SortOrder = json.optString("SortOrder");
//                    String ExamName = json.optString("ExamName");
//                    String Version = json.optString("Version");
//                    String Active = json.optString("Active");
//                    String ExamModuleID = json.optString("ExamModuleID");
//
//                    if (!ExamQuestions.equalsIgnoreCase("null")) {
//                        JSONArray jaa = new JSONArray(ExamQuestions);
//
//                        for (int k = 0; k < jaa.length(); k++) {
//                            JSONObject jss = jaa.getJSONObject(k);
//                            Log.e("data", "" + jaa.getJSONObject(2));
//                            String ExamQuestionID = jss.optString("ExamQuestionID");
//                            String CreateDate = jss.optString("CreateDate");
//                            String AnswerA = jss.optString("AnswerA");
//                            String AnswerB = jss.optString("AnswerB");
//                            String AnswerC = jss.optString("AnswerC");
//                            String AnswerD = jss.optString("AnswerD");
//                            String AnswerE = jss.optString("AnswerE");
//                            String AnswerF = jss.optString("AnswerF");
//                            String AnswerI = jss.optString("AnswerI");
//                            String AnswerII = jss.optString("AnswerII");
//                            String AnswerIII = jss.optString("AnswerIII");
//                            String AnswerIV = jss.optString("AnswerIV");
//                            String AnswerV = jss.optString("AnswerV");
//                            String AnswerVI = jss.optString("AnswerVI");
//                            String ExamID_ = jss.optString("ExamID");
//                            String CorrectAnswer = jss.optString("CorrectAnswer");
//                            String Explanation = jss.optString("Explanation");
//                            String Question = jss.optString("Question");
//                            String ExamCaseID = jss.optString("ExamCaseID");
//                            String SortOrder_ = jss.optString("SortOrder");
//                            String ExamSectionID = jss.optString("ExamSectionID");
//                            String PageBreak = jss.optString("PageBreak");
//                            String PageBreak2 = jss.optString("PageBreak2");
//                            //String ExamSection= jss.optString("ExamSection");
//                            CardContentRes2 cardContentRes = new CardContentRes2();
//                            cardContentRes.setExamQuestionID(ExamQuestionID);
//                            cardContentRes.setCreateDate(CreateDate);
//                            cardContentRes.setAnswerA(AnswerA);
//                            cardContentRes.setAnswerB(AnswerB);
//                            cardContentRes.setAnswerC(AnswerC);
//                            cardContentRes.setAnswerD(AnswerD);
//                            cardContentRes.setAnswerE(AnswerE);
//                            cardContentRes.setAnswerF(AnswerF);
//                            cardContentRes.setAnswerI(AnswerI);
//                            cardContentRes.setAnswerII(AnswerII);
//                            cardContentRes.setAnswerIII(AnswerIII);
//                            cardContentRes.setAnswerIV(AnswerIV);
//                            cardContentRes.setAnswerV(AnswerV);
//                            cardContentRes.setAnswerVI(AnswerVI);
//                            cardContentRes.setExamID(ExamID_);
//                            cardContentRes.setCorrectAnswer(CorrectAnswer);
//                            cardContentRes.setExplanation(Explanation);
//                            cardContentRes.setQuestion(Question);
//                            cardContentRes.setExamCaseID(ExamCaseID);
//                            cardContentRes.setSortOrder("" + (k));
//                            cardContentRes.setExamSectionID(ExamSectionID);
//                            cardContentRes.setPageBreak(PageBreak);
//                            cardContentRes.setPageBreak2(PageBreak2);
//                            cardContentRes.setExamSection("");
//                            ch_list.add(cardContentRes);
//                        }
//
//                        FlashcardJsonList2 flashcardJsonList2 = new FlashcardJsonList2();
//                        flashcardJsonList2.setExamID(ExamID);
//                        flashcardJsonList2.setExamHeader(ExamHeader);
//                        flashcardJsonList2.setExamTypeID(ExamTypeID);
//                        flashcardJsonList2.setRandomized(Randomized);
//                        flashcardJsonList2.setTimed(Timed);
//                        flashcardJsonList2.setQuestionCount("" + ch_list.size());
//                        flashcardJsonList2.setDeckSortOrder("" + (i + 1));
//                        flashcardJsonList2.setExamName(ExamName);
//                        flashcardJsonList2.setVersion(Version);
//                        flashcardJsonList2.setActive(Active);
//                        flashcardJsonList2.setExamModuleID(ExamModuleID);
//                        flashcardJsonList2.setCardContent(ch_list);
//                        result.add(flashcardJsonList2);
//
//                    }
//
//
//                }
//
//                databaseHandler2.insertFlashCard_(result);
//            }else {
//                //Toast.makeText(this, ""+js.optString("errMsg"), Toast.LENGTH_SHORT).show();
//                sharedPrefs.setError(js.optString("errMsg"));
//            }
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

//    void  syncData()
//    {
//        progressLayout.setVisibility(View.VISIBLE);
//        UpdatePre updatePre = databaseHandler.getUserPrefernce(loginRes.getResponse().getUserid());
//        if (updatePre.getPreferencesJson().size() > 0 && updatePre.getPreferencesJson() != null) {
//            AppApplication.mApiController.getUpdatePre(updatePre, 2, this);
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // syncData();
    }

    void callGlossary()
    {
        Intent i=new Intent(this, GlossaryActivity.class);
        i.putExtra("name","Glossary");
        i.putExtra("type","Glossary");
        startActivity(i);
    }

    void callAcronyms()
    {
        Intent i=new Intent(this, AcronymsActivity.class);
        i.putExtra("name","Acronyms");
        i.putExtra("type","Acronyms");
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce){

            moveTaskToBack(true);
            this.finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this,"Please Double click to exit app",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        UpdatePre updatePre = databaseHandler.getUserPrefernce(loginRes.getResponse().getUserid());
        if (updatePre.getPreferencesJson().size() > 0 && updatePre.getPreferencesJson() != null)
        {
            Log.e("Data",""+updatePre.getPreferencesJson());
            AppApplication.mApiController.getUpdatePre(updatePre, 6, this);
        }
    }
}
