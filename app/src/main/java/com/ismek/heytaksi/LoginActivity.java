package com.ismek.heytaksi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.EditText;

import com.ismek.entity.BaseReturn;
import com.ismek.entity.LoginRequest;
import com.ismek.entity.LoginResponse;
import com.ismek.util.ProjectPreferences;
import com.ismek.util.SharedPreferenceUtils;
import com.ismek.ws.ApiClient;
import com.ismek.ws.HeyTaksiRest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edUsername) EditText edUsername;
    @BindView(R.id.edPassword) EditText edPassword;
    @BindView(R.id.btnLogin) FButton btnLogin;
    @BindView(R.id.btnSignIn) FButton btnSignIn;

    SweetAlertDialog pDialog;

    SharedPreferenceUtils prefUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        prefUtil = new SharedPreferenceUtils(LoginActivity.this);

        edUsername.setText("yahyacan55@gmail.com");
        edPassword.setText("12345");
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        pDialog = ProjectPreferences.showSweetDialog(LoginActivity.this,"");
        LoginRequest request = new LoginRequest();
        request.username = edUsername.getText().toString();
        request.password = edPassword.getText().toString();

        HeyTaksiRest iService = ApiClient.getClient().create(HeyTaksiRest.class);
        Call<BaseReturn<LoginResponse>> call = iService.login(request);
        call.enqueue(new Callback<BaseReturn<LoginResponse>>() {
            @Override
            public void onResponse(Call<BaseReturn<LoginResponse>> call, Response<BaseReturn<LoginResponse>> response) {
                pDialog.dismiss();
                BaseReturn<LoginResponse> result = response.body();
                LoginResponse loginResponse = result.data;
                if (result.result){
                    prefUtil.setValue("name",loginResponse.name);
                    prefUtil.setValue("lastname",loginResponse.lastName);
                    prefUtil.setValue("email",loginResponse.email);

                    Intent i = new Intent();
                    Bundle b = new Bundle();
                    b.putParcelable("user", loginResponse);
                    i.putExtras(b);
                    i.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }else{
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(result.message)
                            .setConfirmText("Tamam")
                            .show();
                }

            }

            @Override
            public void onFailure(Call<BaseReturn<LoginResponse>> call, Throwable t) {
                pDialog.dismiss();
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("")
                        .setContentText("Hata : " + t.getMessage())
                        .setConfirmText("Tamam")
                        .show();
            }
        });
    }

    @OnClick(R.id.btnSignIn)
    public void signIn(){
        startActivity(new Intent(LoginActivity.this,SignInActivity.class));
    }
}
