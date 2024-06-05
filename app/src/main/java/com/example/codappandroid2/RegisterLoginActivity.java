package com.example.codappandroid2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codappandroid2.DbObjects.Client;
import com.example.codappandroid2.DbObjects.DAO.ClientsDAO;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;

public class RegisterLoginActivity extends AppCompatActivity {

    private DbConnection dbConnection;
    private Button buttonLogIn, buttonRegister, buttonExit;

    ConstraintLayout rgWindow;
    private ClientsDAO clientsDAO;
    private Client loggedClient;
    private TextView clientText;
    private ListView clientTextList;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Получаем dbConnection и clientsDAO из Intent
//        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.regLog), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbConnection = (DbConnection) getIntent().getSerializableExtra("dbConnection");

        clientsDAO = new ClientsDAO(dbConnection.getConnection());
        buttonLogIn = findViewById(R.id.loginbutton);
        buttonRegister = findViewById(R.id.registerbutton);
        buttonExit = findViewById(R.id.exitbutton);
        rgWindow = findViewById(R.id.regLog);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbConnection.getConnection().close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("Зарегистрироваться");
        dialogue.setMessage("Введите данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window, null);
        dialogue.setView(registerWindow);
        final EditText login = registerWindow.findViewById(R.id.loginField);
        final EditText password = registerWindow.findViewById(R.id.passwordField);

        dialogue.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialogue.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (TextUtils.isEmpty(login.getText().toString())) {
                    Snackbar.make(rgWindow, "Введите логин", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 6) {
                    Snackbar.make(rgWindow, "Введите пароль, который более 6 символов!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                //регистрация пользователя
                boolean success = clientsDAO.registerClient(login.getText().toString(), password.getText().toString());

                if (!success) {
                    Snackbar.make(rgWindow, "Ошибка!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    Snackbar.make(rgWindow, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                }


            }
        });

        dialogue.show();
    }
    public void setDbConnection(DbConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }
}