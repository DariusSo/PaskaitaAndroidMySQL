package com.example.paskaitaandroidmysql;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registrutis = findViewById(R.id.Registruotis);
        registrutis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registracija();
            }
        });
        Button prisijungti = findViewById(R.id.Prisijungti);
        prisijungti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prisijungimas();
            }
        });

    }
    public void registracija(){
        Intent intent = new Intent(this, RegistracijaActivity.class);
        startActivity(intent);
    }
    public void prisijungimas(){
        Intent intent = new Intent(this, PrisijungimasActivity.class);
        startActivity(intent);
    }

}