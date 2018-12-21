package com.example.tstaats.mytrees;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.TreeListViewHolder> {

    private static final String TAG = "TreeListAdapter";

    private List<Tree> mTreeList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class TreeListViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTreeName;
        public TextView mTreeDate;

        public TreeListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_tree_thumb);
            mTreeName = itemView.findViewById(R.id.text_tree_name);
            mTreeDate = itemView.findViewById(R.id.text_tree_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public TreeListAdapter(List<Tree> trees, ImageLoader loader){
        mTreeList = trees;
        mImageLoader = loader;
    }

    @NonNull
    @Override
    public TreeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        TreeListViewHolder holder = new TreeListViewHolder(v, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TreeListViewHolder holder, int position) {

        Tree currentTree = mTreeList.get(position);

        mImageLoader.displayImage(mTreeList.get(position).getTreeImageUrl(), holder.mImageView);
        holder.mTreeName.setText(currentTree.getTreeName());
        holder.mTreeDate.setText(currentTree.getCreated().toString());

    }

    @Override
    public int getItemCount() {
        return mTreeList.size();
    }
}
