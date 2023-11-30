package com.codingstuff.loginandsignup.Activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.codingstuff.loginandsignup.Domain.ModelComment;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.databinding.ActivityPdfDetailuserBinding;
import com.codingstuff.loginandsignup.databinding.DialogCommentAddBinding;
import com.codingstuff.loginandsignup.recyclerview.AdapterComment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PdfDetailUserActivity extends AppCompatActivity {
    
    private ActivityPdfDetailuserBinding binding;
    private static final String TAG_DOWNLOAD = "DOWNLOAD_TAG";
    String bookId, bookTitle, bookUrl;
    boolean isInMyFavorite = false;
    String isInMyPermission = "";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<ModelComment> commentArrayList;
    private AdapterComment adapterComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailuserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");

        binding.downloadBookBtn.setVisibility(View.GONE);
        binding.readBookBtn.setVisibility(View.GONE);
        binding.permissionBtn.setVisibility(View.GONE);
        binding.fabLocation.hideMenu(true);

        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tolong tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();

//        checkUser();

        checkPermission();
        checkIsFavorite();
        loadBookDetails();
        loadComments();
        //
        MyApplication.incrementBookViewCount(bookId);

        binding.permissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.askPermission(PdfDetailUserActivity.this, bookId);
            }
        });

        binding.readBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PdfDetailUserActivity.this, PdfViewActivity.class);
                intent1.putExtra("bookId", bookId);
                startActivity(intent1);
            }
        });
        binding.downloadBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions();
                Log.d(TAG_DOWNLOAD, "onClick: Checking permission");
                if (ContextCompat.checkSelfPermission(PdfDetailUserActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG_DOWNLOAD, "onClick: Permission already Granted, can download book");
                    MyApplication.downloadBook(PdfDetailUserActivity.this, ""+bookId, ""+bookTitle, ""+bookUrl);
                }
                else {
                    Log.d(TAG_DOWNLOAD, "onClick: Permission was not Granted, request permission...");
                    requestPermissionLauncher.launch(WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        //
        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInMyFavorite){
                    //
                    MyApplication.removeFromFavorite(PdfDetailUserActivity.this, bookId);
                }
                else {
                    //
                    MyApplication.addToFavorite(PdfDetailUserActivity.this, bookId);
                }
            }
        });

        //
        binding.addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentDialog();
            }
        });
    }

    private void askPermission1() {
        String timestamp = ""+System.currentTimeMillis();
        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+uid);
        hashMap.put("id", ""+timestamp);
        hashMap.put("bookId", ""+bookId);
//        hashMap.put("title", ""+nmbuku);
//        hashMap.put("description", ""+desc);
//        hashMap.put("categoryId", ""+selectedCategoryId);
        hashMap.put("permission", ""+"false");
        hashMap.put("timestamp", ""+timestamp);
