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

public class NewTreeFragment extends Fragment {

    private static final String TAG = "NewTreeFragment";

    private MainActivity mainActivity;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private ImageView ivTree;
    private EditText etTreeName, etTreeDescription;
    private Button btnConfirm, btnCamera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_tree, container, false);

        initNewContact(view);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String treeName = etTreeName.getText().toString().trim();
                String treeDescription = etTreeDescription.getText().toString().trim();

                if (treeName.isEmpty() || treeDescription.isEmpty()) {
                    //showProgress(true);
                    Toast.makeText(mainActivity, "Enter empty fields please", Toast.LENGTH_SHORT).show();
                } else {
                    Tree tree = new Tree();
                    tree.setTreeName(treeName);
                    tree.setTreeDescription(treeDescription);
                    tree.setUserEmail(ApplicationClass.user.getEmail());

                    showProgress(true);
                    tvLoad.setText(getResources().getString(R.string.create_new_tree));

                    Backendless.Persistence.save(tree, new AsyncCallback<Tree>() {
                        @Override
                        public void handleResponse(Tree response) {
                            Toast.makeText(mainActivity, "Tree saved successfully", Toast.LENGTH_SHORT).show();

                            showProgress(false);

                            etTreeName.setText("");
                            etTreeDescription.setText("");

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

    private void initNewContact(View view) {


        mProgressView = view.findViewById(R.id.login_progress);
        mLoginFormView = view.findViewById(R.id.login_form);
        tvLoad = view.findViewById(R.id.tvLoad);

        ivTree = view.findViewById(R.id.image_tree);
        etTreeName = view.findViewById(R.id.et_tree_name);
        etTreeDescription = view.findViewById(R.id.et_tree_description);

        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCamera = view.findViewById(R.id.btn_camera);

        if (getArguments() != null){
            byte[] byteArray = getArguments().getByteArray("image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ivTree.setImageBitmap(bitmap);
        }

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