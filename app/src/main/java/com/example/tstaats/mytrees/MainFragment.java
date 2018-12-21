package com.example.tstaats.mytrees;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private MainActivity mainActivity;

    private ConstraintLayout mLayout;
    private Button btnTreeList, btnNewTree;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        initMain(view);

        btnNewTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newTreeFragment = new NewTreeFragment();
                Bundle args = new Bundle();
                args.putString(mainActivity.ORIGIN, TAG);
                newTreeFragment.setArguments(args);
                mainActivity.fragmentSwitcher(newTreeFragment, false);
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
        mLayout = view.findViewById(R.id.layout_main_fragment);
        btnTreeList = view.findViewById(R.id.btn_treelist);
        btnNewTree = view.findViewById(R.id.btn_new_tree);

        //Todo try fix overlapping layout after hitting backbtn comming from newTreeFragment
        mLayout.requestLayout();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity)getActivity();
    }
}
