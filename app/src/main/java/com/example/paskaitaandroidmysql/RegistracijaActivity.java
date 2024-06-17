package com.example.paskaitaandroidmysql;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistracijaActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registruotis);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.registruotis);

        TextView slapyvardis = findViewById(R.id.Slapyvardis);
        TextView slaptazodis = findViewById(R.id.Slaptazodis);
        Button registruotis1 = findViewById(R.id.Registruotis1);
        Button atgal = findViewById(R.id.Atgal);
        atgal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                griztiMain();
            }
        });
        registruotis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Klientas klientas = new Klientas(slapyvardis.getText().toString(), slaptazodis.getText().toString());
                Gson gson = new Gson();
                String json = gson.toJson(klientas);

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        uregistruoti(json);
                    }
                });

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        siustiEmail(slapyvardis.getText().toString());
                    }
                });
                griztiMain();
            }
        });
    }
    public void griztiMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void siustiEmail(String vartotojoVardas){

        final String username = "h76806";
        final String password = "xbjj gfuf ebsi afqe";
        String message = "Uzregistruotas naujas vartotojas. Slapyvardis: " + vartotojoVardas;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username, password);
            }
        });
        try{
            Message message1 = new MimeMessage((session));
            message1.setFrom(new InternetAddress(username));
            message1.setRecipients(Message.RecipientType.TO, InternetAddress.parse("darius.songaila1@gmail.com"));
            message1.setSubject("Vartotojo registracija.");
            message1.setText(message);
            Transport.send(message1);

        }catch (MessagingException e){
            throw new RuntimeException();
        }
    }
    public void uregistruoti(String json){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/klientas")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        runAPI(request, client);
    }
    public void runAPI(Request request, OkHttpClient client){
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Handle the response if needed, e.g., show a Toast
                        // Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Handle the error, e.g., show a Toast
                        // Toast.makeText(MainActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Handle the exception, e.g., show a Toast
                    // Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}