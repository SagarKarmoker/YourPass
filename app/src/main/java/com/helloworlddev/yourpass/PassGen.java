package com.helloworlddev.yourpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.provider.Settings;

public class PassGen extends AppCompatActivity {
    // Firebase section
    private FirebaseDatabase db;
    private DatabaseReference ref;

    int length = 8;
    StringBuilder password = new StringBuilder();

    ArrayList <String> passHistory = new ArrayList<>();

    String user = "";

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

                password = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    password.append(all.charAt((int) (Math.random() * all.length())));
                }
                passField.setText(password.toString());
                passHistory.add(password.toString());
            }
        });

        //copy to clipboard
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Copied Text", password);
                clip.setPrimaryClip(clipData);

                Toast.makeText(PassGen.this, "Password copied to clipboard", Toast.LENGTH_LONG).show();

                //firebase
                PasswordModel pass;
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = dateFormat.format(date);

                // setting imei number as user identity
                user = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                String passwordString = password.toString();
                pass = new PasswordModel(user, passwordString, strDate);

                db = FirebaseDatabase.getInstance();
                ref = db.getReference("Users");
                String userId = ref.push().getKey(); // Generate a unique user ID
                assert userId != null;

//                ref.child(userId).setValue(pass);
                ref.child(userId).setValue(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PassGen.this, "Password saved to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PassGen.this, "Failed to save password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        showQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passField.getText().toString().equals("")){
                    Toast.makeText(PassGen.this, "Please generate password", Toast.LENGTH_SHORT).show();
                }
                else{
                    showQRcode(PassGen.this, passField.getText().toString());
                }
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