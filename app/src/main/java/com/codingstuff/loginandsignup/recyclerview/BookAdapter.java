package com.codingstuff.loginandsignup.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codingstuff.loginandsignup.Activity.BookDetailsActivity;
import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.bookviewholder> {

    List<DataClass> list;

    private Context context;

    public BookAdapter(List<DataClass> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book,parent,false);

        return new bookviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookviewholder holder, int position) {
        DataClass model = list.get(position);

        // bind book item data here
        // load book img using glide

        Glide.with(context)
                .load(list.get(position).getDataImage())
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(holder.dataImage);


        holder.nmBuku.setText(model.getNmBuku());
        holder.nmPnls.setText(model.getNmPnls());
        holder.halaman.setText((model.getHal())+" halaman");
        //holder.nmPenerbit.setText(model.getNmPnls());
        //holder.kdBuku.setText(model.getKdBuku());
        //holder.jumlah.setText(model.getJumlah());
        holder.ratingBar.setRating((float) 4.5);

        holder.imgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookDetailsActivity.class);
                intent.putExtra("dataImage", list.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("nmBuku", list.get(holder.getAdapterPosition()).getNmBuku());
                intent.putExtra("nmPnls", list.get(holder.getAdapterPosition()).getNmPnls());
                intent.putExtra("nmPenerbit", list.get(holder.getAdapterPosition()).getNmPenerbit());
                intent.putExtra("Key", list.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("desc", list.get(holder.getAdapterPosition()).getDesc());
                intent.putExtra("hal", list.get(holder.getAdapterPosition()).getHal());
                intent.putExtra("kdBuku", list.get(holder.getAdapterPosition()).getKdBuku());
                intent.putExtra("jumlah", list.get(holder.getAdapterPosition()).getJumlah());



                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        list = searchList;
        notifyDataSetChanged();
    }
    public static class bookviewholder extends RecyclerView.ViewHolder {
        ImageView dataImage,imgFav,imgContainer;
        TextView nmBuku,nmPenerbit,nmPnls,kdBuku,rate,jumlah,halaman;
        RatingBar ratingBar;
        public bookviewholder(@NonNull View itemView) {
            super(itemView);

            dataImage = itemView.findViewById(R.id.item_book_img);
            imgContainer = itemView.findViewById(R.id.container0);
            nmBuku = itemView.findViewById(R.id.item_book_title);
            nmPnls = itemView.findViewById(R.id.item_pjm_tglPjm);
            halaman = itemView.findViewById(R.id.item_pjm_tglPengembalian);
            rate = itemView.findViewById(R.id.item_pjm_status);
            ratingBar = itemView.findViewById(R.id.item_book_ratingbar);






        }
    }
}
