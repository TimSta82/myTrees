package com.example.tstaats.mytrees;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private MainActivity mainActivity;

    private Button btnTreeList, btnNewTree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initMain(view);

        btnNewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.fragmentSwitcher(new NewTreeFragment(), true);

            }
        });

        btnTreeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.fragmentSwitcher(new TreeListFragment(), true);
            }
        });

        return view;
    }

    private void initMain(View view) {
        btnTreeList = view.findViewById(R.id.btn_treelist);
        btnNewTree = view.findViewById(R.id.btn_new_tree);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity)getActivity();
    }
}
