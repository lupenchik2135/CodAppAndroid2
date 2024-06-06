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
import com.example.codappandroid2.DbObjects.Individuals;
import com.example.codappandroid2.DbObjects.LegalEntities;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    private DbConnection dbConnection;
    private Clients clientData;
    Individuals individualData;
    LegalEntities legalEntityData;
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
        clientData = (Clients) getIntent().getSerializableExtra("clientData");
        individualData = (Individuals) getIntent().getSerializableExtra("individualData");
        legalEntityData = (LegalEntities) getIntent().getSerializableExtra("legalEntityData");

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
                if (clientData != null)
                {
                    showAccountInfoWindow(clientData, individualData, legalEntityData);
                }
                else Toast.makeText(AccountActivity.this, "Client пустой!", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void showAccountInfoWindow(Clients clientData, Individuals individualData, LegalEntities legalEntityData) {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("Ваша информация");
        LayoutInflater inflater = LayoutInflater.from(this);
        View accountInfoWindow = inflater.inflate(R.layout.account_info_window, null);
        dialogue.setView(accountInfoWindow);

        Spinner spinner = accountInfoWindow.findViewById(R.id.spinner);
        ArrayList<String> data = new ArrayList<>();
        data.add(clientData.getLogin());
        data.add(clientData.getPassword());
        data.add(String.valueOf(clientData.getBonuses()));
        data.add(String.valueOf(clientData.getTotalSpent()));
        if (individualData != null) {
            data.add(individualData.getFirstName());
            data.add(individualData.getSecondName());
            data.add(individualData.getThirdName());
            data.add(individualData.getEmail());
//            data.add(individualData.getPhoneNumber());
//            data.add(String.valueOf(individualData.getBirthDay()));
        }
        if (legalEntityData != null) {
            data.add(legalEntityData.getCompanyName());
            data.add(legalEntityData.getContactName());
            data.add(legalEntityData.getInn());
//            data.add(legalEntityData.getOgrn());
//            data.add(legalEntityData.getLicense());
//            data.add(legalEntityData.getLegalAddress());
//            data.add(legalEntityData.getRealAddress());
            data.add(legalEntityData.getContactEmail());
            data.add(legalEntityData.getContactNumber());
        }
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