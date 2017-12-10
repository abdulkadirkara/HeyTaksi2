package com.ismek.heytaksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ismek.entity.BaseReturn;
import com.ismek.entity.LoginResponse;
import com.ismek.util.ProjectPreferences;
import com.ismek.ws.ApiClient;
import com.ismek.ws.HeyTaksiRest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.edName) EditText edName;
    @BindView(R.id.edLastname) EditText edLastname;
    @BindView(R.id.edBirthday) EditText edBirthday;
    @BindView(R.id.edEmail) EditText edEmail;
    @BindView(R.id.edPassword) EditText edPassword;
    @BindView(R.id.btnSignIn) FButton btnSignIn;
    @BindView(R.id.edPhone) EditText edPhone;
    @BindView(R.id.rgUserType) RadioGroup rgUserType;
    @BindView(R.id.llPlate) LinearLayout llPlate;
    @BindView(R.id.edPlate) EditText edPlate;

    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    SweetAlertDialog pDialog;

    private String userType = "K";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ButterKnife.bind(this);

    }

    @OnClick({ R.id.rbUser, R.id.rbTaxiDriver })
    public void onUserTypeClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked();
        switch (radioButton.getId()) {
            case R.id.rbUser:
                if (checked) {
                    llPlate.setVisibility(View.GONE);
                    edPlate.setText("");
                    userType = "K";
                }
                break;
            case R.id.rbTaxiDriver:
                if (checked) {
                    llPlate.setVisibility(View.VISIBLE);
                    edPlate.setText("");
                    userType = "T";
                }
                break;
            default :
                break;
        }
    }


    @OnClick(R.id.btnSignIn)
    public void onClickSignIn(){
        pDialog = ProjectPreferences.showSweetDialog(SignInActivity.this,"");

        LoginResponse user = new LoginResponse();
        user.name = edName.getText().toString();
        user.lastName = edLastname.getText().toString();
        try {
            user.birthday = formatter.parse(edBirthday.getText().toString());
        }catch (ParseException e){
            e.printStackTrace();
        }
        user.email = edEmail.getText().toString();
        user.password = edPassword.getText().toString();
        user.phone = edPhone.getText().toString();
        user.userType = userType;
        user.plate = edPlate.getText().toString();

        HeyTaksiRest iService = ApiClient.getClient().create(HeyTaksiRest.class);
        Call<BaseReturn<String>> call = iService.signIn(user);
        call.enqueue(new Callback<BaseReturn<String>>() {
            @Override
            public void onResponse(Call<BaseReturn<String>> call, Response<BaseReturn<String>> response) {
                pDialog.dismiss();
                BaseReturn<String> result = response.body();
                if (result.result){
                    new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("")
                            .setContentText(result.message)
                            .setConfirmText("Tamam")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(SignInActivity.this,LoginActivity.class));
                                }
                            })
                            .show();
                }else{
                    new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(result.message)
                            .setConfirmText("Tamam")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<BaseReturn<String>> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(SignInActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("")
                        .setContentText("Hata : " + t.getMessage())
                        .setConfirmText("Tamam")
                        .show();
            }
        });


    }


}
