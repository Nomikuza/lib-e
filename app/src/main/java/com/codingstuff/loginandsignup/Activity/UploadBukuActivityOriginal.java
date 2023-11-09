package com.codingstuff.loginandsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.codingstuff.loginandsignup.Domain.DataClass;
import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.recyclerview.BookAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class UploadBukuActivityOriginal extends AppCompatActivity {

    private EditText kdBukuEdt,nmBukuEdt,nmPnlsEdt,nmPenerbitEdt,jumlahEdt,halEdt,descEdt;
    private Button btnBuku, btnBack;
    private List<DataClass> mdata;
    private String imageURL, key;
    private Uri uri;
    private ImageView uploadImage;
    FirebaseDatabase firebaseDatabase;
    private BookAdapter adapter;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku);
        kdBukuEdt = findViewById(R.id.kdBukuEdt);
        nmBukuEdt = findViewById(R.id.nmBukuEdt);
        nmPnlsEdt = findViewById(R.id.nmPnlsEdt);
        nmPenerbitEdt = findViewById(R.id.nmPenerbitEdt);
        jumlahEdt = findViewById(R.id.jumlahEdt);
        halEdt = findViewById(R.id.HalamanET);
        descEdt = findViewById(R.id.DescET);
        uploadImage = findViewById(R.id.uploadImage);
        btnBuku = findViewById(R.id.addbookET);

        //autosetKdBuku();

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
                            Toast.makeText(UploadBukuActivityOriginal.this, "Tidak ada gambar yang terpilih", Toast.LENGTH_SHORT).show();
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

        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
                //saveData();
                startActivity(new Intent(UploadBukuActivityOriginal.this, ShowBukuActivity.class));

            }
        });
    }

    private void validateData(){
        if (TextUtils.isEmpty((CharSequence) uri)){
            Toast.makeText(UploadBukuActivityOriginal.this, "Tolong klik gambar untuk menambahkan foto buku! ", Toast.LENGTH_SHORT).show();
        } else {
            saveData();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadBukuActivityOriginal.this);
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
                Toast.makeText(UploadBukuActivityOriginal.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(UploadBukuActivityOriginal.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadBukuActivityOriginal.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
