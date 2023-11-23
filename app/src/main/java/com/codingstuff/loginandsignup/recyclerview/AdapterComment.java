package com.codingstuff.loginandsignup.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingstuff.loginandsignup.Activity.MyApplication;
import com.codingstuff.loginandsignup.Domain.ModelComment;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.databinding.RowCommentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



    //
    public class  AdapterComment extends RecyclerView.Adapter<AdapterComment.HolderComment> {

        //
        private Context context;
        //
        private ArrayList<ModelComment> commentArrayList;
        //
        private FirebaseAuth firebaseAuth;
        private RowCommentBinding binding;

        //cons
        public AdapterComment(Context context, ArrayList<ModelComment> commentArrayList) {
            this.context = context;
            this.commentArrayList = commentArrayList;

            firebaseAuth = FirebaseAuth.getInstance();
        }

        @NonNull
        @Override
        public HolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //
            binding = RowCommentBinding.inflate(LayoutInflater.from(context), parent, false);

            return new HolderComment(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull HolderComment holder, int position) {
//

//
            ModelComment modelComment = commentArrayList.get(position);
            String id = modelComment.getId();
            String bookId = modelComment.getBookId();
            String comment = modelComment.getComment();
            String uid = modelComment.getUid();
            String timestamp = modelComment.getTimestamp();

            //
            String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

            //
            holder.dateTv.setText(date);
            holder.commentTv.setText(comment);

            //
            loadUserDetails(modelComment, holder);

            //
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firebaseAuth.getCurrentUser() != null && uid.equals(firebaseAuth.getUid())){
                        deleteComment(modelComment, holder);
                    }
                }
            });
        }
        private void deleteComment(ModelComment modelComment, AdapterComment.HolderComment holder) {
            //
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Hapus Komentar")
                    .setMessage("Apakah anda yakin untuk menghapus komentar ini?")
                    .setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
                            ref.child(modelComment.getBookId())
                                    .child("Comments")
                                    .child(modelComment.getId())    //
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Terhapus...", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Gagal menghapus dikarenakan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            //
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }

        @Override
        public int getItemCount() {
            return commentArrayList.size(); //
        }

        private void loadUserDetails(ModelComment modelComment, HolderComment holder) {
            String uid = modelComment.getUid();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //
                            String name = ""+snapshot.child("name").getValue();
                            String profileImage = ""+snapshot.child("profileImage").getValue();

                            //
                            holder.nameTv.setText(name);
                            try {
                                Glide.with(context)
                                        .load(profileImage)
                                        .placeholder(R.drawable.profile)
                                        .into(holder.profileTv);
                            }
                            catch (Exception e){
                                holder.profileTv.setImageResource(R.drawable.profile);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        //
        class HolderComment extends RecyclerView.ViewHolder{

            //
            ShapeableImageView profileTv;
            TextView nameTv, dateTv, commentTv;

            public HolderComment(@NonNull View itemView) {
                super(itemView);

                //
                profileTv = binding.profileTv;
                nameTv = binding.nameTv;
                dateTv = binding.dateTv;
                commentTv = binding.commentTv;
            }
        }
    }



