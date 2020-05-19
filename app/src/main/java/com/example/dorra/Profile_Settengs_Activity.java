package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dorra.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Settengs_Activity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneNumberEditText,passwordEditText,locationCityEditText,locationStreetEditText,buildNumberEditeText,homeNumberEditeText;
    private TextView closeTextBtn,saveTextBtn,userName;
    private Uri imageUri;
    private String myUrl="";
    private StorageReference storeProfilePictureRef;
    private String checker="";
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        FindViewId();

        userInfoDisplay(profileImageView,fullNameEditText,userPhoneNumberEditText,passwordEditText,locationCityEditText,locationStreetEditText,buildNumberEditeText,homeNumberEditeText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked")){
                    userInfoSaved();
                }else{
                    //if the user not need to upload hear image just information
                   updateOnlyUserInfo();
                }
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checker hear its mean go to userIngoSaved method (user need to edit profile pic)
                checker="clicked";
                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(Profile_Settengs_Activity.this);
            }
        });

    }

    private void updateOnlyUserInfo() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullNameEditText.getText().toString());
        userMap.put("phone",userPhoneNumberEditText.getText().toString());
        userMap.put("password",passwordEditText.getText().toString());
        userMap.put("city",locationCityEditText.getText().toString());
        userMap.put("street",locationStreetEditText.getText().toString());
        userMap.put("buildnumber",buildNumberEditeText.getText().toString());
        userMap.put("homenumber",homeNumberEditeText.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
        startActivity(new Intent(Profile_Settengs_Activity.this,MainActivity.class));
        Toast.makeText(Profile_Settengs_Activity.this,"تم تحديث بياناتك بنجاح",Toast.LENGTH_SHORT).show();
        finish();

    }

    //for image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
        }else{
            //refresh the settings activity
            startActivity(new Intent(Profile_Settengs_Activity.this,Profile_Settengs_Activity.class));
            finish();
        }
    }

    private void userInfoSaved() {

            if (checker.equals("clicked")) {
                //that means image has been sellected
                uploadImage();

        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("يتم تحديث الملف الشخصي...");
        progressDialog.setMessage("الرجاء الانتظار, يتم تحديث معلوماتك الشخصية...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (imageUri!=null){
            final StorageReference fileRef=storeProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getPhone()+".jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                         HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",fullNameEditText.getText().toString());
                        userMap.put("phone",userPhoneNumberEditText.getText().toString());
                        userMap.put("password",passwordEditText.getText().toString());
                        userMap.put("city",locationCityEditText.getText().toString());
                        userMap.put("street",locationStreetEditText.getText().toString());
                        userMap.put("buildnumber",buildNumberEditeText.getText().toString());
                        userMap.put("homenumber",homeNumberEditeText.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(Profile_Settengs_Activity.this,MainActivity.class));
                        Toast.makeText(Profile_Settengs_Activity.this,"تم تحديث بياناتك بنجاح",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Profile_Settengs_Activity.this,"حدث خطأ...",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(Profile_Settengs_Activity.this,"لم يتم اختيار صورة...",Toast.LENGTH_SHORT).show();

        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneNumberEditText, final EditText passwordEditText, final EditText locationCityEditText, final EditText locationStreetEditText, final EditText buildNumberEditeText, final EditText homeNumberEditeText) {
        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check if the full number exist or user exist
                if(dataSnapshot.exists()){
                    //check if the image is exist
                    if(dataSnapshot.child("image").exists()) {
                            //show the information in settings activity
                            String image = dataSnapshot.child("image").getValue().toString();
                            String name = dataSnapshot.child("name").getValue().toString();
                            String pass = dataSnapshot.child("password").getValue().toString();
                            String phone = dataSnapshot.child("phone").getValue().toString();
                            String city = dataSnapshot.child("city").getValue().toString();
                            String street = dataSnapshot.child("street").getValue().toString();
                            String buildnumber = dataSnapshot.child("buildnumber").getValue().toString();
                            String homenumber = dataSnapshot.child("homenumber").getValue().toString();
                            //display it
                            Picasso.get().load(image).into(profileImageView);
                            fullNameEditText.setText(name);
                            userPhoneNumberEditText.setText(phone);
                            passwordEditText.setText(pass);
                            locationCityEditText.setText(city);
                            locationStreetEditText.setText(street);
                            buildNumberEditeText.setText(buildnumber);
                            homeNumberEditeText.setText(homenumber);



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void FindViewId(){
        storeProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileImageView=(CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText=findViewById(R.id.settings_full_name);
        userPhoneNumberEditText=findViewById(R.id.settings_phone_number);
        passwordEditText=findViewById(R.id.settings_password);
        locationCityEditText=findViewById(R.id.settings_location_city);
        locationStreetEditText=findViewById(R.id.settings_location_street);
        buildNumberEditeText=findViewById(R.id.settings_build_number);
        homeNumberEditeText=findViewById(R.id.settings_home_number);
        closeTextBtn=findViewById(R.id.close_settings_btn);
        saveTextBtn=findViewById(R.id.update_settings_btn);
        userName=findViewById(R.id.settings_user_name);
        userName.setText(Prevalent.currentOnlineUser.getName());
        fullNameEditText.setText(Prevalent.currentOnlineUser.getName());
        userPhoneNumberEditText.setText(Prevalent.currentOnlineUser.getPhone());
        passwordEditText.setText(Prevalent.currentOnlineUser.getPassword());

    }
}

