package com.example.minions.spek;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private final String TAG = "DeviceListActivity";
    private Button signupButton;
    private EditText usernameTxt;
    private EditText nameTxt;
    private EditText passwordTxt;
    private EditText telPhnNoTxt;
    private EditText addressTxt;

    private String username;
    private String name;
    private String password;
    private String address;
    private int telNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addressTxt = (EditText) findViewById(R.id.txtAddress);
        signupButton = (Button) findViewById(R.id.btnSigup);
        usernameTxt = (EditText) findViewById(R.id.txtUserName);
        nameTxt = (EditText) findViewById(R.id.txtName);
        passwordTxt = (EditText) findViewById(R.id.txtPassword);
        telPhnNoTxt = (EditText) findViewById(R.id.txtPhnNo);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTxt.getText().toString().trim();
                name = nameTxt.getText().toString().trim();
                password = passwordTxt.getText().toString().trim();
                address = addressTxt.getText().toString().trim();
                telNumber = Integer.valueOf(telPhnNoTxt.getText().toString().trim());

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                boolean hasUppercase = !password.equals(password.toLowerCase());
                if (username.equals("") || name.equals("") || password.equals("") || address.equals("") || telPhnNoTxt.getText().equals("")) {
                    Toast.makeText(context, "Form is not Valid", duration).show();
                } else {
                    if (username.length() <= 20) {
                        if (!hasUppercase) {
                            Toast.makeText(context, "Must have an uppercase Character", duration).show();
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = database.getReference().child("Users");
                            Log.i(TAG, "Test 1");
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i(TAG, "Test 2");
                                    //Get map of users in data-snap-shot
//                                            String value = dataSnapshot.getValue(String.class);
//                                            Log.i(TAG, "Value is: " + value);
                                    GenericTypeIndicator<Map<String, Map<String, String>>> genIndicator = new GenericTypeIndicator<Map<String, Map<String, String>>>() {
                                    };
                                    Map<String, Map<String, String>> usersMap = dataSnapshot.getValue(genIndicator);
                                    boolean result = false;
                                    for (Map.Entry<String, Map<String, String>> entry : usersMap.entrySet()) {
                                        Log.i(TAG, entry.getKey());

//                                                Map<String,String> userDetails = entry.getValue();
//                                                for(Map.Entry<String,String> subEntry : userDetails.entrySet()){
//                                                    Log.i(TAG,subEntry.getKey()+"" + subEntry.getValue());
//                                                }

                                        String tempUsername = entry.getKey();
                                        if (tempUsername.equals(username)) {
                                            result = true;
                                            break;
                                        }
                                    }
                                    if (result == false) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference().child("Users");
                                        myRef.child(username).child("Name").setValue(name);
                                        myRef.child(username).child("Username").setValue(username);
                                        myRef.child(username).child("Password").setValue(password);
                                        myRef.child(username).child("Address").setValue(address);
                                        myRef.child(username).child("TelNumber").setValue(Integer.toString(telNumber));

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "User Successfully Created, Thanks You ", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sorry, Username Already Exist", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    //handle databaseError
                                    Log.i(TAG, "Failed to read value.");
                                    Toast.makeText(getApplicationContext(), "Form is not Valid", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(context, "Username not contain more than 20 Characters", duration).show();
                    }
                }


            }
        });

    }
}
