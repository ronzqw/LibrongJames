package com.rondolphcardozo.librongjamesrjv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rondolphcardozo.librongjamesrjv.ui.User;

public class SignUp extends AppCompatActivity {

    EditText usernameSP;
    EditText emailSP;
    EditText passwordSP;
    EditText confirmSP;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameSP = (EditText)findViewById(R.id.usernameSP);
        emailSP = (EditText)findViewById(R.id.emailSP);
        passwordSP = (EditText) findViewById(R.id.passwordSP);
        confirmSP = (EditText)findViewById(R.id.confirmSP);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
    }


    public void signInButtonClicked(View v) {

        String txtUsername =usernameSP.getText().toString().trim();
        String txtEmail = emailSP.getText().toString().trim();
        String txtPassword = passwordSP.getText().toString().trim();
        String txtConfirm = confirmSP.getText().toString().trim();

        if (txtUsername.isEmpty()) {
            usernameSP.setError("Please Enter Username");
            usernameSP.requestFocus();
        }

        if (txtEmail.isEmpty()) {
            emailSP.setError("Please Enter Email");
            emailSP.requestFocus();
        }

        if (txtPassword.isEmpty()|| txtPassword.length() < 6 )  {
            passwordSP.setError("Please Enter Password containing at least six characters");
            passwordSP.requestFocus();
        }

        if (txtConfirm.isEmpty()|| txtPassword.length() < 6) {
            passwordSP.setError("Please Re-enter Password containing at least six characters");
            passwordSP.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(txtUsername,txtEmail,txtPassword,txtConfirm);

                            FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUp.this, "User Register Successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                            else {
                                                Toast.makeText(SignUp.this, "User Failed to Register", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(SignUp.this, "User Failed to Register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }
}