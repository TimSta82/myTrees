package com.example.tstaats.mytrees;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class TreeDetailAdapter extends RecyclerView.Adapter<TreeDetailAdapter.TreeInfoViewHolder> {

    //private ArrayList<Tree> mList;
    private ArrayList<TreeState> mList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class TreeInfoViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTreeInfoDate;
        public TextView mTreeInfoDescription;


        public TreeInfoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_info);
            mTreeInfoDate = itemView.findViewById(R.id.text_tree_info_date);
            mTreeInfoDescription = itemView.findViewById(R.id.text_tree_info_description);

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

    @NonNull
    @Override
    public TreeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_info, parent, false);
        TreeInfoViewHolder holder = new TreeInfoViewHolder(v, mListener);

        return holder;
    }

    public TreeDetailAdapter(ArrayList<TreeState> treeInfoList, ImageLoader loader){
        mList = treeInfoList;
        mImageLoader = loader;
    }

    @Override
    public void onBindViewHolder(@NonNull TreeInfoViewHolder holder, int position) {

        //Tree currentTree = mList.get(position);
        TreeState currentTree = mList.get(position);

//        if (mList.size() > 1){
//            holder.mImageView.setImageResource(R.drawable.bonsai);
//        } else {
            //mImageLoader.displayImage(mList.get(position).getTreeImageUrl(), holder.mImageView);
            mImageLoader.displayImage(mList.get(position).getTreeStateImageUrl(), holder.mImageView);
//        }
        holder.mTreeInfoDate.setText(currentTree.getCreated().toString());
        holder.mTreeInfoDescription.setText(currentTree.getTreeStateDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
