package com.chnu.android_vk.client;

import android.app.Activity;

import android.content.Context;

import android.net.ConnectivityManager;



import android.os.Bundle;

import android.webkit.WebView;
import android.widget.*;



public class LoginActivity extends Activity {

    private WebView webView;

    private String vkApplicationID = "";
    private String vkPermissionsScope = "photos,audios,videos,wall,messages";
    private String vkRedirectPage = "google.com.ua";
    private String vkDisplayType = "page";
    private String vkApiVersion = "5.52";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();

        if ( !isNetworkConnected() )
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        else webView.loadUrl("https://oauth.vk.com/authorize?" +
                "client_id=" + vkApplicationID + "&" +
                "scope=" + vkPermissionsScope + "&" +
                "redirect_uri=" + vkRedirectPage + "&" +
                "display=" + vkDisplayType + "&" +
                "v=" + vkApiVersion + "&" +
                "response_type=token");
    }

    private boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null;
    }

    private void initializeComponents(){
        webView = (WebView) findViewById(R.id.webView);
    }

}



