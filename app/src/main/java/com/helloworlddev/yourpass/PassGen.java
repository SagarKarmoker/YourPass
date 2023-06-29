package com.helloworlddev.yourpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.Arrays;

public class PassGen extends AppCompatActivity {
    int length = 8;
    final StringBuilder[] password = new StringBuilder[1];

    ArrayList <String> passHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_gen);

        MaterialButton genBtn = findViewById(R.id.genBtn);
        MaterialButton copyBtn = findViewById(R.id.copyBtn);
        TextInputEditText passField = findViewById(R.id.passField);
        MaterialToolbar toolbar = findViewById(R.id.toolBar);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        MaterialRadioButton rBtn1 = findViewById(R.id.rBtn1);
        MaterialRadioButton rBtn2 = findViewById(R.id.rBtn2);
        MaterialRadioButton rBtn3 = findViewById(R.id.rBtn3);
        MaterialRadioButton rBtn4 = findViewById(R.id.rBtn4);
        ImageView showQR = findViewById(R.id.showQR);
        ImageButton historyBtn = findViewById(R.id.historyBtn);

        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIconTint(Color.BLACK);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rBtn1){
                    length = 8;
                }
                else if (i == R.id.rBtn2){
                    length = 16;
                }
                else if (i == R.id.rBtn3){
                    length = 32;
                }
                else if (i == R.id.rBtn4){
                    length = 48;
                }

            }
        });

        //generate password
        genBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uppercase;
                String lowercase;
                String special;
                String numbers;
                String all;

                uppercase= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                lowercase= "abcdefghijklmnopqrstuvwxyz";
                special= "!@#$%^&*()_+~`|}{[]:;?><,./-=";
                numbers= "0123456789";

                all = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_+~`|}{[]:;?><,./-=0123456789";

                password[0] = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    password[0].append(all.charAt((int) (Math.random() * all.length())));
                }
                passField.setText(password[0].toString());
                passHistory.add(password[0].toString());
            }
        });

        //copy to clipboard
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Text", Arrays.toString(password));
                clip.setPrimaryClip(clipData);

                Toast.makeText(PassGen.this, "Password copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });


        showQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQRcode(PassGen.this, passField.getText().toString());
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PassGen.this, "Coming soon, Password history", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void showQRcode(Context context, String text){
        Bitmap qrCodeBitmap;
        try {
            qrCodeBitmap = QRCodeGenerator.generateQRCode(text, 500);
        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.qr_code, null);
        ImageView imageView = view.findViewById(R.id.QRcode);
        imageView.setImageBitmap(qrCodeBitmap);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}