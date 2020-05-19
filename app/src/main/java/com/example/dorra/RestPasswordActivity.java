package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dorra.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RestPasswordActivity extends AppCompatActivity {
    private String check="";
    private TextView pageTitle;
    private EditText phoneNumber;
    private Button virefy_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);

        //to get the states from user
        check = getIntent().getStringExtra("check");
        FindViewId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        // if user come from the setting activity
        if (check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            virefy_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VirefyUserLogin();
                }
            });

        }
    }


    private void FindViewId(){
        pageTitle= findViewById(R.id.title_qustions);
        phoneNumber=findViewById(R.id.find_phone_number);
        virefy_btn = findViewById(R.id.virefy_btn);
    }
    private void VirefyUserLogin(){
        final String phone = phoneNumber.getText().toString();
        if (!phone.equals("")){
            final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);

            Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String mPhone = dataSnapshot.child("phone").getValue().toString();
                        //check if user have an account
                                //allow the user to chang the password
                                AlertDialog.Builder builder = new AlertDialog.Builder(RestPasswordActivity.this);
                                builder.setTitle("كلمة المرور الجديدة");
                                final EditText newPassword = new EditText(RestPasswordActivity.this);
                                newPassword.setHint("اكتب كلمة المرور الجديدة هنا...");
                                builder.setView(newPassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newPassword.getText().toString().equals("")){
                                            Ref.child("password")
                                                    .setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(RestPasswordActivity.this,"تم تعيير كلمة المرور بنجاح",Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(RestPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();


                    }else{
                            Toast.makeText(RestPasswordActivity.this,"رقم الهاتف غير موجود",Toast.LENGTH_LONG).show();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(RestPasswordActivity.this,"لا يمكن ترك الأسئلة فارغة",Toast.LENGTH_LONG).show();

        }


    }
}
