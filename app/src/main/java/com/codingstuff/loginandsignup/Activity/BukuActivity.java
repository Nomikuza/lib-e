package com.codingstuff.loginandsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.Adapter.CourseRVModal;
import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BukuActivity extends AppCompatActivity {

    private EditText kdBukuEdt,nmBukuEdt,nmPnlsEdt,nmPenerbitEdt,jumlahEdt;
    private Button btnBuku, btnBack;
    private String imageURL;
    private Uri uri;
    private ImageView uploadImage;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);
        kdBukuEdt = findViewById(R.id.KdbukuET);
        nmBukuEdt = findViewById(R.id.NmbukuET);
        nmPnlsEdt = findViewById(R.id.NmpnlsET);
        nmPenerbitEdt = findViewById(R.id.PenerbitET);
        jumlahEdt = findViewById(R.id.JumlahET);
        uploadImage = findViewById(R.id.uploadImage);
        btnBuku = findViewById(R.id.addbookET);
        btnBack = findViewById(R.id.btnBack);

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
                            Toast.makeText(BukuActivity.this, "Tidak ada gambar yang terpilih", Toast.LENGTH_SHORT).show();
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBackPressed();
            }
        });

        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
                saveData();

            }
        });
    }

    public  void saveData(){

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(BukuActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                //pending
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
    public void uploadData() {

        String kdbuku = kdBukuEdt.getText().toString();
        String nmbuku = nmBukuEdt.getText().toString();
        String nmpnls = nmPnlsEdt.getText().toString();
        String nmpnrbt = nmPenerbitEdt.getText().toString();
        String jmlh = jumlahEdt.getText().toString();

        DataClass dataClass = new DataClass(kdbuku,nmbuku,nmpnls,nmpnrbt,jmlh,imageURL);

        FirebaseDatabase.getInstance().getReference("Android Tutorials").child(kdbuku)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(BukuActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
