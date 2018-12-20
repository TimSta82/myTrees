package com.example.tstaats.mytrees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TreeInfoFragment extends Fragment {

    private static final String TAG = "TreeInfoFragment";

    private MainActivity mainActivity;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private TextView tvTreeInfoName;
    private Button btnAddTreeState;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Tree> mTreeInfoList;


    private int position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tree_info, container, false);

        initInfo(view);

        showProgress(true);
        tvLoad.setText("Loading Tree...please wait...");
        Bundle args = getArguments();

        if (args != null) {
            mTreeInfoList = new ArrayList<>();
            showProgress(false);
            position = args.getInt("position");
            Tree currentTree = ApplicationClass.treeList.get(position);
            tvTreeInfoName.setText(currentTree.getTreeName());
            mTreeInfoList.add(currentTree);

            mAdapter = new TreeInfoAdapter(mTreeInfoList);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            Log.d(TAG, "onCreateView: currentTree: " + currentTree.getTreeName());

        } else {
            showProgress(false);
        }

        btnAddTreeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tree t = new Tree();
                t.setTreeDescription("neuer toller baum");
                t.setCreated(mTreeInfoList.get(0).getCreated());
                mTreeInfoList.add(t);
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }


    private void initInfo(View view) {
        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        btnAddTreeState = view.findViewById(R.id.btn_add_treestate);
        mRecyclerView = view.findViewById(R.id.tree_info_container);
        tvTreeInfoName = view.findViewById(R.id.text_tree_info_name);

        mLayoutManager = new LinearLayoutManager(mainActivity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
