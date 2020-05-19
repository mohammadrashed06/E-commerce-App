package com.example.dorra.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dorra.HomeeActivity;
import com.example.dorra.MainActivity;
import com.example.dorra.R;
import com.example.dorra.ShowAllProductsActivity;

public class FirstPageToAdminActivity extends AppCompatActivity {
    private Button logoutAdminBtn,addNewProductBtn,CheckNewOrdersBtn,maintainProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page_to_admin);
        logoutAdminBtn = findViewById(R.id.log_out_admin);
        addNewProductBtn= findViewById(R.id.add_new_item_admin);
        CheckNewOrdersBtn = findViewById(R.id.check_new_orders_admin);
        maintainProductBtn=findViewById(R.id.edit_user_product_data_admin);
        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageToAdminActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });
        logoutAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageToAdminActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        CheckNewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageToAdminActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
        maintainProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstPageToAdminActivity.this, ShowAllProductsActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });
    }
}
