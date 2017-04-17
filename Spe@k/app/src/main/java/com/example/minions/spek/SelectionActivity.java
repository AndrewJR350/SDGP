package com.example.minions.spek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {
    private final String TAG = "DeviceListActivity";
    private Button login;
    private Button signup;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        login = (Button) findViewById(R.id.btnLogin);
        signup = (Button) findViewById(R.id.btnSignup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "State Login");
                intent = new Intent(SelectionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "State SignUp");
                intent = new Intent(SelectionActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
