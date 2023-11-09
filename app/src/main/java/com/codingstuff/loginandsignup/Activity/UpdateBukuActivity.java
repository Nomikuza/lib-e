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

public class UpdateBukuActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateDesc, updateNmBuku, updateNmPnls, updateKdBuku, updateNmPenerbit, updateJumlah, updateHal;
    String nmBuku, desc, nmPnls, kdBuku, nmPenerbit, jumlah, hal;
    private String imageUrl;
    String  key, oldImageURL;
    Intent photoPicker;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buku_update);

        updateButton = findViewById(R.id.updatebuttonET);
        updateDesc = findViewById(R.id.updateDescET);
        updateImage = findViewById(R.id.updateImage);
        updateNmBuku = findViewById(R.id.updateNmbukuET);
        updateNmPnls = findViewById(R.id.updateNmpnlsET);
        updateKdBuku = findViewById(R.id.updateKdbukuET);
        updateNmPenerbit = findViewById(R.id.updatePenerbitET);
        updateJumlah = findViewById(R.id.updateJumlahET);
        updateHal = findViewById(R.id.updateHalamanET);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateBukuActivity.this, "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            //Glide.with(UpdateBukuActivity.this).load(bundle.getString("dataImage")).into(updateImage);
            updateKdBuku.setText(bundle.getString("kdBuku"));
            updateNmBuku.setText(bundle.getString("nmBuku"));
            updateJumlah.setText(bundle.getString("Jumlah"));
            updateDesc.setText(bundle.getString("desc"));
            updateNmPenerbit.setText(bundle.getString("nmPenerbit"));
            updateNmPnls.setText(bundle.getString("nmPnls"));
            updateHal.setText(bundle.getString("hal"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Images");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  pending
                saveData();
                Intent intent = new Intent(UpdateBukuActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });
    }
    public void saveData(){
        storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBukuActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();


        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                //  pending
                updateData();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateBukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateData(){

        nmBuku = updateNmBuku.getText().toString().trim();
        nmPnls = updateNmPnls.getText().toString().trim();
        nmPenerbit = updateNmPenerbit.getText().toString().trim();
        jumlah = updateJumlah.getText().toString().trim();
        hal = updateHal.getText().toString().trim();
        desc = updateDesc.getText().toString().trim();
        kdBuku = updateKdBuku.getText().toString().trim();

        //sesuai urutan Upload
        DataClass dataClass = new DataClass(imageUrl,desc,hal,jumlah,kdBuku,nmBuku,nmPenerbit,nmPnls);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateBukuActivity.this,"Diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateBukuActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}