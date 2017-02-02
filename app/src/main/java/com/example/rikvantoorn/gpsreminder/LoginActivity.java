/**
 * LoginActivity
 * Rik van Toorn, 11279184
 *
 * This activity handles the login functionaliy. This is done bij FireBase.
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // declare all the views
    private Button buttonLogin;
    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private TextView textViewToRegistration;

    // declare the progressdialog
    private ProgressDialog progressDialog;

    // declare the firebase authentication
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Gets the current firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // checks if the user is already logged in and if so redirects to the ReminderListActivity
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        // asign all the views
        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewToRegistration = (TextView) findViewById(R.id.textViewToRegistration);

        // set onclick listeners
        buttonLogin.setOnClickListener(this);
        textViewToRegistration.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogin) {
            userLogin();
        }
        if(view == textViewToRegistration) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    private void userLogin(){
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

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

        progressDialog.setMessage("Loging in....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ReminderListActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
