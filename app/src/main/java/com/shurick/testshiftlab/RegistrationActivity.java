package com.shurick.testshiftlab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements TextWatcher, DatePickerDialog.OnDateSetListener {

    EditText firstName;
    EditText lastName;
    EditText date;
    EditText password;
    EditText r_password;
    Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        date = (EditText) findViewById(R.id.date);
        password = (EditText) findViewById(R.id.password);
        r_password = (EditText) findViewById(R.id.r_password);
        registration = (Button) findViewById(R.id.sign_up);

        registration.setEnabled(false);

        firstName.addTextChangedListener(this);
        lastName.addTextChangedListener(this);
        date.addTextChangedListener(this);
        password.addTextChangedListener(this);
        r_password.addTextChangedListener(this);
        registration.addTextChangedListener(this);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<Boolean, String> p = validation();
                if (p.first) {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    intent.putExtra("Name", firstName.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(RegistrationActivity.this,
                            p.second,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog();
                }
                return false;
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog pickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        pickerDialog.show();
    }

    private boolean ageLimit() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        String today = c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(date.getText().toString());
            date2 = sdf.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        long diff = (date2.getTime() - date1.getTime()) / 1000 / 3600 / 24 / 365; // Возможны неточности
        return diff >= 18;
    }

    private Pair validation() {
        if (!firstName.getText().toString().matches("[A-Z][a-z]*"))
            return new Pair<>(false, "The name must start with a uppercase letter and contain no digits!");
        if (!lastName.getText().toString().matches("[A-Z][a-z]*"))
            return new Pair<>(false, "The surname must start with a uppercase letter and contain no digits!");
        if (lastName.getText().toString().length() <= 2)
            return new Pair<>(false, "The last name must contain at least two characters!");
        if (!date.getText().toString().matches("[0-9][0-9]\\.[0-9][0-9]\\.[0-9][0-9][0-9][0-9]"))
            return new Pair<>(false, "Date of birth entered incorrectly!");
        if (!ageLimit())
            return new Pair<>(false, "You are under the age of majority!");
        if (!password.getText().toString().matches("[A-Z[0-9]]*"))
            return new Pair<>(false, "The password must contain only numbers and uppercase letters!");
        if (!password.getText().toString().equals(r_password.getText().toString()))
            return new Pair<>(false, "The passwords you entered don't match!");
        return new Pair<>(true, "");
    }

    private boolean chanceRegistration() {
        return !firstName.getText().toString().equals("") &&
                !lastName.getText().toString().equals("") &&
                !date.getText().toString().equals("") &&
                !password.getText().toString().equals("") &&
                !r_password.getText().toString().equals("");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (chanceRegistration())
            registration.setEnabled(true);
        else
            registration.setEnabled(false);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        date.setText((i2 / 10 ) + "" + (i2 % 10) + "." + ((i1 + 1) / 10) + "" + ((i1 + 1) % 10) + "." + i);
        date.setSelection(date.getText().toString().length());
    }
}