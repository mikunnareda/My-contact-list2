package com.example.mycontactlist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import android.text.format.DateFormat;
import java.util.Calendar;
import android.widget.ScrollView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener{

    private EditText editName, editAddress, editHome, editCell, editEmail;
    private TextView tvBirthday;
    private Button btnChange, btnSave;
    private ToggleButton toggleButtonEdit;
    private Contact currentContact;
    final int PERMISSION_REQUEST_PHONE = 102;
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
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
        initTextChangedEvents();
        initCallFunction();
        initImageButton();

     /*   currentContact = new Contact();

        Intent intent = getIntent();    //retrieve contactID from intent
        int contactID = intent.getIntExtra("contactID",-1); //extract contact ID
        if (contactID !=-1){initContact(contactID);} //if valid contactID passed then load*/
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            initContact(extras.getInt("contactID"));
        } else {currentContact = new Contact();}
    }
    //3 buttons
    private void initImageButtons() {
        ImageButton ibEdit = findViewById(R.id.imageButtonList);
        ImageButton ibMap = findViewById(R.id.imageButtonMap);
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
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
            if (currentContact.getContactID() == -1) {
                Toast.makeText(getBaseContext(), "Contact must be saved before it can be mapped", Toast.LENGTH_LONG).show();
            } else {
                intent.putExtra("contactid", currentContact.getContactID());
            }
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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                boolean wasSuccessful;
                ContactDataSource ds = new ContactDataSource(MainActivity.this);
                try {
                    ds.open();
                    //insert contact
                    if (currentContact.getContactID() == -1) {
                        wasSuccessful = ds.insertContact(currentContact);
                        //duplicate
                        if (wasSuccessful){
                            int newId = ds.getLastContactID();

                            currentContact.setContactID(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateContact(currentContact);
                    }
                    ds.close();
                } catch (Exception e) {
                    wasSuccessful = false;
                    e.printStackTrace();
                }
                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }
                /*-> {
            Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);
            startActivity(intent);
        }); }*/
    private void initTextChangedEvents() {
        final EditText etContactName = findViewById(R.id.editName);
        etContactName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(etContactName.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Auto-generated method stub
            }
        });

        final EditText etStreetAddress = findViewById(R.id.editAddress);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(etStreetAddress.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Auto-generated method stub
            }
        });
        final EditText editCity = findViewById(R.id.editCity);
        editCity.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setCity(editCity.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText editState = findViewById(R.id.editState);
        editState.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setState(editState.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText editZipcode = findViewById(R.id.editZipcode);
        editZipcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(editZipcode.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText editEmail = findViewById(R.id.editEMail);
        editEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setEMail(editEmail.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        final EditText editHome = findViewById(R.id.editHome);
        editHome.addTextChangedListener(new PhoneNumberFormattingTextWatcher(){
            public void afterTextChanged (Editable s){
                currentContact.setPhoneNumber(editHome.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        final EditText editCell = findViewById(R.id.editCell);
        editCell.addTextChangedListener(new PhoneNumberFormattingTextWatcher(){
            public void afterTextChanged (Editable s){
                currentContact.setCellNumber(editCell.getText().toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EditText editName = findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);

        EditText editAddress = findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);

        EditText editCity = findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);

        EditText editState = findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(), 0);

        EditText editZipcode = findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editZipcode.getWindowToken(), 0);

        EditText editPhone = findViewById(R.id.editHome);
        imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);

        EditText editCell = findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);

        EditText editEmail = findViewById(R.id.editEMail);
        imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
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
        //editPhone.setEnabled(enabled);
        //editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        if (enabled){
            editPhone.setInputType(InputType.TYPE_CLASS_PHONE);
            editCell.setInputType(InputType.TYPE_CLASS_PHONE);
        } else {
            editPhone.setInputType(InputType.TYPE_NULL);
            editCell.setInputType(InputType.TYPE_NULL);
        }
        ImageButton picture = findViewById(R.id.imageContact);
        picture.setEnabled(enabled);

    }
    @Override
    public void didFinishDatePickerDialog(Calendar selectedDate) {
        TextView birthday = findViewById(R.id.textBirthday);
       // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        birthday.setText(DateFormat.format("MM/dd/yyyy",selectedDate));

        //store selected date in current contact
        currentContact.setBirthday(selectedDate);
    }
    private void initContact(int id) {
        ContactDataSource ds = new ContactDataSource(MainActivity.this);
        try {
            ds.open();
            currentContact = ds.getSpecificContact(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }

        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView birthDay = findViewById(R.id.textBirthday);


        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZipCode.setText(currentContact.getZipCode());

        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.getEMail());
        birthDay.setText(DateFormat.format("MM/dd/yyyy", currentContact.getBirthday().getTimeInMillis()).toString());

        ImageButton picture = (ImageButton) findViewById (R.id.imageContact);
        if (currentContact.getPicture() != null) {
            picture.setImageBitmap(currentContact.getPicture());
        } else {
            picture.setImageResource(R.drawable.ic_launcher_foreground); // Default placeholder image
        }
    }
    private void initCallFunction() {
        EditText editPhone = findViewById(R.id.editHome);
        editPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                checkPhonePermission(currentContact.getPhoneNumber());
                return false;
            }
        });

        EditText editCell = findViewById(R.id.editCell);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                openSMSApp(currentContact.getCellNumber());
                return true;
            }
        });
    }
    private void initImageButton() {
        ImageButton ib = findViewById(R.id.imageContact);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.activity_main),
                                            "The app needs permission to take pictures.",
                                            Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                    new String[]{android.Manifest.permission.CAMERA},
                                                    PERMISSION_REQUEST_CAMERA);
                                        }
                                    }).show();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    PERMISSION_REQUEST_CAMERA);
                        }
                    } else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
            }
        });
    }
    private void checkPhonePermission(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        android.Manifest.permission.CALL_PHONE)) {

                    Snackbar.make(findViewById(R.id.activity_main),
                            "MyContactList requires this permission to place a call from the app.",
                            Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.CALL_PHONE},
                                    PERMISSION_REQUEST_PHONE);
                        }
                    }).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_PHONE);
                }
            } else {
                callContact(phoneNumber);
            }
        } else {
            callContact(phoneNumber);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "You may now call from this app.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "You will not be able to make calls from this app", Toast.LENGTH_LONG).show();
                }
                break; //added
            }
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivity.this,
                            "You will not be able to save contact pictures from this app", Toast.LENGTH_LONG).show();
                }
                break; //use break instead of return
            }
        }
    }
    public void takePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Add
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageContact = findViewById(R.id.imageContact);
                imageContact.setImageBitmap(scaledPhoto);
                currentContact.setPicture(scaledPhoto);
            }
        }
    }

    private void callContact(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            startActivity(intent);
        }
    }
    private void openSMSApp(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(this, "No phone number available for SMS", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Opening SMS app...", Toast.LENGTH_SHORT).show(); // Debugging log

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + phoneNumber)); // "smsto:" ensures only SMS apps handle this
        smsIntent.putExtra("sms_body", "Hello!");

        try {
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No messaging app found", Toast.LENGTH_SHORT).show();
        }
    }

}
