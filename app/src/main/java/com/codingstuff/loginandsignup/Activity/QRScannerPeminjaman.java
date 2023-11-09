package com.codingstuff.loginandsignup.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.codingstuff.loginandsignup.R;
import com.codingstuff.loginandsignup.databinding.ActivityQrScannerPeminjamanBinding;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.net.ContentHandler;

public class QRScannerPeminjaman extends AppCompatActivity {

    Button scan_btn, pengembalianBtn;
    ActivityQrScannerPeminjamanBinding binding;
    UploadPengembalianActivity pengembalian;
    String res;
    TextView textView;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCamera();
                } else {
                    //   Show why user need this permission
                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else {
            setResult(result.getContents());
            getResult();
        }
    });


    public void getResult() {
        String getKdPjm = textView.getText().toString();
        Intent myIntent = new Intent(QRScannerPeminjaman.this,UploadPengembalianActivity.class);
        myIntent.putExtra("hasilQR",getKdPjm);
        startActivity(myIntent);
    }
    private void setResult(String contents) {
        binding.txtcodeQr.setText(contents);

        //  buat text qr masuk ke pengembalian upload
        //res = String.valueOf(binding.txtcodeQr.getText());
        //pengembalian.kdPeminjamanEdt.setText(res);

        //  little bug when open camera for the second time it just shows black.
        //startActivity(new Intent(QRScannerPeminjaman.this, UploadPengembalianActivity.class));

    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan Kode QR");
        options.setCameraId(1);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }


    private void initViews() {
        binding.scannerQr.setOnClickListener(view -> {
                checkPermissionAndShowActivity(this);
        });
    }

    private void checkPermissionAndShowActivity(Context context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(context, "Ijin Kamera diperlukan", Toast.LENGTH_SHORT).show();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityQrScannerPeminjamanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();

        scan_btn = findViewById(R.id.scannerQr);
        textView = findViewById(R.id.txtcodeQr);
        pengembalianBtn = findViewById(R.id.backToPengembalian);

        pengembalianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QRScannerPeminjaman.this, UploadPengembalianActivity.class));
            }
        });
    }
}
