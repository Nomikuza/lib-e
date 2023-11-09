package com.codingstuff.loginandsignup.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.codingstuff.loginandsignup.Domain.PeminjamanModel;
import com.codingstuff.loginandsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadPeminjamanActivity extends AppCompatActivity {

    private EditText kdBukuEdt, kdPeminjamanEdt, nimEdt, tglPjmEdt, tglKblEdt;
    private Button btnBuku, btnQr;
    private ImageView qrImage;
    private DataSnapshot dataSnapshot;
    private String imageUrl;
    private Uri uri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);
        kdPeminjamanEdt = findViewById(R.id.kdPeminjamanPjm);
        kdBukuEdt = findViewById(R.id.kdBukuPjm);
        nimEdt = findViewById(R.id.nimPjm);
        tglPjmEdt = findViewById(R.id.tglPeminjamanPjm);
        tglKblEdt = findViewById(R.id.tglPengembalianPjm);
        qrImage = findViewById(R.id.qrPjm);
        btnBuku = findViewById(R.id.addpeminjamanPjm);
        btnQr = findViewById(R.id.addpeminjamanPjm2);

        tglPjmEdt.setClickable(false);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                Bundle extras = result.getData().getExtras();
                Uri imageUri;
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                WeakReference<Bitmap> result1 = new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap,
                        imageBitmap.getHeight(), imageBitmap.getWidth(), false).copy(
                        Bitmap.Config.RGB_565, true));

                Bitmap bm = result1.get();
                imageUri = saveImage(bm, UploadPeminjamanActivity.this);
                qrImage.setImageURI(imageUri);
            }
        });

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(kdPeminjamanEdt.getText().toString(), BarcodeFormat.QR_CODE, 300, 300);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    qrImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadData();
                //saveData();
                startActivity(new Intent(UploadPeminjamanActivity.this, ShowPeminjamanActivity.class));
            }
        });
    }



    private Uri saveImage(Bitmap image, Context context) {

        File imagesFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "captured_image.jpg");
            FileOutputStream stream=new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context.getApplicationContext(), "com.codingstuff.loginandsignup"+".provider",file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }


    private void autoUpdateTanggal(){
        tglPjmEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date dateAndTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                String date = dateFormat.format(dateAndTime);
                tglPjmEdt.setText(date);
            }
        });
        tglKblEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        autoUpdateTanggal();
    }

    private long getCurrentDateInMiliseconds() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private void openDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tglKblEdt.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(day));
            }
        }, 2023, 10, 15);
        dialog.show();
    }

    private String[] longIntoString(long milliseconds) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        return new String[]{dateFormat.format(milliseconds), timeFormat.format(milliseconds)};
    }

    public  void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android QR")
                    .child(uri.getLastPathSegment());

        // Get the data from an ImageView as bytes
        qrImage.setDrawingCacheEnabled(true);
        qrImage.buildDrawingCache();
        Bitmap bitmap = qrImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        });
    }
    public void uploadData() {

        String kdPjm = kdPeminjamanEdt.getText().toString();
        String kdbuku = kdBukuEdt.getText().toString();
        String nim = nimEdt.getText().toString();
        String tglpjm = tglPjmEdt.getText().toString();
        String tglkbl = tglKblEdt.getText().toString();
        String status = "Dipinjam";


        //  sesuai urutan PeminjamanModel
        PeminjamanModel dataClass = new PeminjamanModel(kdPjm, kdbuku, nim, tglpjm, tglkbl, status);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        DatabaseReference DB_ROOT_REF = FirebaseDatabase.getInstance().getReference();
        DatabaseReference kdPinjam = DB_ROOT_REF.child("Android Peminjaman").child(kdPjm);

        kdPinjam.child("kdPeminjaman").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If data exists, toast currently stored value. Otherwise, set value to "Present"
                if (dataSnapshot.exists()) {
                    Toast.makeText(UploadPeminjamanActivity.this, "Data sudah terdaftar '" + dataSnapshot.getValue(String.class) + "'", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference("Android Peminjaman").child(kdPjm)
                            .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UploadPeminjamanActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadPeminjamanActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: Handle this
            }
        });

    }
}
