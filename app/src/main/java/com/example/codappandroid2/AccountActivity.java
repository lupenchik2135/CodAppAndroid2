package com.example.codappandroid2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codappandroid2.DbObjects.Clients;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    private DbConnection dbConnection;
    private Clients loggedClients;
    private Button buttonExit, showDataButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConnection = (DbConnection) getIntent().getSerializableExtra("dbConnection");
        loggedClients = (Clients) getIntent().getSerializableExtra("Client");
        buttonExit = findViewById(R.id.exitbuttonAccountActivity);

        showDataButton = findViewById(R.id.showDataButton);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbConnection.getConnection().close();
                    finish(); // закрыть текущую активность
                    System.exit(0); // завершить работу приложения
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        showDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Данные для отображения в Spinner
                if (loggedClients != null)
                {
                    showAccountInfoWindow(loggedClients);
                }
                else Toast.makeText(AccountActivity.this, "Client пустой!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void showAccountInfoWindow(Clients loggedClients) {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("Ваша информация");
        LayoutInflater inflater = LayoutInflater.from(this);
        View accountInfoWindow = inflater.inflate(R.layout.account_info_window, null);
        dialogue.setView(accountInfoWindow);

        Spinner spinner = accountInfoWindow.findViewById(R.id.spinner);
        ArrayList<String> data = new ArrayList<>();
        data.add(loggedClients.getLogin());
        data.add(loggedClients.getPassword());
        data.add(String.valueOf(loggedClients.getBonuses()));
        data.add(String.valueOf(loggedClients.getTotalSpent()));

        // Адаптер для заполнения данных в Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Привязка адаптера к Spinner
        if (spinner != null){
            spinner.setAdapter(adapter);
        }


        dialogue.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialogue.show();
    }
}