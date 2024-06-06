package com.example.codappandroid2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codappandroid2.DbObjects.Clients;
import com.example.codappandroid2.DbObjects.Individuals;
import com.example.codappandroid2.DbObjects.LegalEntities;
import com.google.android.material.snackbar.Snackbar;

public class ChooseClientTypeActivity extends AppCompatActivity {
    private DbConnection dbConnection;
    private Clients clientData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_client_type);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConnection = (DbConnection) getIntent().getSerializableExtra("dbConnection");

        clientData = (Clients) getIntent().getSerializableExtra("clientData");
        showAccountTypeRegister();
    }
    private void showAccountTypeRegister() {
        AlertDialog.Builder dialogueType = new AlertDialog.Builder(this);
        dialogueType.setTitle("Зарегистрироваться");
        dialogueType.setMessage("Выберите какое вы лицо");

        LayoutInflater inflater = LayoutInflater.from(this);
        View chooseTypeWindow = inflater.inflate(R.layout.choose_client_type_window, null);
        dialogueType.setView(chooseTypeWindow);
        Button individualButton = chooseTypeWindow.findViewById(R.id.individualbutton);
        Button legalEntityButton = chooseTypeWindow.findViewById(R.id.legalentitybutton);

        Individuals individuals = null;
        LegalEntities legalEntities = null;
        individualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIndividualRegisterWindow();
            }
        });
        legalEntityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLegalEntityRegisterWindow();
            }
        });
        dialogueType.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (individuals != null || legalEntities != null) {
                    showAccountTypeRegister();
                } else {
                    Snackbar.make(chooseTypeWindow, "Ошибка!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        dialogueType.show();
    }

    private void showLegalEntityRegisterWindow() {
    }

    private void showIndividualRegisterWindow() {
    }
}