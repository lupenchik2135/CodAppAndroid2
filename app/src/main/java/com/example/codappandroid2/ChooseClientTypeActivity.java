package com.example.codappandroid2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codappandroid2.DbObjects.Clients;
import com.example.codappandroid2.DbObjects.DAO.ClientsDAO;
import com.example.codappandroid2.DbObjects.DAO.IndividualsDAO;
import com.example.codappandroid2.DbObjects.DAO.LegalEntitiesDAO;
import com.example.codappandroid2.DbObjects.Individuals;
import com.example.codappandroid2.DbObjects.LegalEntities;
import com.google.android.material.snackbar.Snackbar;

public class ChooseClientTypeActivity extends AppCompatActivity {
    private DbConnection dbConnection;
    private Clients clientData;
    Button individualButton, legalEntityButton;
    private IndividualsDAO individualsDAO;
    private LegalEntitiesDAO legalEntitiesDAO;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_client_type);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.choooseType), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConnection = (DbConnection) getIntent().getSerializableExtra("dbConnection");

        clientData = (Clients) getIntent().getSerializableExtra("clientData");
        individualsDAO = new IndividualsDAO(dbConnection.getConnection());
        legalEntitiesDAO = new LegalEntitiesDAO(dbConnection.getConnection());
        individualButton = findViewById(R.id.individualbutton);
        legalEntityButton = findViewById(R.id.legalentitybutton);
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
    }

    private void showLegalEntityRegisterWindow() {
        AlertDialog.Builder dialogueTypeLegalEntity= new AlertDialog.Builder(this);
        dialogueTypeLegalEntity.setTitle("Введите данные");
        LayoutInflater inflater = LayoutInflater.from(this);
        View individualLegalEntityWindow = inflater.inflate(R.layout.legal_entity_client_register, null);
        dialogueTypeLegalEntity.setView(individualLegalEntityWindow);

        final EditText companyName = individualLegalEntityWindow.findViewById(R.id.companyNameField);
        final EditText contactName = individualLegalEntityWindow.findViewById(R.id.contactNameField);
        final EditText inn = individualLegalEntityWindow.findViewById(R.id.innField);
//        final EditText ogrn = individualLegalEntityWindow.findViewById(R.id.ogrnField);
//        final EditText license = individualLegalEntityWindow.findViewById(R.id.licenseField);
//        final EditText legalAddress = individualLegalEntityWindow.findViewById(R.id.legalAddressField);
//        final EditText realAddress = individualLegalEntityWindow.findViewById(R.id.realAddressField);
        final EditText contactEmail = individualLegalEntityWindow.findViewById(R.id.contactEmailField);
        final EditText contactNumber = individualLegalEntityWindow.findViewById(R.id.contactNumberField);
        dialogueTypeLegalEntity.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialogueTypeLegalEntity.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(companyName.getText().toString())) {
                    Snackbar.make(individualLegalEntityWindow, "Введите имя компании", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contactName.getText().toString())) {
                    Snackbar.make(individualLegalEntityWindow, "Введите имя контактного лица", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inn.getText().toString())) {
                    Snackbar.make(individualLegalEntityWindow, "Введите инн", Snackbar.LENGTH_SHORT).show();
                    return;
                }
//                if (TextUtils.isEmpty(ogrn.getText().toString())) {
//                    Snackbar.make(individualLegalEntityWindow, "Введите огрн", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(license.getText().toString())) {
//                    Snackbar.make(individualLegalEntityWindow, "Введите лицензию", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(legalAddress.getText().toString())) {
//                    Snackbar.make(individualLegalEntityWindow, "Введите легальный адресс", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(realAddress.getText().toString())) {
//                    Snackbar.make(individualLegalEntityWindow, "Введите юридический", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(contactEmail.getText().toString())) {
                    Snackbar.make(individualLegalEntityWindow, "Введите почту контактного лица", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (contactNumber.getText().toString().length() != 11) {
                    Snackbar.make(individualLegalEntityWindow, "Введите номер контактного лица", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //регистрация пользователя
                boolean success = legalEntitiesDAO.registerLegalEntity(companyName.getText().toString(), contactName.getText().toString(), inn.getText().toString(), contactEmail.getText().toString(), contactNumber.getText().toString(), clientData.getId());

                if (success) {
                    Snackbar.make(individualLegalEntityWindow, "Добро пожаловать!", Snackbar.LENGTH_SHORT).show();

                    LegalEntities legalEntityData = legalEntitiesDAO.getLegalEntityByClient(clientData.getId());
                    // Создаем Intent
                    Intent intent = new Intent(ChooseClientTypeActivity.this, AccountActivity.class);

                    // Передаем dbConnection и clientsDAO
                    intent.putExtra("dbConnection", dbConnection);
                    intent.putExtra("clientData", clientData);
                    intent.putExtra("legalEntityData", legalEntityData);
                    startActivity(intent);
                } else {
                    Snackbar.make(individualLegalEntityWindow, "Ошибка!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        dialogueTypeLegalEntity.show();
    }

    private void showIndividualRegisterWindow() {
        AlertDialog.Builder dialogueTypeIndividual = new AlertDialog.Builder(this);
        dialogueTypeIndividual.setTitle("Введите данные");
        LayoutInflater inflater = LayoutInflater.from(this);
        View individualRegisterWindow = inflater.inflate(R.layout.individual_client_register, null);
        dialogueTypeIndividual.setView(individualRegisterWindow);

        final EditText firstName = individualRegisterWindow.findViewById(R.id.firstNameField);
        final EditText secondName = individualRegisterWindow.findViewById(R.id.secondNameField);
        final EditText thirdName = individualRegisterWindow.findViewById(R.id.thirdNameField);
        final EditText email = individualRegisterWindow.findViewById(R.id.emailField);
//        final EditText phoneNumber = individualRegisterWindow.findViewById(R.id.phoneNumberField);
//        final EditText birthDay = individualRegisterWindow.findViewById(R.id.birthDayField);


        dialogueTypeIndividual.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialogueTypeIndividual.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(firstName.getText().toString())) {
                    Snackbar.make(individualRegisterWindow, "Введите имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(secondName.getText().toString())) {
                    Snackbar.make(individualRegisterWindow, "Введите фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(thirdName.getText().toString())) {
                    Snackbar.make(individualRegisterWindow, "Введите отчество!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(individualRegisterWindow, "Введите почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
//                if (phoneNumber.getText().toString().length() != 11) {
//                    Snackbar.make(individualRegisterWindow, "Номер телефона без +", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(thirdName.getText().toString())) {
//                    Snackbar.make(individualRegisterWindow, "Введите дату рождения", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
                //регистрация пользователя
                boolean success = individualsDAO.registerIndividual(firstName.getText().toString(), secondName.getText().toString(), thirdName.getText().toString(), email.getText().toString(), clientData.getId());

                if (!success) {
                    Snackbar.make(individualRegisterWindow, "Ошибка!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    Snackbar.make(individualRegisterWindow, "Добро пожаловать!", Snackbar.LENGTH_SHORT).show();

                    Individuals individualData = individualsDAO.getIndividualByClient(clientData.getId());
                    // Создаем Intent
                    Intent intent = new Intent(ChooseClientTypeActivity.this, AccountActivity.class);

                    // Передаем dbConnection и clientsDAO
                    intent.putExtra("dbConnection", dbConnection);
                    intent.putExtra("clientData", clientData);
                    intent.putExtra("individualData", individualData);
                    startActivity(intent);
                    finish();
                }
            }
        });

        dialogueTypeIndividual.show();
    }
}