//        hashMap.put("viewsCount", +0);
//        hashMap.put("downloadsCount", +0);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).child("Permissions").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("CHECK_PERMISSION", "onSuccess: Ask permission sent successfully");
                        Toast.makeText(PdfDetailUserActivity.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("CHECK_PERMISSION", "onFailure: Failed to upload permission to db due to: " +e.getMessage());
                        Toast.makeText(PdfDetailUserActivity.this, "PDF gagal diupload dikarenakan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkPermission() {
        //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Permissions");
        reference.child(firebaseAuth.getUid()).child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        isInMyPermission = String.valueOf(snapshot.child("permission").getValue());
                        Toast.makeText(PdfDetailUserActivity.this, ""+isInMyPermission, Toast.LENGTH_SHORT).show();
                        if (isInMyPermission.equals("true")){
                            //
                            binding.permissionBtn.setVisibility(View.GONE);
                            binding.downloadBookBtn.setVisibility(View.VISIBLE);
                            binding.readBookBtn.setVisibility(View.VISIBLE);
                        }
                        else if (isInMyPermission.equals("false")){
                            //
                            Toast.makeText(PdfDetailUserActivity.this, "Peminjaman masih pending", Toast.LENGTH_SHORT).show();
                            binding.permissionBtn.setVisibility(View.GONE);
                        }
                        else if (isInMyPermission.equals("null")){
                            binding.permissionBtn.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void checkUser() {
        //
        //
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            //
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {  //idk
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            //
                            String userType = "" + snapshot.child("userType").getValue();
                            //
                            if (userType.equals("user")) {
                                //
                                binding.fabLocation.hideMenu(true);
                                binding.permissionBtn.setVisibility(View.VISIBLE);

                            } else if (userType.equals("admin")) {
                                binding.readBookBtn.setVisibility(View.VISIBLE);
                                binding.permissionBtn.setVisibility(View.GONE);
                                //

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

    }

    private void loadComments() {
        //
        commentArrayList = new ArrayList<>();

        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).child("Comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //
                        commentArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            //
                            ModelComment model = ds.getValue(ModelComment.class);
                            //
                            commentArrayList.add(model);
                        }
                        //
                        adapterComment = new AdapterComment(PdfDetailUserActivity.this, commentArrayList);
                        //
                        binding.commentsRv.setAdapter(adapterComment);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String comment = "";

    private void addCommentDialog() {
        //
        DialogCommentAddBinding commentAddBinding = DialogCommentAddBinding.inflate(LayoutInflater.from(this));

        //
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        builder.setView(commentAddBinding.getRoot());

        //
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //
        commentAddBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //
        commentAddBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                comment = commentAddBinding.commentEt.getText().toString().trim();
                //
                if (TextUtils.isEmpty(comment)){
                    Toast.makeText(PdfDetailUserActivity.this, "Ketik komen disini...", Toast.LENGTH_SHORT).show();
                }
                else {
                    alertDialog.dismiss();
                    addComment();
                }
            }
        });
    }

    private void addComment() {
        //
        progressDialog.setMessage("Menambahkan komentar...");
        progressDialog.show();

        //
        String timestamp = ""+System.currentTimeMillis();

        //
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("bookId", ""+bookId);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("comment", ""+comment);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //
        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).child("Comments").child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PdfDetailUserActivity.this, "Komentar telah ditambahkan...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //
                        progressDialog.dismiss();
                        Toast.makeText(PdfDetailUserActivity.this, "Komentar gagal ditambahkan dikarenakan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storage_permissions_33;
        } else {
            p = storage_permissions;
        }
        return p;
    }
//    private boolean checkPermission() {
//        if (SDK_INT >= Build.VERSION_CODES.R) {
//            return Environment.isExternalStorageManager();
//        } else {
//            int result = ContextCompat.checkSelfPermission(PermissionActivity.this, READ_EXTERNAL_STORAGE);
//            int result1 = ContextCompat.checkSelfPermission(PermissionActivity.this, WRITE_EXTERNAL_STORAGE);
//            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//        }
//    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    Log.d(TAG_DOWNLOAD, "Permission Granted");
                    MyApplication.downloadBook(this,""+bookId, ""+bookTitle, ""+bookUrl);
                }
                else {
                    Log.d(TAG_DOWNLOAD, "Permission was denied...: ");
                    Toast.makeText(this, "Permission was denied...", Toast.LENGTH_SHORT).show();
                }
            });
    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookTitle = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String categoryId = ""+snapshot.child("categoryId").getValue();
                        String viewsCount = ""+snapshot.child("viewsCount").getValue();
                        String downloadsCount = ""+snapshot.child("downloadsCount").getValue();
                        bookUrl = ""+snapshot.child("url").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();

//                        binding.downloadBookBtn.setVisibility(View.VISIBLE);

                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                        MyApplication.loadCategory(
                                ""+categoryId,
                                binding.categoryTv
                        );
                        MyApplication.loadPdfFromUrl(
                                ""+bookUrl,
                                ""+bookTitle,
                                binding.pdfView,
                                binding.progressBar,
                                binding.pagesTv
                        );
                        MyApplication.loadPdfSize(
                                ""+bookUrl,
                                ""+bookTitle,
                                binding.sizeTv
                        );

                        binding.titleTv.setText(bookTitle);
                        binding.detailsDesc.setText(description);
                        binding.viewsTv.setText(viewsCount.replace("null","N/A"));
                        binding.downloadsTv.setText(downloadsCount.replace("null","N/A"));
                        binding.dateTv.setText(date);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkIsFavorite(){
        //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Favorites").child(bookId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInMyFavorite = snapshot.exists(); //
                        if (isInMyFavorite){
                            //
                            binding.favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.favoritepress, 0, 0);
                            binding.favoriteBtn.setText("Hapus Favorit");
                        }
                        else {
                            //
                            binding.favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.favoriteori, 0, 0);
                            binding.favoriteBtn.setText("Tambah Favorit");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}