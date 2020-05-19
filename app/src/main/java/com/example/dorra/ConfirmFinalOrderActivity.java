package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText cityEditText,streetEditText,buildNumberEditText,homeNumberEditText,nameEditText,phoneNumberEditeText;
    private Button confirmOrderBtn;
    private String totalAmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        FindViewId();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("أتمام الطلب");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);

        nameEditText.setText(Prevalent.currentOnlineUser.getName());
        phoneNumberEditeText.setText(Prevalent.currentOnlineUser.getPhone());
        cityEditText.setText(Prevalent.currentOnlineUser.getCity());
        streetEditText.setText(Prevalent.currentOnlineUser.getStreet());
        buildNumberEditText.setText(Prevalent.currentOnlineUser.getBuild());
        homeNumberEditText.setText(Prevalent.currentOnlineUser.getHome());
        totalAmount=getIntent().getStringExtra("Total Price");
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال الاسم",Toast.LENGTH_SHORT).show();
                }
                else if(phoneNumberEditeText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم الهاتف",Toast.LENGTH_SHORT).show();
                }
                else if (cityEditText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال المحافظة",Toast.LENGTH_SHORT).show();
                }else if(streetEditText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال أسم الشارع و المنطقة",Toast.LENGTH_SHORT).show();
                }else if(buildNumberEditText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم البناية",Toast.LENGTH_SHORT).show();
                }else if(homeNumberEditText.getText().toString().isEmpty()){
                    Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم المنزل",Toast.LENGTH_SHORT).show();
                }else{
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    Check();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmFinalOrderActivity.this);
                    builder.setMessage("هل أنت متأكد من اتمام العملية ؟").setPositiveButton("نعم", dialogClickListener)
                            .setNegativeButton("لا", dialogClickListener).show();
                }


            }
        });

    }
    private void Check() {
        if (nameEditText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال الاسم",Toast.LENGTH_SHORT).show();
        }
        else if(phoneNumberEditeText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم الهاتف",Toast.LENGTH_SHORT).show();
        }
        else if (cityEditText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال المدينة",Toast.LENGTH_SHORT).show();
        }else if(streetEditText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال أسم الشارع و المنطقة",Toast.LENGTH_SHORT).show();
        }else if(buildNumberEditText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم البناية",Toast.LENGTH_SHORT).show();
        }else if(homeNumberEditText.getText().toString().isEmpty()){
            Toast.makeText(ConfirmFinalOrderActivity.this,"الرجاء أدخال رقم المنزل",Toast.LENGTH_SHORT).show();
        }else{
            ConfirmOrder();
        }
    }
    private void ConfirmOrder() {
       final String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference ordarRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap= new HashMap<>();
        //add the data to database
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("buildnumber",buildNumberEditText.getText().toString());
        ordersMap.put("homenumber",homeNumberEditText.getText().toString());
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneNumberEditeText.getText().toString());
        ordersMap.put("streetPlace",streetEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not shipped");

        ordarRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //to empty an cart activity if the user confirm an buy the products
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            // to notify the user in changes activity
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ConfirmFinalOrderActivity.this,"تمت عملية الشراء بنجاح الرجاء الانتظار لجين وصول طلبك",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeeActivity.class);
                            //to block the user to back to cart activity
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }
    private void FindViewId(){
        nameEditText=findViewById(R.id.sheppmint_full_name);
        phoneNumberEditeText=findViewById(R.id.sheppmint_phone_number);
        confirmOrderBtn=findViewById(R.id.confirm_product_to_cart_btn);
        cityEditText=findViewById(R.id.sheppmint_location_city);
        streetEditText=findViewById(R.id.sheppmint_location_street);
        buildNumberEditText=findViewById(R.id.sheppmint_build_number);
        homeNumberEditText=findViewById(R.id.sheppmint_home_number);
    }
}
