package com.example.tstaats.mytrees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.backendless.files.BackendlessFile;

import java.util.ArrayList;
import java.util.List;

public class NewTreeStateFragment extends Fragment {

    private static final String TAG = NewTreeStateFragment.class.getName();

    public static final String BACKENDLESS_FILE_PATH = "treepics";

    private MainActivity mainActivity;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ImageView ivTreeState;
    private EditText etTreeStateName, etTreeStateDescription;
    private TextView tvRootTreeName;
    private Button btnStateConfirm, btnStateCamera;
    private Bitmap treeStateBitmap;
    private Tree mTree;
    private TreeState mTreeState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_tree_state, container, false);
        Log.d(TAG, "onCreateView: is called");

        initNewTreeState(view);

        btnStateCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mainActivity.isNewTreeState = true;

                startActivityForResult(intent, 1);

            }
        });

        btnStateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String treeStateName = etTreeStateName.getText().toString().trim();
                String treeStateDescription = etTreeStateDescription.getText().toString().trim();

                if (treeStateName.isEmpty() || treeStateDescription.isEmpty() || getArguments() == null) {
                    //showProgress(true);
                    Toast.makeText(mainActivity, "Enter empty fields please", Toast.LENGTH_SHORT).show();
                } else {

                    mTreeState = new TreeState();

                    mTreeState.setRootTreeName(mTree.getTreeName());
                    mTreeState.setTreeStateDescription(treeStateDescription);

                    Log.d(TAG, "onClick: ApplicationClass.rootTreeName: " + ApplicationClass.rootTree.getTreeName());
                    Log.d(TAG, "onClick: mTree: " + mTree.getTreeName());

                    showProgress(true);

                    tvLoad.setText(getResources().getString(R.string.upload_new_tree_state_image));

                    String fileName = treeStateName + ".png";

                    Backendless.Files.Android.upload(treeStateBitmap, Bitmap.CompressFormat.PNG, 100, fileName, BACKENDLESS_FILE_PATH, new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {

                            mTreeState.setTreeStateImageUrl(response.getFileURL());

                            Log.d(TAG, "handleResponse: treeStateImageUrl: " + response.getFileURL());
                            Toast.makeText(mainActivity, "TreeState Upload successful", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                            showProgress(true);
                            tvLoad.setText(getResources().getString(R.string.create_new_tree_state));

                            mTree.addTreeState(mTreeState);

                            new SaveTreeStateTask().execute(mTree);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(mainActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });

                }

            }
        });

        return view;
    }

    private class SaveTreeStateTask extends AsyncTask<Tree, Void, Tree> {

        @Override
        protected Tree doInBackground(Tree... trees) {
            Tree tree = trees[0];

            List<TreeState> treeStateList = mTree.getTreeStates();
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

            Fragment treeDetailFragment = new TreeDetailFragment();
            Bundle args = new Bundle();
            args.putInt("position", ApplicationClass.position);
            treeDetailFragment.setArguments(args);

            mainActivity.fragmentSwitcher(treeDetailFragment, true);
        }
    }


    private void initNewTreeState(View view) {

        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        tvRootTreeName = view.findViewById(R.id.text_root_tree_name);
        ivTreeState = view.findViewById(R.id.image_tree_state);
        etTreeStateName = view.findViewById(R.id.et_tree_state_name);
        etTreeStateDescription = view.findViewById(R.id.et_tree_state_description);

        btnStateConfirm = view.findViewById(R.id.btn_state_confirm);
        btnStateCamera = view.findViewById(R.id.btn_state_camera);

        if (ApplicationClass.rootTree != null){
            mTree = ApplicationClass.rootTree;
            tvRootTreeName.setText(ApplicationClass.rootTree.getTreeName());
        }
        if (getArguments() != null) {
            if (getArguments().getByteArray("image") != null){

                byte[] byteArray = getArguments().getByteArray("image");
                treeStateBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivTreeState.setImageBitmap(treeStateBitmap);
                showViews();

            } else {
                hideViews();
            }

        }

    }

    private void showViews() {
        etTreeStateName.setVisibility(View.VISIBLE);
        etTreeStateDescription.setVisibility(View.VISIBLE);
        btnStateConfirm.setVisibility(View.VISIBLE);
    }

    public void resetTreeImage() {
        ivTreeState.setImageResource(R.drawable.bonsai);
    }

    public void hideViews() {
        etTreeStateName.setVisibility(View.INVISIBLE);
        etTreeStateDescription.setVisibility(View.INVISIBLE);
        btnStateConfirm.setVisibility(View.INVISIBLE);
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
