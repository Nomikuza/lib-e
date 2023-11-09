package com.codingstuff.loginandsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.databinding.ActivityPdfAddBinding;
import com.codingstuff.loginandsignup.recyclerview.BookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadBukuActivity extends AppCompatActivity {

    private EditText kdBukuEdt,nmBukuEdt,nmPnlsEdt,nmPenerbitEdt,jumlahEdt,halEdt,descEdt;
    private Button btnBuku, btnBack;
    private ImageButton pdfAttach;
    private TextView categoryTv;
    private List<DataClass> mdata;
    private ArrayList<String> categoryTitleArrayList, categoryIdArrayList;
    private String imageURL, key;
    private Uri uri, pdfUri;
    private ImageView uploadImage;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private BookAdapter adapter;
    private ActivityPdfAddBinding binding;
    private static final int PDF_PICK_CODE = 1000;
    private static final String TAG = "ADD_PDF_TAG";
    private ProgressDialog progressDialog;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        kdBukuEdt = findViewById(R.id.kdBukuEdt);
        nmBukuEdt = findViewById(R.id.nmBukuEdt);
        nmPnlsEdt = findViewById(R.id.nmPnlsEdt);
        nmPenerbitEdt = findViewById(R.id.nmPenerbitEdt);
        jumlahEdt = findViewById(R.id.jumlahEdt);
        halEdt = findViewById(R.id.HalamanET);
        descEdt = findViewById(R.id.DescET);
        uploadImage = findViewById(R.id.uploadImage);
        btnBuku = findViewById(R.id.addbookET);
        categoryTv = findViewById(R.id.categoryTv);
        pdfAttach = findViewById(R.id.pdfAttach);
        //progressDialog = find

        firebaseAuth = FirebaseAuth.getInstance();
        //autosetKdBuku();
        loadPdfCategories();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadBukuActivity.this, "Tidak ada gambar yang terpilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        binding.pdfAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdfPickIntent();
            }
        });
        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPdfToStorage();
                //validateData();
                //saveData();
                //startActivity(new Intent(UploadBukuActivity.this, ShowBukuActivity.class));


            }
        });
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryPickDialog();
            }
        });
    }

    private void loadPdfCategories() {
        Log.d(TAG,"pdfPickIntent: starting pdf pick intent");
        categoryTitleArrayList = new ArrayList<>();
        categoryIdArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Android Category");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIdArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String categoryId = ""+ds.child("id").getValue();
                    String categoryTitle = ""+ds.child("category").getValue();

                    categoryTitleArrayList.add(categoryTitle);
                    categoryIdArrayList.add(categoryId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String selectedCategoryId, selectedCategoryTitle;
    private void categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: showing category pick dialog");

        String[] categoriesArray = new String[categoryTitleArrayList.size()];
        for (int i = 0; i< categoryTitleArrayList.size(); i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategoryTitle = categoryTitleArrayList.get(which);
                        selectedCategoryId = categoryIdArrayList.get(which);
                        categoryTv.setText(selectedCategoryTitle);

                        Log.d(TAG, "onClick: Selected Category" +selectedCategoryId+" "+selectedCategoryTitle);
                    }
                })
                .show();
    }

    private String title= "", description= "", category= "";
    private void validateData(){
        String kdbuku = kdBukuEdt.getText().toString();
        String nmbuku = nmBukuEdt.getText().toString();
        String nmpnls = nmPnlsEdt.getText().toString();
        String nmpnrbt = nmPenerbitEdt.getText().toString();
        String jmlh = jumlahEdt.getText().toString();
        String hal = halEdt.getText().toString();
        String desc = descEdt.getText().toString();
        title = binding.nmBukuEdt.getText().toString().trim();
        description = binding.DescET.getText().toString().trim();
        category = binding.categoryTv.getText().toString().trim();

        
        if (TextUtils.isEmpty(kdbuku)) {
            Toast.makeText(UploadBukuActivity.this, "Tolong klik gambar untuk menambahkan foto buku! ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(nmbuku)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(nmpnls)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(nmpnrbt)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(jmlh)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(hal)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(desc)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(selectedCategoryTitle)) {
            Toast.makeText(UploadBukuActivity.this, "Ketik nama buku ", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(UploadBukuActivity.this, "Tolong klik attach pdf untuk menambahkan pdf! ", Toast.LENGTH_SHORT).show();
        } else {
            uploadPdfToStorage();

            //saveData();
            
        }
    }

    private void uploadPdfToStorage() {
        Log.d(TAG,"uploadPdfToStorage: uploading pdf to storage");

        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Books/" + timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: PDF uploaded to storage ");
                        Log.d(TAG, "onSuccess: getting pdf url ");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadedPdfUrl = ""+uriTask.getResult();
                        
                        uploadedPdfUrl(uploadedPdfUrl, timestamp);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: PDF upload failed due to: " +e.getMessage());
                        Toast.makeText(UploadBukuActivity.this, "PDF gagal diupload dikarenakan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadedPdfUrl(String uploadedPdfUrl, long timestamp) {
        String desc = descEdt.getText().toString();
        String nmbuku = nmBukuEdt.getText().toString();
        Log.d(TAG,"uploadPdfToStorage: uploading pdf into to firebase db");
        String uid = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+uid);
        hashMap.put("id", ""+timestamp);
        hashMap.put("title", ""+nmbuku);
        hashMap.put("description", ""+desc);
        hashMap.put("categoryId", ""+selectedCategoryId);
        hashMap.put("url", ""+uploadedPdfUrl);
        hashMap.put("timestamp", +timestamp);
        hashMap.put("viewsCount", +0);
        hashMap.put("downloadsCount", +0);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, "onSuccess: PDF upload successfully");
                        Toast.makeText(UploadBukuActivity.this, "Upload berhasil", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to upload to db due to: " +e.getMessage());
                        Toast.makeText(UploadBukuActivity.this, "PDF gagal diupload dikarenakan "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pdfPickIntent() {
        Log.d(TAG, "pdfPickIntent: starting pdf pick intent");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == PDF_PICK_CODE){
                Log.d(TAG, "onActivityResult: PDF Picked");

                pdfUri = data.getData();

                Log.d(TAG, "onActivityResult: URI: "+pdfUri);

            }
        }
        else {
            Log.d(TAG,"onActivityResult: cancelled picking pdf");
            Toast.makeText(this,"cancelled picking pdf", Toast.LENGTH_SHORT).show();
        }
    }

    private void autosetKdBuku() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Android Tutorials");
            Query lastQuery = dbRef.orderByKey().limitToLast(1);
            lastQuery.addListenerForSingleValueEvent(new ValueEventListener()   {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren())
                {
                        String tanggal = snapshot.getKey();


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public  void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadBukuActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                //pending

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadBukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void uploadData() {

        String kdbuku = kdBukuEdt.getText().toString();
        String nmbuku = nmBukuEdt.getText().toString();
        String nmpnls = nmPnlsEdt.getText().toString();
        String nmpnrbt = nmPenerbitEdt.getText().toString();
        String jmlh = jumlahEdt.getText().toString();
        String hal = halEdt.getText().toString();
        String desc = descEdt.getText().toString();

        //sesuai urutan DataClass
        DataClass dataClass = new DataClass(imageURL,desc,hal,jmlh,kdbuku,nmbuku,nmpnrbt,nmpnls);
        //String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("Android Tutorials").child(kdbuku)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UploadBukuActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadBukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
