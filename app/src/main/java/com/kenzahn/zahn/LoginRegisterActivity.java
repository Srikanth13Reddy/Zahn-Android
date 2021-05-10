package com.kenzahn.zahn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.kenzahn.zahn.database.DatabaseHandler;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;
import com.kenzahn.zahn.widget.UtilsMethods;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginRegisterActivity extends AppCompatActivity implements View.OnClickListener, SaveView {
    private AppPreference mAppPreference;
    private Gson gson = new Gson();
    public DatabaseHandler databaseHandler;
    View progressLayout;
    ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hideStatusBar();
        setContentView(R.layout.activity_login_register);
        progressLayout = findViewById(R.id.progressLayout);
        mViewFlipper = findViewById(R.id.mViewFlipper);
        this.databaseHandler = new DatabaseHandler(this);
        TextView txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(this);
    }

    public final void hideStatusBar() {
        Window w = this.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.txtLogin:
                EditText etLName = findViewById(R.id.etLName);
                EditText etPass = findViewById(R.id.etPass);

                TextInputLayout input_layout_loginemail = findViewById(R.id.input_layout_loginemail);
                TextInputLayout input_layout_loginpass = findViewById(R.id.input_layout_loginpass);
                String username = String.valueOf(etLName.getText());
                String password = String.valueOf(etPass.getText());
                boolean failFlag = false;
                boolean failFlag1 = false;
                if (username.isEmpty()) {
                    input_layout_loginemail.setError(getString(R.string.username));
                    failFlag = false;
                } else {

                    input_layout_loginemail.setErrorEnabled(false);
                    failFlag = true;

                }

                if (password.isEmpty()) {
                    input_layout_loginpass.setError(getString(R.string.err_msg_password));
                    //requestFocus(view!!.etRPass)
                    failFlag1 = false;

                } else {
                    input_layout_loginpass.setErrorEnabled(false);
                    failFlag1 = true;
                }

                if (failFlag && failFlag1) {

                    if (UtilsMethods.internetCheck(this))
                    {
                        progressLayout.setVisibility(View.VISIBLE);
                       // AppApplication.mApiController.getLogin(username, password, 1, this);
                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("userName",username);
                            jsonObject.put("password",password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // onRegister(jsonObject);
                        new SaveImpl(this).handleSave(jsonObject,"api/AppUsers/Login","POST");

                    } else {
                        UtilsMethods.initToast(this, "No Internet Connection!!");
                    }


                }

            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mViewFlipper.getDisplayedChild() > 0) {
            mViewFlipper.setDisplayedChild(0);
        } else {
            finishAffinity();
            System.exit(0);
        }
    }




    private void assignData(String response)
    {
        try {
            if (response != null) {
                 JSONObject js=new JSONObject(response);
                if (js.getJSONObject("response").optBoolean("loginstatus")) {
                    this.mAppPreference = AppApplication.getPreferenceInstance();
                    mAppPreference.writeString(Constants.USER_DATA, response);
                    this.setIntent(new Intent(this.getApplicationContext(), HomeActivity.class));
                    this.startActivity(this.getIntent());
                    this.finish();
                } else {
                    UtilsMethods.initToast(this, "Username Or Password Incorrect!!!");
                }
            }
        } catch (Exception ignored) {

        }
    }


    @Override
    public void onSaveSucess(String code, String response)
    {
         progressLayout.setVisibility(View.GONE);
         if (code.equalsIgnoreCase("200"))
         {
             assignData(response);
         }
    }

    @Override
    public void onSaveFailure(String error) {
        progressLayout.setVisibility(View.GONE);
    }
}
