package edu.uga.cs.shopsync.ui;

import static edu.uga.cs.shopsync.TemporaryStuff.users;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.uga.cs.shopsync.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Email or password cannot be empty",
                           Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (users.containsKey(email)) {
            Toast.makeText(RegistrationActivity.this, "User already exists", Toast.LENGTH_LONG)
                    .show();
        } else {
            // TODO:
            /*
            MainActivity.users.put(email, new User(email, password));
            Toast.makeText(RegistrationActivity.this, "User registered successfully",
                           Toast.LENGTH_LONG)
                    .show();
             */
        }
    }
}
