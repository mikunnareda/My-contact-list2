package com.example.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import android.text.format.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener{

    private EditText etContact, etAddress, etHomePhone, etCellPhone, etEmail, etBirthday;
    private TextView tvBirthday;
    private Button btnChange, btnSave;
    private ToggleButton toggleButtonEdit;
    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.radioGroupSortBy), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initListButton();
        initMapButton();
        initSettingsButton();
        initImageButtons();
        initToggleButton();
        initChangeDateButton();
        initSaveButton();

    }
    //3 buttons
    private void initImageButtons() {
        ImageButton ibEdit = findViewById(R.id.listNavbar);
        ImageButton ibMap = findViewById(R.id.mapNavbar);
        ImageButton ibSettings = findViewById(R.id.settingsNavbar);
        //ibEdit.setOnClickListener(view -> { });Add function for Edit button
        //ibMap.setOnClickListener(view -> { });Add function for Map button
        //ibSettings.setOnClickListener(view -> { });Add function for Settings button
    }
    private void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(view -> {
            Intent intent = new Intent (MainActivity.this, ContactListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });
    }
    private void initMapButton() {
        ImageButton ibMap = findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactMapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(view -> setForEditing(editToggle.isChecked()));
    }
    //Change Birthday Button
    private void initChangeDateButton(){
        Button changeDate = findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }
    private void initSaveButton() {
        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);
            startActivity(intent);
        });
    }
    private void setForEditing(boolean enabled){
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipcode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        Button buttonChange = findViewById(R.id.btnBirthday);
        Button buttonSave = findViewById(R.id.buttonSave);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipcode.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        if (enabled){
            editName.requestFocus();
        }
    }
    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        TextView birthday = findViewById(R.id.textBirthday);
       // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        birthday.setText(DateFormat.format("MM/dd/yyyy",selectedDate));
    }

}
