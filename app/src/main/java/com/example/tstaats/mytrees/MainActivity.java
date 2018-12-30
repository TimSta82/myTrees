package com.example.tstaats.mytrees;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ORIGIN = "origin";

    public boolean isNewTree = false;
    public boolean isNewTreeState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: is called");

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
        ApplicationClass.loader = ImageLoader.getInstance();

        LoginFragment loginFragment = new LoginFragment();
        fragmentSwitcher(loginFragment, true);

        Log.d(TAG, "onCreate: test commit");

        
    }

    public void fragmentSwitcher(Fragment fragment, boolean toBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if(toBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bundle args = new Bundle();
        args.putByteArray("image", byteArray);

        Fragment fragment = null;

        if (isNewTree == true){
            Log.d(TAG, "onActivityResult: NewTreeFragment");
            fragment = new NewTreeFragment();
            isNewTree = false;
        }

        if (isNewTreeState == true){
            Log.d(TAG, "onActivityResult: NewTreeStateFragment");
            fragment = new NewTreeStateFragment();
            isNewTreeState = false;
        }

        fragment.setArguments(args);
        fragmentSwitcher(fragment, false);
    }
}
