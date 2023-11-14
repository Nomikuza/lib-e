package com.codingstuff.loginandsignup.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.loginandsignup.Activity.MyApplication;
import com.codingstuff.loginandsignup.Activity.PdfDetailActivity;
import com.codingstuff.loginandsignup.Domain.ModelPDF;
import com.codingstuff.loginandsignup.Filter.FilterPdfUser;
import com.codingstuff.loginandsignup.databinding.ItemBookUserBinding;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfUser extends RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {

    private Context context;
    public ArrayList<ModelPDF> pdfArrayList, filterlist;
    private FilterPdfUser filter;
    private ItemBookUserBinding binding;
    private static final String TAG = "ADAPTER_PDF_USER_TAG";

    public AdapterPdfUser(Context context, ArrayList<ModelPDF> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filterlist = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        binding = ItemBookUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {
        ModelPDF model = pdfArrayList.get(position);
        String bookId = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String pdfUrl = model.getUrl();
        String categoryId = model.getCategoryId();
        long timestamp = model.getTimestamp();

        //
        String date = MyApplication.formatTimestamp(timestamp);

        //
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(date);

        MyApplication.loadCategory(
                ""+categoryId,
                holder.categoryTv
        );
        MyApplication.loadPdfFromUrl(
                ""+pdfUrl,
                ""+title,
                holder.pdfView,
                holder.progressBar
        );
        MyApplication.loadPdfSize(
                ""+pdfUrl,
                ""+title,
                holder.sizeTv
        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfDetailActivity.class);
                intent.putExtra("bookId", bookId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new FilterPdfUser(filterlist, this);
        }
        return filter;
    }

    class HolderPdfUser extends RecyclerView.ViewHolder{

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, sizeTv, dateTv;
        ImageView dataImage, imgFav, moreBtn;
        RatingBar ratingBar;

        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);
            pdfView = binding.pdfView;
            progressBar = binding.progressBar;
            titleTv = binding.itemBookTitle;
            descriptionTv = binding.itemBookStatus;
            moreBtn = binding.container0;
            categoryTv = binding.itemPjmCategory;
            dateTv = binding.itemPjmTglPengembalian;
            sizeTv = binding.itemBookSize;
        }
    }
}
