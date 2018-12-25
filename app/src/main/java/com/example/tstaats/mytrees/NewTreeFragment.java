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

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

public class NewTreeFragment extends Fragment {

    private static final String TAG = "NewTreeFragment";


    public static final String BACKENDLESS_FILE_PATH = "treepics";

    private MainActivity mainActivity;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ImageView ivTree;
    private EditText etTreeName, etTreeDescription;
    private Button btnConfirm, btnCamera;
    private Bitmap treeBitmap;
    private Tree mTree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_tree, container, false);
        Log.d(TAG, "onCreateView: is called");

        initNewTree(view);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mainActivity.isNewTree = true;

                startActivityForResult(intent, 2);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String treeName = etTreeName.getText().toString().trim();
                String treeDescription = etTreeDescription.getText().toString().trim();

                if (treeName.isEmpty() || treeDescription.isEmpty() || getArguments() == null) {
                    //showProgress(true);
                    Toast.makeText(mainActivity, "Enter empty fields please", Toast.LENGTH_SHORT).show();
                } else {

                    mTree = new Tree();
                    mTree.setTreeName(treeName);
                    mTree.setTreeDescription(treeDescription);
                    mTree.setUserEmail(ApplicationClass.user.getEmail());

                    showProgress(true);

                    tvLoad.setText("Busy uploading image... please wait...");

                    String fileName = treeName + ".png";

                    Backendless.Files.Android.upload(treeBitmap, Bitmap.CompressFormat.PNG, 100, fileName, BACKENDLESS_FILE_PATH, new AsyncCallback<BackendlessFile>() {
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
                }
            }
        });

        return view;
    }

    private void initNewTree(View view) {

        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        ivTree = view.findViewById(R.id.image_tree);
        etTreeName = view.findViewById(R.id.et_tree_name);
        etTreeDescription = view.findViewById(R.id.et_tree_description);

        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCamera = view.findViewById(R.id.btn_camera);

        if (getArguments() != null) {
            if (getArguments().getByteArray("image") != null){

                byte[] byteArray = getArguments().getByteArray("image");
                treeBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivTree.setImageBitmap(treeBitmap);
                showViews();
            } else {
                hideViews();
            }
        }
    }

    private void showViews() {
        etTreeName.setVisibility(View.VISIBLE);
        etTreeDescription.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
    }

    public void resetTreeImage() {
        ivTree.setImageResource(R.drawable.bonsai);
    }

    public void hideViews() {
        etTreeName.setVisibility(View.INVISIBLE);
        etTreeDescription.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
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
