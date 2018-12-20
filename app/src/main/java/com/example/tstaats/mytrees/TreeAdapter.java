package com.example.tstaats.mytrees;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class TreeAdapter extends ArrayAdapter<Tree> {

    // Context is the class that will be using this adapter
    private Context context;
    private List<Tree> trees;
    private ImageLoader imageLoader;

    public TreeAdapter(Context context, List<Tree> list, ImageLoader imageLoader){
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.trees = list;
        this.imageLoader = imageLoader;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_layout, parent, false);



        ImageView ivTreeThumb = convertView.findViewById(R.id.image_tree_thumb);
        TextView tvTreeName = convertView.findViewById(R.id.text_tree_name);
        TextView tvTreeDate = convertView.findViewById(R.id.text_tree_date);

        imageLoader.displayImage(trees.get(position).getTreeImageUrl(), ivTreeThumb);

        tvTreeName.setText(trees.get(position).getTreeName());
        tvTreeDate.setText(trees.get(position).getCreated().toString());


        return convertView;

    }
}
