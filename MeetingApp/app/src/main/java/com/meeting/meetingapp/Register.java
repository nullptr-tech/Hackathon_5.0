package com.meeting.meetingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meeting.meetingapp.ChatHelper.ChatHelper;
import com.meeting.meetingapp.adapter.UsersChatAdapter;

import java.util.Date;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText email,password,name, startingBalance;
    Button register;
    TextView loginbck;
    Switch isParentOrChildTest;
    Boolean IsParentOrChild;
    String Name,Email,Password;
    FirebaseAuth mAuth;
    ProgressDialog mDialog;
    DatabaseReference databaseReference;
    double StartingBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.regEmail);
        password = (EditText)findViewById(R.id.regPassword);
        name = (EditText)findViewById(R.id.regname);
        register = (Button)findViewById(R.id.regHere);
        loginbck = (TextView)findViewById(R.id.backToLogin);

        isParentOrChildTest = (Switch)findViewById(R.id.isParentOrChild);

        startingBalance = (EditText) findViewById(R.id.startingbalance);



        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
        loginbck.setOnClickListener(this);
        mDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (v==register){
            Signup();
        }else if (v==loginbck){
            startActivity(new Intent(Register.this,Login.class));
        }
    }

    private void Signup() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        StartingBalance = Double.parseDouble(startingBalance.getText().toString().trim());
        boolean parentOrChildStatus;

        if (isParentOrChildTest.isChecked())
            parentOrChildStatus = true;
        else
            parentOrChildStatus = false;

        IsParentOrChild = parentOrChildStatus;
        Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Name)){
            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(Password)){
            Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6){
            Toast.makeText(Register.this,"Passwor must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth(task.getResult().getUser(), Double.parseDouble(startingBalance.getText().toString()));
                    mAuth.signOut();
                }else{
                    Toast.makeText(Register.this,"error on creating user",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Check your Email for Verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user, double startingBalance) {
        createNewUser(user.getUid(), startingBalance);
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(Register.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createNewUser(String userId, double startingBalance){
        User user = buildNewUser();
        databaseReference.child("users").child(userId).setValue(user);

        String key = databaseReference.child("users").child(userId).child("balance").push().getKey();
        databaseReference.child("users").child(userId).child("balance").child("Monday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Tuesday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Wednesday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Thursday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Friday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Saturday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Sunday").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("CurrentBalance").setValue(startingBalance);


        databaseReference.child("users").child(userId).child("balance").child("Monday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Monday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Tuesday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Tuesday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Wednesday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Wednesday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Thursday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Thursday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Friday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Friday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Saturday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Saturday").child("MoneyIn").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Sunday").child("MoneyOut").setValue("");
        databaseReference.child("users").child(userId).child("balance").child("Sunday").child("MoneyIn").setValue("");

    }

    private User buildNewUser() {
        return new User(
                getDisplayName(),
                getUserEmail(),
                UsersChatAdapter.ONLINE,
                ChatHelper.generateRandomAvatarForUser(),
                new Date().getTime(),
                getParentOrChild(),
                getStartingBalance()

        );
    }
    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

    public boolean getParentOrChild(){
        return IsParentOrChild;
    }

    public Double getStartingBalance(){ return StartingBalance; }

}