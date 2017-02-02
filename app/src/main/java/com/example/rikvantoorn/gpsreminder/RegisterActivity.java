/**
 * RegisterActivity
 * Rik van Toorn, 11279184
 *
 * This activity handles the register functionaliy. This is done bij FireBase.
 */

package com.example.rikvantoorn.gpsreminder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    // declare all the views
    private Button buttonRegistration;
    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private EditText editTextPasswordRegistrationConfirmation;
    private TextView textViewToLogin;

    // declare the progressdialog
    private ProgressDialog progressDialog;

    // declare the firebase authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Gets the current firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // checks if the user is already logged in and if so redirects to the ReminderListActivity
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        // asign all the views
        editTextEmailRegistration = (EditText) findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = (EditText) findViewById(R.id.editTextPasswordRegistration);
        editTextPasswordRegistrationConfirmation = (EditText) findViewById(R.id.editTextPasswordRegistrationConfirmation);
        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        textViewToLogin = (TextView) findViewById(R.id.textViewToLogin);

        // set onclick listeners
        buttonRegistration.setOnClickListener(this);
        textViewToLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegistration) {
            userRegistration();
        }
        if(view == textViewToLogin) {
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }

    private void userRegistration() {
        String email = editTextEmailRegistration.getText().toString().trim();
        String password = editTextPasswordRegistration.getText().toString().trim();
        String passwordConfirmation = editTextPasswordRegistrationConfirmation.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passwordConfirmation)) {
            //password is empty
            Toast.makeText(this, "Please enter your password again", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordConfirmation)) {
            Toast.makeText(this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
