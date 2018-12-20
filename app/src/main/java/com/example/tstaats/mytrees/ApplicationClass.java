package com.example.tstaats.mytrees;


import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

// very first class that the application is running, even before the MainActivity
public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "7E4037CC-1A42-F063-FF97-4482E7BA9000";
    public static final String API_KEY = "913ACE63-EFF8-1DFA-FF1E-D502D1163300";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Tree> treeList;
    public static ImageLoader loader;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),
                APPLICATION_ID,
                API_KEY);

    }
}
