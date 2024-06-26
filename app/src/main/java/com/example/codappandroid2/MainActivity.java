package com.example.codappandroid2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private Button buttonEnter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);


        buttonEnter = findViewById(R.id.enterbutton);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbConnection dbConnection = new DbConnection();
                if (dbConnection.getConnection() != null) {
                    Toast.makeText(MainActivity.this, "Подключено", Toast.LENGTH_SHORT).show();

                    // Создаем Intent
                    Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);

                    // Передаем dbConnection и clientsDAO
                    intent.putExtra("dbConnection", dbConnection);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }
}