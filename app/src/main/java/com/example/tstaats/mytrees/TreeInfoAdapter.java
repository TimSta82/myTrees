package com.example.tstaats.mytrees;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TreeInfoAdapter extends RecyclerView.Adapter<TreeInfoAdapter.TreeInfoViewHolder> {

    private ArrayList<Tree> mList;

    public static class TreeInfoViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTreeInfoDate;
        public TextView mTreeInfoDescription;


        public TreeInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_info);
            mTreeInfoDate = itemView.findViewById(R.id.text_tree_info_date);
            mTreeInfoDescription = itemView.findViewById(R.id.text_tree_info_description);
        }
    }

    @NonNull
    @Override
    public TreeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_info, parent, false);
        TreeInfoViewHolder holder = new TreeInfoViewHolder(v);

        return holder;
    }

    public TreeInfoAdapter(ArrayList<Tree> treeList){
        mList = treeList;
    }

    @Override
    public void onBindViewHolder(@NonNull TreeInfoViewHolder holder, int position) {

        Tree currentTree = mList.get(position);

        holder.mImageView.setImageResource(R.drawable.bonsai);
        holder.mTreeInfoDate.setText(currentTree.getCreated().toString());
        holder.mTreeInfoDescription.setText(currentTree.getTreeDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
