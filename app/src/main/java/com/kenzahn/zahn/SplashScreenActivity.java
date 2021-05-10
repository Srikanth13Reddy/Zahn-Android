package com.kenzahn.zahn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;


public class SplashScreenActivity extends AppCompatActivity {

    private final long SPLASH_DELAY = 3000L;
    private Handler mDelayHandler = new Handler();
    private AppPreference mAppPreference;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
//    private AppUpdateManager appUpdateManager;
//    private static final int MY_REQUEST_CODE = 17326;

    private final Runnable mRunnable = (Runnable)(new Runnable() {
        public final void run()
        {
            mAppPreference = AppApplication.getPreferenceInstance();
            String userData = mAppPreference.readString(Constants.USER_DATA);
            Log.e("userdata", "userdata" + userData);
            if (userData != null)
            {
                SplashScreenActivity.this.setIntent(new Intent(SplashScreenActivity.this.getApplicationContext(), HomeActivity.class));
                SplashScreenActivity.this.startActivity(SplashScreenActivity.this.getIntent());
                SplashScreenActivity.this.finish();
            } else {
                SplashScreenActivity.this.setIntent(new Intent(SplashScreenActivity.this.getApplicationContext(), LoginRegisterActivity.class));
                SplashScreenActivity.this.startActivity(SplashScreenActivity.this.getIntent());
                SplashScreenActivity.this.finish();
            }

        }
    });

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        sp=getSharedPreferences("Zahn",MODE_PRIVATE);
        spe=sp.edit();
        this.mDelayHandler = new Handler();
        mDelayHandler.postDelayed(this.mRunnable, this.SPLASH_DELAY);
    }

    void updateApp()
    {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
            {
                AlertDialog.Builder alb=new AlertDialog.Builder(this);
                alb.setMessage("Update Available");
                alb.create().show();
            }
        });
    }
}
