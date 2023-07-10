package com.helloworlddev.yourpass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appColor)));

        MaterialButton enterBtn = findViewById(R.id.enterBtn);

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PassGen.class);
                startActivity(intent);
            }
        });


    }
}