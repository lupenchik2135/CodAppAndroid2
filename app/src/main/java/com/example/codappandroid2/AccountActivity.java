package com.example.codappandroid2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codappandroid2.DbObjects.Clients;
import com.example.codappandroid2.DbObjects.DAO.OrdersDAO;
import com.example.codappandroid2.DbObjects.Individuals;
import com.example.codappandroid2.DbObjects.LegalEntities;
import com.example.codappandroid2.DbObjects.Orders;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private DbConnection dbConnection;
    private Clients clientData;
    private Individuals individualData;
    private LegalEntities legalEntityData;
    private OrdersDAO ordersDAO;
    private Button buttonExit, showClientInfoButton, showOrderInfoButton, createOrderButton;

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

        ordersDAO = new OrdersDAO(dbConnection.getConnection());

        buttonExit = findViewById(R.id.exitbuttonAccountActivity);
        showClientInfoButton = findViewById(R.id.showClientInfoButtonAccountActivity);
        showOrderInfoButton = findViewById(R.id.showOrderInfoButtonAccountActivity);
        createOrderButton = findViewById(R.id.createOrderButtonAccountActivity);

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
        showClientInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Данные для отображения в Spinner
                if (clientData != null) {
                    showAccountInfoWindow(clientData, individualData, legalEntityData);
                } else
                    Toast.makeText(AccountActivity.this, "Client пустой!", Toast.LENGTH_SHORT).show();

            }
        });
        showOrderInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrdersWindow(clientData);
            }
        });
        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateOrderWindow(clientData);
            }
        });
    }

    private void showAccountInfoWindow(Clients clientData, Individuals individualData, LegalEntities legalEntityData) {
        AlertDialog.Builder dialogue = new AlertDialog.Builder(this);
        dialogue.setTitle("Ваша информация");
        LayoutInflater inflater = LayoutInflater.from(this);
        View accountInfoWindow = inflater.inflate(R.layout.account_info_window, null);
        dialogue.setView(accountInfoWindow);

        Spinner spinner = accountInfoWindow.findViewById(R.id.spinnerAccount);
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
        if (spinner != null) {
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

    private void showOrdersWindow(Clients clientData) {
        AlertDialog.Builder dialogueOrderInfo = new AlertDialog.Builder(this);
        dialogueOrderInfo.setTitle("Ваши заказы");
        LayoutInflater inflater = LayoutInflater.from(this);
        View orderInfoWindow = inflater.inflate(R.layout.order_info_window, null);
        dialogueOrderInfo.setView(orderInfoWindow);

        Spinner spinner = orderInfoWindow.findViewById(R.id.spinnerOrder);
        ArrayList<String> data = new ArrayList<>();
        List<Orders> orders = ordersDAO.getOrdersListByClientId(clientData.getId());
        if (orders != null) {
            for (Orders order : orders) {
                // Добавление информации о заказе в массив data
                String orderInfo = "Дата заказа: " + order.getOrderDate() + " - потрачено: " + order.getTotalAmount();
                data.add(orderInfo);
            }
        }

        // Адаптер для заполнения данных в Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AccountActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Привязка адаптера к Spinner
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }


        dialogueOrderInfo.setNegativeButton("Вернуться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        dialogueOrderInfo.show();
    }

    private void showCreateOrderWindow(Clients clientData) {
        AlertDialog.Builder dialogueCreateOrder = new AlertDialog.Builder(this);
        dialogueCreateOrder.setTitle("Введите дату заказа");
        LayoutInflater inflater = LayoutInflater.from(this);
        View createOrderWindow = inflater.inflate(R.layout.order_create_window, null);
        dialogueCreateOrder.setView(createOrderWindow);

        final DatePicker orderData = createOrderWindow.findViewById(R.id.orderDateField);
        final NumberPicker datacenterIdPicker = createOrderWindow.findViewById(R.id.orderDatacenterIdField);
        final int[] datacenterId = new int[1];
        datacenterIdPicker.setMinValue(1);
        datacenterIdPicker.setMaxValue(5);
        datacenterIdPicker.setWrapSelectorWheel(false);

        datacenterIdPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                datacenterId[0] = newVal;
            }
        });
        dialogueCreateOrder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialogueCreateOrder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (orderData == null) {
                    Snackbar.make(createOrderWindow, "Выберите дату", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                DatePicker orderData = createOrderWindow.findViewById(R.id.orderDateField);
                int year = orderData.getYear();
                int month = orderData.getMonth();
                int day = orderData.getDayOfMonth();

// Создать объект класса Calendar для удобства работы с датой
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

// Получить объект класса Date из объекта Calendar
                java.util.Date selectedDate = calendar.getTime();
                java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
                boolean success = ordersDAO.registerOrder(clientData.getId(), sqlDate, datacenterId[0]);
                if (!success) {
                    Snackbar.make(createOrderWindow, "Ошибка!", Snackbar.LENGTH_SHORT).show();
                    return;
                } else {
                    Snackbar.make(createOrderWindow, "Добро пожаловать!", Snackbar.LENGTH_SHORT).show();
                    // Создаем Intent
                    Intent intent = new Intent(AccountActivity.this, CreateOrderActivity.class);

                    // Передаем dbConnection и clientsDAO
                    intent.putExtra("dbConnection", dbConnection);
                    intent.putExtra("clientData", clientData);
                    startActivity(intent);
                    finish();
                }
            }
        });
        dialogueCreateOrder.show();
    }


}