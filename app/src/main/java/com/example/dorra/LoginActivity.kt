package com.example.dorra

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dorra.Admin.FirstPageToAdminActivity
import com.example.dorra.Model.Users
import com.example.dorra.Prevalent.Prevalent
import com.google.firebase.database.*
import io.paperdb.Paper

class LoginActivity : AppCompatActivity() {
    var loginButton: Button? =null
    var inputPhoneNumberLogin: EditText? =null
    var inputPasswordLogin: EditText? =null
    var checkBoxRememberMe: com.rey.material.widget.CheckBox ?=null
    var forgetPassord: TextView? =null
    var adminLink:TextView?=null
    var notAdminLink:TextView?=null
    private var loadingBar: ProgressDialog? =null
    private var parentDbName:String="Users"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
         FindViewId()
         loadingBar = ProgressDialog(this)
         Paper.init(this)
         loginButton?.setOnClickListener {
            LoginUser()
        }
        adminLink?.setOnClickListener {
            loginButton?.setText("دخول كمشرف")
            adminLink?.visibility = View.GONE
            notAdminLink?.visibility = View.VISIBLE
            parentDbName="Admins"

        }
        notAdminLink?.setOnClickListener {
            loginButton?.setText("Login")
            adminLink?.visibility=View.VISIBLE
            notAdminLink?.visibility=View.GONE
            parentDbName="Users"

        }
        forgetPassord?.setOnClickListener {
            val intent = Intent(applicationContext, RestPasswordActivity::class.java)
            intent.putExtra("check", "login")
            startActivity(intent)
        }

    }
    private fun LoginUser() {
        val phone: String = inputPhoneNumberLogin?.text.toString()
        val password: String = inputPasswordLogin?.text.toString()
        if (phone.isEmpty() && password.isEmpty()) {
            Toast.makeText(applicationContext, "الرجاء ادخال جميع البيانات.", Toast.LENGTH_LONG).show()
        } else{
            loadingBar?.setTitle("الرجاء الانتظار")
            loadingBar?.setMessage("تسجيل الدخول, يتم التحقق من هويتك...")
            loadingBar?.setCanceledOnTouchOutside(false)
            loadingBar?.show()
            AllowAccsesToAccount(phone,password)
        }
    }
    private fun AllowAccsesToAccount(phone: String, password: String) {
        if (checkBoxRememberMe!!.isChecked){
            //if checked add values to paper
            Paper.book().write(Prevalent.userPhoneKey,phone)
            Paper.book().write(Prevalent.userPasswordKey,password)
        }
        //create database referance
        val mRef: DatabaseReference
        mRef = FirebaseDatabase.getInstance().reference
        //use to add a ValueEventListener to a DatabaseReference.
        mRef.addListenerForSingleValueEvent(object : ValueEventListener {
            //The onDataChange() method is called every time data is changed at the specified database reference, including changes to children.
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //this line check if the user have an phone number(unieq) in the database then cant create new account
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    // Get userdata object and use the values to update the UI
                    var usersData: Users? = dataSnapshot.child(parentDbName).child(phone).getValue(Users::class.java)
                    //hear iam check if phone number exsist and correct in database then check the password or not
                    if(usersData?.phone.equals(phone)){
                        if(usersData?.password.equals(password)) {
                            //check if parent id is admin or user
                            if (parentDbName.equals("Users")) {
                                Toast.makeText(applicationContext, "تم تسجيل الدخول بنجاح.", Toast.LENGTH_LONG).show()
                                loadingBar?.dismiss()
                                val intent = Intent(applicationContext, HomeeActivity::class.java)
                                Prevalent.currentOnlineUser=usersData
                                startActivity(intent)
                            } else if (parentDbName.equals("Admins")) {
                                Toast.makeText(applicationContext, "أهلا بك, تم تسجيل دخولك كمشرف بنجاح.", Toast.LENGTH_LONG).show()
                                loadingBar?.dismiss()
                                val intent = Intent(applicationContext, FirstPageToAdminActivity::class.java)
                                startActivity(intent)
                            }
                        }else {
                            loadingBar?.dismiss()
                            Toast.makeText(applicationContext, "كلمة المرور خطأ", Toast.LENGTH_LONG).show()
                        }
                    }
                }else {
                    //if number phone not alredy exist not compleat prosses
                    Toast.makeText(applicationContext, "الحساب مع هاذا الرقم  $phone غير موجود ", Toast.LENGTH_LONG).show()
                    loadingBar?.dismiss()
                    Toast.makeText(applicationContext, "يجب ان تنشئ حساب جديد.", Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
    private fun FindViewId(){
        loginButton= findViewById(R.id.login_btn)
        inputPhoneNumberLogin =findViewById(R.id.login_phone_number_input)
        inputPasswordLogin =findViewById(R.id.login_password_input)
        checkBoxRememberMe=findViewById(R.id.remember_me_chkb)
        forgetPassord=findViewById(R.id.forget_password_link)
        adminLink=findViewById(R.id.admin_panel_link)
        notAdminLink=findViewById(R.id.not_admin_panel_link)
    }
}
