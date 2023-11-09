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

import com.codingstuff.loginandsignup.Activity.PeminjamanDetailsActivity;
import com.codingstuff.loginandsignup.Activity.ShowBukuActivity;
import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.Domain.PeminjamanModel;
import com.codingstuff.loginandsignup.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.bookviewholder>{

    List<PeminjamanModel> list;

    private Context context;

    public PeminjamanAdapter(List<PeminjamanModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public bookviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_peminjaman,parent,false);

        return new bookviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bookviewholder holder, int position) {
        PeminjamanModel model = list.get(position);

        // bind book item data here
        // load book img using glide

        holder.kdPeminjaman.setText("Kode Peminjaman " + model.getKdPeminjaman());
        holder.tglPjm.setText(model.getTglPeminjaman());
        holder.tglPengembalian.setText(("Terakhir "+model.getTglPengembalian()));
        holder.nim.setText(model.getNim());
        holder.status.setText(model.getStatus());
        //holder.nmPenerbit.setText(model.getkdPeminjaman());
        //holder.tglPjm.setText(model.gettglPjm());
        //holder.jumlah.setText(model.getJumlah());

        holder.imgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PeminjamanDetailsActivity.class);
                intent.putExtra("kdPeminjaman", list.get(holder.getAdapterPosition()).getKdPeminjaman());
                intent.putExtra("kdBuku", list.get(holder.getAdapterPosition()).getKdBuku());
                intent.putExtra("tglPjm", list.get(holder.getAdapterPosition()).getTglPeminjaman());
                intent.putExtra("nim", list.get(holder.getAdapterPosition()).getNim());
                intent.putExtra("tglPeminjaman", list.get(holder.getAdapterPosition()).getTglPeminjaman());
                intent.putExtra("tglPengembalian", list.get(holder.getAdapterPosition()).getTglPengembalian());
                intent.putExtra("key", list.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("status", list.get(holder.getAdapterPosition()).getStatus());
                //intent.putExtra("qr", list.get(holder.getAdapterPosition()).getQr());
                //belum ditambahkan QR!

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchDataList(ArrayList<PeminjamanModel> searchList){
        list = searchList;
        notifyDataSetChanged();
    }
    public static class bookviewholder extends RecyclerView.ViewHolder {
        ImageView dataImage,imgFav,imgContainer;
        TextView tglPjm,kdPeminjaman,status,nim,tglPengembalian;
        RatingBar ratingBar;
        public bookviewholder(@NonNull View itemView) {
            super(itemView);

            //dataImage = itemView.findViewById(R.id.item_book_img);
            imgContainer = itemView.findViewById(R.id.container0);
            kdPeminjaman = itemView.findViewById(R.id.item_pjm_kdPjm);
            tglPjm = itemView.findViewById(R.id.item_pjm_tglPjm);
            nim = itemView.findViewById(R.id.item_pjm_nim);
            tglPengembalian = itemView.findViewById(R.id.item_pjm_tglPengembalian);
            status = itemView.findViewById(R.id.item_pjm_status);
            //ratingBar = itemView.findViewById(R.id.item_book_ratingbar);






        }
    }
}

