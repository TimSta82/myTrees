package com.example.tstaats.mytrees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class TreeListFragment extends Fragment {

    private static final String TAG = "TreeListFragment";

    private MainActivity mainActivity;

    private RecyclerView mRv;
    private TreeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View mProgressView;
    public static View mLoginFormViewContactList;
    private TextView tvLoad;

    private TextView tvListOfTrees;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tree_list, container, false);

        initContactList(view);

        /**
         * deleting row item via long click
         */
//        mRv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });


        String whereClause = "userEmail = '" + ApplicationClass.user.getEmail() + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        // for sorting the data
        queryBuilder.setGroupBy("treeName");

        showProgress(true);
        tvLoad.setText(getResources().getString(R.string.get_contacts));

        Backendless.Persistence.of(Tree.class).find(queryBuilder, new AsyncCallback<List<Tree>>() {
            @Override
            public void handleResponse(List<Tree> response) {

                Toast.makeText(mainActivity, "Trees loaded successful", Toast.LENGTH_SHORT).show();
                ApplicationClass.treeList = response;

                mAdapter = new TreeListAdapter(ApplicationClass.treeList, ApplicationClass.loader);
                mRv.setLayoutManager(mLayoutManager);
                mRv.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new TreeListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle args = new Bundle();
                        args.putInt("position", position);

                        Fragment treeDetailFragment = new TreeDetailFragment();
                        treeDetailFragment.setArguments(args);
                        mainActivity.fragmentSwitcher(treeDetailFragment, true, "TreeDetailFragment");
                    }
                });

                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(mainActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "handleFault: " + fault.getMessage());
                showProgress(false);
            }
        });

        return view;
    }

    private void initContactList(View view) {
        mProgressView = view.findViewById(R.id.login_progress_treelistfragment);
        mLoginFormViewContactList = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad_treelistfragment);

        mRv = view.findViewById(R.id.rv_tree_list);
        tvListOfTrees = view.findViewById(R.id.tv_trees);

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

            mLoginFormViewContactList.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormViewContactList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormViewContactList.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormViewContactList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
