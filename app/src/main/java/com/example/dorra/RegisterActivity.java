package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText inputName,inputPhoneNumber,inputPassword ;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadingBar= new ProgressDialog(RegisterActivity.this);
        createAccountButton = findViewById(R.id.register_btn);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        FindViewId();
    }
    private void CreateAccount(){
        String name =inputName.getText().toString();
        String phone =inputPhoneNumber.getText().toString();
        String password =inputPassword.getText().toString();
        if(name.isEmpty() || phone.isEmpty() || password.isEmpty()){
            Toast.makeText(RegisterActivity.this,"الرجاء أدخال جميع المعلومات.",Toast.LENGTH_LONG).show();
        }else {
            loadingBar.setTitle("الرجاء الانتظار");
            loadingBar.setMessage("أنشاء الحساب, يتم أدخال بياناتك...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatePhoneNumber(name,phone,password);
        }
    }
    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        //create database referance
        final DatabaseReference mRef;
        mRef= FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this line check if the user have an phone number(unieq) in the database then cant create new account
                if (!(dataSnapshot.child("Users").child(phone).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    //add data ti firebase
                    mRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //check if prossec is sucsses intent to loginActivity
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "تهانينا, تم انشاء حساب لك.", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "خطأ في الشبكة, الرجاء المحاولة لاحقا...", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }else{
                    //if number phone alredy exist not compleat prosses
                    Toast.makeText(RegisterActivity.this,"هاذا الرقم "+phone+"موجود بالفعل ",Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this,"الرجاء المحاولة مرة اخرى بستخدام رقم اخر...",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void FindViewId(){
        inputName= findViewById(R.id.register_name_input);
        inputPhoneNumber= findViewById(R.id.register_phone_number_input);
        inputPassword= findViewById(R.id.register_password_input);
        createAccountButton= findViewById(R.id.register_btn);

    }
}
