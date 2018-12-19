package com.example.tstaats.mytrees;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: is called");

        LoginFragment loginFragment = new LoginFragment();
        fragmentSwitcher(loginFragment, true);

        
    }

    public void fragmentSwitcher(Fragment fragment, boolean toBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if(toBackStack){
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
