package com.example.duy26.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.RecyclerViewHolder> {

    Bitmap.Config config = Bitmap.Config.ARGB_4444;
    private List<Student> students = new ArrayList<>();
    Context context;
    public StudentAdapter(List<Student> students,Context context) {
        this.students = students;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_recyclerview, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(students.get(position).getName());
        String i = students.get(position).getImageView();
        try {
            if (i.contains("http://")) {
                Glide.with(context)
                        .load(i)
                        .into(viewHolder.ivDelete);
            } else Glide.with(context)
                    .load(decodeBase64(i))

                    .into(viewHolder.ivDelete);
        }catch (Exception e){}
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivDelete;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

            }
    }
    public static Bitmap decodeBase64(String input)
    {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds =false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        byte[] decodedBytes = Base64.decode(input,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length,options);
    }


}

