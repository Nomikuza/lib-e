package com.codingstuff.loginandsignup.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codingstuff.loginandsignup.Domain.PeminjamanModel;
import com.codingstuff.loginandsignup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UploadPengembalianActivity extends AppCompatActivity {

    String[] items = {"Nihil", "Telat Pengembalian", "Buku Hilang/Rusak"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    private EditText kdBukuEdt, nimEdt, tglKblEdt, tglPjmEdt;
    private TextView hitungBtn, dendaTxt;
    public EditText kdPeminjamanEdt;
    private Button btnBuku, btnQr;
    private ImageView qrImage;
    public DataSnapshot dataSnapshot;
    private String imageUrl;
    private Uri uri;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembalian);
        kdPeminjamanEdt = findViewById(R.id.kdPeminjamanPjms);
        kdBukuEdt = findViewById(R.id.kdBukuPjm);
        nimEdt = findViewById(R.id.nimPjm);
        tglPjmEdt = findViewById(R.id.tglPeminjamanPjm);
        tglKblEdt = findViewById(R.id.tglPengembalianPjm);
        qrImage = findViewById(R.id.qrPjm);
        btnBuku = findViewById(R.id.addpeminjamanPjm);
        btnQr = findViewById(R.id.addpeminjamanPjm2);
        hitungBtn = findViewById(R.id.hitungBtn);
        dendaTxt = findViewById(R.id.dendaTxt);
        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item_combobox, items);
        autoCompleteTxt.setAdapter(adapterItems);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String kdBuku = kdBukuEdt.getText().toString();
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                autoSetKdBuku();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query kdBooks = databaseReference.child("Android Tutorials").orderByChild("kdBuku").equalTo(kdBuku);
                //System.out.println(kdBooks);

                //String selection = (String) parent.getItemAtPosition(position);
                    if (item.equals ("Buku Hilang/Rusak")) {
                        kdBooks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        PeminjamanModel projectModel = postSnapshot.getValue(PeminjamanModel.class);
                                        //projectModel.setKey(dataSnapshot.getKey());
                                        String hargaBuku = postSnapshot.child("harga").getValue().toString();

                                        dendaTxt.setText(hargaBuku);
                                    }
                                } else {
                                    Toast.makeText(UploadPengembalianActivity.this, "Error Denda Harga Buku", Toast.LENGTH_SHORT).show();
                                }

                                // If data exists, toast currently stored value. Otherwise, set value to "Present"

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }

                        });
                    } else {
                        Toast.makeText(UploadPengembalianActivity.this, "Error Equals", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        kdPeminjamanEdt.setText(getIntent().getStringExtra("hasilQR"));
        kdPeminjamanEdt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    retrieveData();
                    return true;
                }
                return false;
            }
        });

        hitungBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDifference();
            }
        });
        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadPengembalianActivity.this, QRScannerPeminjaman.class));
            }
        });
        btnBuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
                //saveData();
                startActivity(new Intent(UploadPengembalianActivity.this, ShowPeminjamanActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        autoUpdateTanggal();
    }

    private void autoSetKdBuku() {
        String status = autoCompleteTxt.getText().toString();
        String kdBuku = kdBukuEdt.getText().toString();
        try {
            if (status.equals ("Buku Hilang/Rusak")) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query kdBooks = databaseReference.child("Android Tutorial").orderByChild("kdBuku").equalTo(kdBuku);

                kdBooks.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                PeminjamanModel projectModel = postSnapshot.getValue(PeminjamanModel.class);
                                //projectModel.setKey(dataSnapshot.getKey());
                                String hargaBuku = postSnapshot.child("harga").getValue().toString();

                                dendaTxt.setText(hargaBuku);
                            }
                        }

                        // If data exists, toast currently stored value. Otherwise, set value to "Present"

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        } catch (Exception e) {
            Toast.makeText(UploadPengembalianActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void autoUpdateTanggal() {
        tglPjmEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        tglKblEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private long getCurrentDateInMiliseconds() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void retrieveData() {
        String kdPjm = kdPeminjamanEdt.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query kdPinjam = databaseReference.child("Android Peminjaman").orderByChild("kdPeminjaman").equalTo(kdPjm);

        kdPinjam.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If data exists, toast currently stored value. Otherwise, set value to "Present"
                if (dataSnapshot.exists()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        PeminjamanModel projectModel = postSnapshot.getValue(PeminjamanModel.class);
                        //projectModel.setKey(dataSnapshot.getKey());
                        String kdPjm = postSnapshot.child("kdPeminjaman").getValue().toString();
                        String kdBuku = postSnapshot.child("kdBuku").getValue().toString();
                        String tglPjm = postSnapshot.child("tglPeminjaman").getValue().toString();
                        String tglKbl = postSnapshot.child("tglPengembalian").getValue().toString();
                        String nim = postSnapshot.child("nim").getValue().toString();

                        kdPeminjamanEdt.setText(kdPjm);
                        kdBukuEdt.setText(kdBuku);
                        tglPjmEdt.setText(tglPjm);
                        tglKblEdt.setText(tglKbl);
                        nimEdt.setText(nim);
                        autoCompleteTxt.setText("Nihil", false);
                    }
                } else {
                    Toast.makeText(UploadPengembalianActivity.this, "Kode peminjaman tidak terdaftar '" + dataSnapshot.getValue(String.class) + "'", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: Handle this
            }
        });

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


    public void printDifference() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date c = Calendar.getInstance().getTime();
            String tglNow = sdf.format(c);
            Date tglHariIni = sdf.parse(tglNow);
            String dateStr1 = tglKblEdt.getText().toString();
            Date tglKblEdts = sdf.parse(dateStr1);
            //int days = Days.daysBetween(tglKblEdts, tglPjmEdts).getDays();
            long timeDiff = Math.abs(tglHariIni.getTime() - tglKblEdts.getTime());
            long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
            long denda = daysDiff * 200;
            String dendaString = String.valueOf(denda);
            dendaTxt.setText(dendaString);

        } catch (Exception e) {
            Toast.makeText(UploadPengembalianActivity.this, "Error diff", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] longIntoString(long milliseconds) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        return new String[]{dateFormat.format(milliseconds), timeFormat.format(milliseconds)};
    }

    public void uploadData() {

        String kdPjm = kdPeminjamanEdt.getText().toString();
        String kdbuku = kdBukuEdt.getText().toString();
        String nim = nimEdt.getText().toString();
        String tglpjm = tglPjmEdt.getText().toString();
        String tglkbl = tglKblEdt.getText().toString();
        String status = autoCompleteTxt.getText().toString();


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
                    FirebaseDatabase.getInstance().getReference("Android Peminjaman").child(kdPjm)
                            .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UploadPengembalianActivity.this, "Tersimpan", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadPengembalianActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(UploadPengembalianActivity.this, "Kode peminjaman tidak terdaftar '" + dataSnapshot.getValue(String.class) + "'", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //TODO: Handle this
            }
        });

    }
}
