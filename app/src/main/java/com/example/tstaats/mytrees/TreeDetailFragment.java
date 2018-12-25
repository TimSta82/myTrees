package com.example.tstaats.mytrees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class TreeDetailFragment extends Fragment {

    private static final String TAG = "TreeDetailFragment";

    private MainActivity mainActivity;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private TextView tvRootTreeName;
    private Button btnShowRootTree;
    private boolean isRootTreeVisibil = false;

    private CardView cv;
    private ImageView ivRootTree;
    private TextView tvRootTreeDate;
    private TextView tvRootTreeDescription;
    private Button btnAddTreeState;
    private RecyclerView mRecyclerView;
    private TreeDetailAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageLoader mLoader;

    private Tree mCurrentTree;
    private ArrayList<Tree> mTreeInfoList;
    private ArrayList<TreeState> mTreeStateList;

    private TreeState mTreeState;
    private int count = 0;

    private int position;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tree_detail, container, false);

        initInfo(view);

        showProgress(true);
        tvLoad.setText("Loading Tree...please wait...");
        Bundle args = getArguments();

        if (args != null) {
            mTreeStateList = new ArrayList<>();
            showProgress(false);
            position = args.getInt("position");
            mCurrentTree = ApplicationClass.treeList.get(position);

            String whereClause = "rootTreeName = '" + mCurrentTree.getTreeName() + "'";

            DataQueryBuilder queryBuilder = DataQueryBuilder.create();
            queryBuilder.setWhereClause(whereClause);

            queryBuilder.setGroupBy("created");

            showProgress(true);
            tvLoad.setText("Busy loading treestates... please wait...");

            Backendless.Persistence.of(TreeState.class).find(queryBuilder, new AsyncCallback<List<TreeState>>() {
                @Override
                public void handleResponse(List<TreeState> response) {
                    Toast.makeText(mainActivity, "TreeStates successfully loaded", Toast.LENGTH_SHORT).show();
                    ApplicationClass.treeStateList = response;

                    mAdapter = new TreeDetailAdapter((ArrayList<TreeState>) ApplicationClass.treeStateList, ApplicationClass.loader);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    showProgress(false);

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(mainActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleFault: " + fault.getMessage());
                    showProgress(false);
                }
            });


            mLoader.displayImage(mCurrentTree.getTreeImageUrl(), ivRootTree);
            tvRootTreeName.setText(mCurrentTree.getTreeName());
            tvRootTreeDescription.setText(mCurrentTree.getTreeDescription());
            tvRootTreeDate.setText(mCurrentTree.getCreated().toString());


            // TODO handle item clicks
//            mAdapter.setOnItemClickListener(new TreeDetailAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//                    //Toast.makeText(mainActivity, mTreeInfoList.get(position).getTreeDescription(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(mainActivity, mTreeStateList.get(position).getTreeStateDescription(), Toast.LENGTH_SHORT).show();
//                }
//            });

            Log.d(TAG, "onCreateView: mCurrentTree: " + mCurrentTree.getTreeName());

        } else {
            showProgress(false);
        }

        btnShowRootTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv.setVisibility(View.VISIBLE);
                btnShowRootTree.setVisibility(View.GONE);
            }
        });

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cv.setVisibility(View.GONE);
                btnShowRootTree.setVisibility(View.VISIBLE);

            }
        });
        btnAddTreeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newTreeStateFragment = new NewTreeStateFragment();
                Bundle args = new Bundle();
                args.putString(mainActivity.ROOT_TREE_NAME, mCurrentTree.getTreeName());
                newTreeStateFragment.setArguments(args);
                mainActivity.fragmentSwitcher(newTreeStateFragment, true);


                // TODO saving treeState nach newTreeState portieren
                // Dummy data for backendless relation test
                mTreeState = new TreeState();
                mTreeState.setRootTreeName(mCurrentTree.getTreeName());
                mTreeState.setTreeStateDescription("Krasse neue info " + count);
                mTreeState.setTreeStateImageUrl("https://backendlessappcontent.com/7E4037CC-1A42-F063-FF97-4482E7BA9000/" +
                        "913ACE63-EFF8-1DFA-FF1E-D502D1163300/files/treepics/Dachs.png");

                mCurrentTree.addTreeState(mTreeState);

                //new SaveTreeStateTask().execute(mCurrentTree);

                count++;
            }
        });

        return view;
    }

    private class SaveTreeStateTask extends AsyncTask<Tree, Void, Tree>{

        @Override
        protected Tree doInBackground(Tree... trees) {
            Tree tree = trees[0];

            List<TreeState> treeStateList = mCurrentTree.getTreeStates();
            List<TreeState> savedTreeStates = new ArrayList<TreeState>();

            for (TreeState treeState: treeStateList) {
                TreeState savedTreeState = Backendless.Data.of(TreeState.class).save(treeState);
                savedTreeStates.add(savedTreeState);
            }

            Tree savedTree = Backendless.Data.of(Tree.class).save(tree);
            Backendless.Data.of(Tree.class).addRelation(savedTree,
                    "treeStates:TreeState:n",
                    savedTreeStates);
            savedTree.setTreeStates(savedTreeStates);

            return savedTree;
        }

        @Override
        protected void onPostExecute(Tree tree) {
            super.onPostExecute(tree);

            ApplicationClass.treeStateList = tree.getTreeStates();
            mAdapter.notifyItemInserted(ApplicationClass.treeStateList.size());
        }
    }


    private void initInfo(View view) {
        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        btnShowRootTree = view.findViewById(R.id.btn_show_root_tree);
        cv = view.findViewById(R.id.cardview_root);
        ivRootTree = cv.findViewById(R.id.image_root_tree);
        tvRootTreeDescription = cv.findViewById(R.id.text_root_tree_description);
        tvRootTreeDate = cv.findViewById(R.id.text_root_tree_date);

        btnAddTreeState = view.findViewById(R.id.btn_add_treestate);
        mRecyclerView = view.findViewById(R.id.tree_info_container);
        tvRootTreeName = view.findViewById(R.id.text_tree_info_name);

        mLayoutManager = new LinearLayoutManager(mainActivity);
        mLoader = ApplicationClass.loader;
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
