package com.kenzahn.zahn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kenzahn.zahn.interfaces.SaveView;
import com.kenzahn.zahn.model.LoginModel;
import com.kenzahn.zahn.rest.SaveImpl;
import com.kenzahn.zahn.utils.AppPreference;
import com.kenzahn.zahn.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOutActivity extends AppCompatActivity implements SaveView
{

    private AppPreference mAppPreference;
    private LoginModel loginRes;
    int user_id;
    ProgressDialog pd;

    AppCompatEditText et_fstname,et_lastname,et_address1,et_address2,et_state,et_phone,et_email,et_zipcode,et_city;
    AppCompatEditText et_fstname_,et_lastname_,et_address1_,et_address2_,et_state_,
            et_phone_,et_email_,et_zipcode_,et_city_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Secure Checkout");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Please wait....");
        findViews();
        getUserId();
        getCustomerSetails();
    }

    private void findViews()
    {
        et_fstname= findViewById(R.id.et_fstname);
        et_lastname= findViewById(R.id.et_lastname);
        et_address1= findViewById(R.id.et_address1);
        et_address2= findViewById(R.id.et_address2);
        et_zipcode= findViewById(R.id.et_zipcode);
        et_state= findViewById(R.id.et_state);
        et_phone= findViewById(R.id.et_phone);
        et_email= findViewById(R.id.et_email);
        et_city= findViewById(R.id.et_city);

        et_fstname_= findViewById(R.id.et_fstname_);
        et_lastname_= findViewById(R.id.et_lastname_);
        et_address1_= findViewById(R.id.et_address1_);
        et_address2_= findViewById(R.id.et_address2_);
        et_zipcode_= findViewById(R.id.et_zipcode_);
        et_state_= findViewById(R.id.et_state_);
        et_phone_= findViewById(R.id.et_phone_);
        et_email_= findViewById(R.id.et_email_);
        et_city_= findViewById(R.id.et_city_);
    }

    private void getUserId()
    {

            this.mAppPreference = AppApplication.getPreferenceInstance();
            String userData = mAppPreference.readString(Constants.USER_DATA);
            if (userData != null)
            {
                Gson gson = new Gson();
                String userDataNew = mAppPreference.readString(Constants.USER_DATA);
                loginRes = gson.fromJson(userDataNew, LoginModel.class);
                user_id= loginRes.getResponse().getUserid();

            }

    }

    private void getCustomerSetails()
    {
        if (user_id>0)
        {
            pd.show();
            new SaveImpl(this).handleSave(new JSONObject(),"api/Checkout/GetCustomerDetail?userName="+user_id,"GET");
        }
    }

    @Override
    public void onSaveSucess(String code, String response) {
        pd.dismiss();
        Log.e("Response",response);
        if (code.equalsIgnoreCase("200"))
        {
            assignData(response);
        }
    }

    private void assignData(String response)
    {
        try {
            JSONObject json=new JSONObject(response);
           JSONObject jsonObject= json.getJSONObject("response");
           JSONObject bill= jsonObject.getJSONObject("Bill");
            et_fstname.setText(bill.optString("FirstName"));
            et_lastname.setText(bill.optString("LastName"));
            et_address1.setText(bill.optString("Street"));
            et_address2.setText(bill.optString("BillToStreet2"));
            et_state.setText(bill.optString("State"));
            et_phone.setText(bill.optString("PhoneNum"));
            et_email.setText(bill.optString("Email"));
            et_city.setText(bill.optString("City"));
            et_zipcode.setText(bill.optString("Zip"));


           JSONObject ship= jsonObject.getJSONObject("Ship");
            et_fstname_.setText(ship.optString("ShipToFirstName"));
            et_lastname_.setText(ship.optString("ShipToLastName"));
            et_address1_.setText(ship.optString("ShipToStreet"));
            et_address2_.setText(ship.optString("ShipToStreet2"));
            et_state_.setText(ship.optString("ShipToState"));
            et_phone_.setText(ship.optString("ShipToPhone"));
            et_email_.setText(ship.optString("Email"));
            et_city_.setText(ship.optString("ShipToCity"));
            et_zipcode_.setText(ship.optString("ShipToZip"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveFailure(String error)
    {
        pd.dismiss();
        Log.e("Response",error);
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
    }

    public void loadOrderPage(View view)
    {
        Intent i=new Intent(this,ConfirmCart.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
