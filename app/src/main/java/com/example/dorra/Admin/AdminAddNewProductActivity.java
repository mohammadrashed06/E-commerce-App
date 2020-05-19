package com.example.dorra.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dorra.HomeeActivity;
import com.example.dorra.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
      private String categoryName,productName,productDescription,productPrice,saveCurrentDate,saveCurrentTime;
      private Button addNewProductButton,addNewImageButton;
      private EditText inputProductName,inputProductDescription,inputProductPrice;
      private ImageView inputProductImage;
      private static final int gallaryPick=1;
      private Uri imageUri;
      //product random key to differace between products
      private String productRandomKey,downloadImageUrl;
      private StorageReference productImagesRef;
      private DatabaseReference productRef;
      private ProgressDialog loadingBar;
      private String newest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        FindViewId();
        newest = "";
        loadingBar= new ProgressDialog(AdminAddNewProductActivity.this);
        //Storing data will parsing in this var key
        categoryName= getIntent().getExtras().get("category").toString();
        //create a folder in the firebase will contain all product Images
        productImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        //create a folder in the firebase will contain all product Data
        productRef=FirebaseDatabase.getInstance().getReference().child("Products");
        addNewImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to select photo from gallary
                OpenGallary();
            }

            private void OpenGallary() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallaryPick);

            }
        });
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                newest="Yes";
                                ValidateProductData();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                ValidateProductData();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminAddNewProductActivity.this);
                builder.setMessage("هل تريد الاضافة الى المنتجات الجديدة ؟").setPositiveButton("نعم", dialogClickListener)
                        .setNegativeButton("لا", dialogClickListener).show();

            }


            private void ValidateProductData() {
                productName= inputProductName.getText().toString();
                productDescription= inputProductDescription.getText().toString();
                productPrice= inputProductPrice.getText().toString();
                //verify
                if (imageUri==null){
                    Toast.makeText(getApplicationContext(),"الرجاء اختيار صورة",Toast.LENGTH_LONG).show();
                }else if(productName.isEmpty()){
                    Toast.makeText(getApplicationContext(),"الرجاء ادخال اسم المنتج",Toast.LENGTH_LONG).show();
                }else if (productDescription.isEmpty()){
                    Toast.makeText(getApplicationContext(),"الرجاء ادخال معلومات المنتج",Toast.LENGTH_LONG).show();
                }else if (productPrice.isEmpty()){
                    Toast.makeText(getApplicationContext(),"الرجاء ادخال سعر المنتج",Toast.LENGTH_LONG).show();
                }else{
                    //store the data of product to firebase storage i need to current time i add the product to display it to users
                    storeProdutInformation();

                }
            }

            private void storeProdutInformation() {
                loadingBar.setTitle("الرجاء الانتظار");
                loadingBar.setMessage("يتم اضافة المنتج...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd,yyyy");
                saveCurrentDate=currentDate.format(calendar.getTime());
                SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime=currentTime.format(calendar.getTime());
                //store the time in one var(uniqe random key)
                productRandomKey=saveCurrentDate+saveCurrentTime;

                //Store the image url in firebase
                //this is link of product image
                final StorageReference filePath= productImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
                final UploadTask uploadTask=filePath.putFile(imageUri);
                //cheack if the image cant upload to store firebase
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.dismiss();
                        String massage=e.toString();
                        Toast.makeText(getApplicationContext(),"Error: "+massage,Toast.LENGTH_LONG).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"تم رفع الصورة بنجاح",Toast.LENGTH_LONG).show();
                        //get the link of image(uri) and store it in firebase database
                        Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                //hear iam get the image url
                                downloadImageUrl=filePath.getDownloadUrl().toString();
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                downloadImageUrl=task.getResult().toString();
                                Toast.makeText(getApplicationContext(),"تم حفظ الصورة في قاعدة البيانات بنجاح...",Toast.LENGTH_LONG).show();
                                 saveProductInfoToDataBase();
                            }
                        });
                    }
                });

            }
        });
    }


    private void saveProductInfoToDataBase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",productDescription);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",productPrice);
        productMap.put("pname",productName);
        productMap.put("newornot",newest);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(),"تمت اضافة المنتج بنجاح...",Toast.LENGTH_LONG).show();
                        }else{
                            loadingBar.dismiss();
                            String massege=task.getException().toString();
                            Toast.makeText(getApplicationContext(),"Error: "+massege,Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == gallaryPick && resultCode == RESULT_OK && data != null ) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Log.d(TAG, String.valueOf(bitmap));
                inputProductImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void FindViewId(){
        inputProductImage=findViewById(R.id.select_product_image);
        addNewImageButton=findViewById(R.id.add_new_image);
        inputProductName=findViewById(R.id.product_name);
        inputProductDescription=findViewById(R.id.product_description);
        inputProductPrice=findViewById(R.id.product_price);
        addNewProductButton=findViewById(R.id.add_new_product);
    }
}
