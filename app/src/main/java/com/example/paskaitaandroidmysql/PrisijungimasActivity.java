package com.example.paskaitaandroidmysql;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PrisijungimasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prisijungimas2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView slapyvardis = findViewById(R.id.SlapyvardisPrisijungti);
        TextView slaptazodis = findViewById(R.id.SlaptazodisPrisijungti);
        Button prisijungimas = findViewById(R.id.Prisijungimas);
        Button atsijungti = findViewById(R.id.atsijungti);
        atsijungti.setEnabled(false);
        atsijungti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lougout();
            }
        });
        prisijungimas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               prisijungimas(slapyvardis.getText().toString(), slaptazodis.getText().toString());

            }
        });
    }
    public void prisijungimas(String slapyvardis, String slaptazodis){
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/klientas?username="+ slapyvardis +"&password="+ slaptazodis)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                            TextView sveikiname = findViewById(R.id.textView1);
                        String s = null;
                        try {
                            s = response.body().string();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        sveikiname.setText("Sveikiname prisijungus, jusu id yra: " + s);
                            TextView slapv = findViewById(R.id.SlapyvardisPrisijungti);
                            TextView slapt = findViewById(R.id.SlaptazodisPrisijungti);
                            slapv.setEnabled(false);
                            slapt.setEnabled(false);
                            slapv.setVisibility(View.INVISIBLE);
                            slapt.setVisibility(View.INVISIBLE);
                            Button atsijungti = findViewById(R.id.atsijungti);
                            atsijungti.setVisibility(View.VISIBLE);
                            atsijungti.setEnabled(true);
                            Button prisijungti = findViewById(R.id.Prisijungimas);
                            prisijungti.setEnabled(false);
                            prisijungti.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
    public void lougout(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}