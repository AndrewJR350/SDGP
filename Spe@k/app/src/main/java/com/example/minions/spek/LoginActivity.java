package com.example.minions.spek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "DeviceListActivity";
    private Button login;
    private EditText usernameTxt;
    private EditText passwordTxt;

    private String username;
    private String password;
    private String databasePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.btnLogin);
        usernameTxt = (EditText) findViewById(R.id.btnUsername);
        passwordTxt = (EditText) findViewById(R.id.txtPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameTxt.getText().toString();
                password = passwordTxt.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference().child("Users");

                DatabaseReference databaseReference1 = databaseReference.child(username);

                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {
                            };
                            Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                            databasePassword = map.get("Password");
                            if(password.equals(databasePassword)){
                                Intent intend = new Intent(LoginActivity.this,DeviceListActivity.class);
                                startActivity(intend);
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            }else{
                                usernameTxt.setText("");
                                passwordTxt.setText("");
                                Toast.makeText(getApplicationContext(), "Invalid Password, Try again", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Username, Try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
