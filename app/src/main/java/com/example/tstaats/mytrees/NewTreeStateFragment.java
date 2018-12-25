package com.example.tstaats.mytrees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class NewTreeStateFragment extends Fragment {

    private static final String TAG = NewTreeStateFragment.class.getName();

    public static final String BACKENDLESS_FILE_PATH = "treepics";

    private MainActivity mainActivity;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ImageView ivTreeState;
    private EditText etTreeStateName, etTreeStateDescription;
    private Button btnStateConfirm, btnStateCamera;
    private Bitmap treeStateBitmap;
    //private Tree mTree;
    private TreeState mTreeState;
    private String mRootTreeName;

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
                    mTreeState.setRootTreeName(mRootTreeName);

                    Log.d(TAG, "onClick: rootTreeName: " + mRootTreeName);
                        
/*
                        mTree = new Tree();
                        mTree.setTreeName(treeStateName);
                        mTree.setTreeDescription(treeStateDescription);
                        mTree.setUserEmail(ApplicationClass.user.getEmail());
*/

/*
                        showProgress(true);

                        tvLoad.setText("Busy uploading image... please wait...");

                        String fileName = treeStateName + ".png";

                        Backendless.Files.Android.upload(treeStateBitmap, Bitmap.CompressFormat.PNG, 100, fileName, BACKENDLESS_FILE_PATH, new AsyncCallback<BackendlessFile>() {
                            @Override
                            public void handleResponse(BackendlessFile response) {

                                mTree.setTreeImageUrl(response.getFileURL());
                                Log.d(TAG, "handleResponse: treeImageUrl: " + response.getFileURL());
                                Toast.makeText(mainActivity, "Upload successful", Toast.LENGTH_SHORT).show();
                                showProgress(false);

                                showProgress(true);
                                tvLoad.setText(getResources().getString(R.string.create_new_tree));

                                Backendless.Persistence.save(mTree, new AsyncCallback<Tree>() {
                                    @Override
                                    public void handleResponse(Tree response) {
                                        Toast.makeText(mainActivity, "Tree saved successfully", Toast.LENGTH_SHORT).show();

                                        mainActivity.fragmentSwitcher(new MainFragment(), false);
//                                    showProgress(false);
//
//                                    etTreeStateName.setText("");
//                                    etTreeStateDescription.setText("");
//                                    hideViews();
//                                    resetTreeImage();

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(mainActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(mainActivity, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
*/

                    }



            }
        });

        return view;
    }

    private void initNewTreeState(View view) {

        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        ivTreeState = view.findViewById(R.id.image_tree_state);
        etTreeStateName = view.findViewById(R.id.et_tree_state_name);
        etTreeStateDescription = view.findViewById(R.id.et_tree_state_description);

        btnStateConfirm = view.findViewById(R.id.btn_state_confirm);
        btnStateCamera = view.findViewById(R.id.btn_state_camera);

        if (getArguments() != null) {
            mRootTreeName = getArguments().getString(mainActivity.ROOT_TREE_NAME);
            Log.d(TAG, "initNewTreeState: rootTreeName: " + mRootTreeName);
            if (getArguments().getByteArray("image") != null){

                byte[] byteArray = getArguments().getByteArray("image");
                treeStateBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivTreeState.setImageBitmap(treeStateBitmap);
                showViews();

            } else {
                hideViews();
            }

        } else {
            mRootTreeName = "";
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
