package com.ismek.heytaksi;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ismek.util.SharedPreferenceUtils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";
    
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Token: " + token);

        SharedPreferenceUtils.getInstance(getApplicationContext()).setValue("FCM_ID",token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

    }

}