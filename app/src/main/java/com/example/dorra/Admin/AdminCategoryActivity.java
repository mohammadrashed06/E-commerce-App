package com.example.dorra.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dorra.R;

public class AdminCategoryActivity extends AppCompatActivity {
private ImageView vegetarian,meat,pickles,jam,frozen,bread,spices,cannedFood,img_pasta,img_suses,img_moxsrat,img_cheps,img_pipses,img_chese;
private TextView text_Admin_Profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        FindViewId();
        vegetarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","الخضراوات");
                startActivity(intent);
            }
        });
        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","اللحوم و الدواجن");
                startActivity(intent);
            }
        });
        pickles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المخللات و الزيتون");
                startActivity(intent);
            }
        });
        jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المربيات");
                startActivity(intent);
            }
        });
        frozen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المفرزات");
                startActivity(intent);
            }
        });
        img_pasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المعكرونا و النوديلز");
                startActivity(intent);
            }
        });
        spices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","البهارات");
                startActivity(intent);
            }
        });
        cannedFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المعلبات");
                startActivity(intent);
            }
        });
        img_suses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","الصلصات");
                startActivity(intent);
            }
        });img_moxsrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المكسرات و المحمص");
                startActivity(intent);
            }
        });
        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","المعجنات و الخبز");
                startActivity(intent);
            }
        });img_cheps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","الشيبس");
                startActivity(intent);
            }
        });
        img_pipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","العصائر و المشروبات");
                startActivity(intent);
            }
        });
        img_chese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intent.putExtra("category","الاجبان و الالبان");
                startActivity(intent);
            }
        });


    }
    private void FindViewId(){
        vegetarian=findViewById(R.id.img_vegetarian);
        meat=findViewById(R.id.img_meat);
        pickles=findViewById(R.id.img_pickles);
        jam=findViewById(R.id.img_jam);
        frozen=findViewById(R.id.img_frozen);
        bread=findViewById(R.id.img_bread);
        spices=findViewById(R.id.img_spices);
        cannedFood=findViewById(R.id.img_cannedfood);
        text_Admin_Profile=findViewById(R.id.textView);
        img_suses = findViewById(R.id.img_suses);
        img_moxsrat = findViewById(R.id.img_moxsrat);
        img_pasta = findViewById(R.id.img_nodles);
        img_cheps = findViewById(R.id.img_cheps);
        img_pipses = findViewById(R.id.img_pipses);
        img_chese = findViewById(R.id.img_chese);


    }
}
