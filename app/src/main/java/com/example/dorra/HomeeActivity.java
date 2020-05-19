package com.example.dorra;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.CollapsibleActionView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.dorra.Admin.AdminMaintainProductsActivity;
import com.example.dorra.Category.CategotyHomeAdapter;
import com.example.dorra.Category.HomeCategoryFragment;
import com.example.dorra.Model.Category;
import com.example.dorra.Model.Products;
import com.example.dorra.Prevalent.Prevalent;
import com.example.dorra.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String type="";
    private ViewFlipper viewFlipper;
    private ImageView facebookImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Category> categoryList = new ArrayList<>();
    RecyclerView categoryRecyclerView;
    CategotyHomeAdapter categotyHomeAdapter;


    Category category1 = new Category();
    Category category2 = new Category();
    Category category3 = new Category();
    Category category4 = new Category();
    Category category5 = new Category();
    Category category6 = new Category();
    Category category7 = new Category();
    Category category8 = new Category();
    Category category9 = new Category();
    Category category10 = new Category();
    Category category11 = new Category();
    Category category12 = new Category();
    Category category13 = new Category();
    Category category14 = new Category();
    Category category15 = new Category();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homee);

        Paper.init(this);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=findViewById(R.id.recycler_menu);
        viewFlipper = findViewById(R.id.v_flipper);
        facebookImage = findViewById(R.id.img_face_book);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager =new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!= null){
            //to diferans btween users/admins
            type=getIntent().getExtras().get("Admin").toString();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("الرئيسية");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("Admin")){
                    Intent intent =new Intent(HomeeActivity.this,CartActivity.class);
                    startActivity(intent);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "https://web.facebook.com/?_rdc=1&_rdr");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://web.facebook.com/?_rdc=1&_rdr");
                sharingIntent.setPackage("com.facebook.katana");
                startActivity(sharingIntent);

            }
        });
        //to display user name and photo
        View headerVeiew= navigationView.getHeaderView(0);
        TextView userNameTextView=headerVeiew.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView=headerVeiew.findViewById(R.id.user_profile_image);

        if (!type.equals("Admin")){
           userNameTextView.setText(Prevalent.currentOnlineUser.getName());
           Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }
        int imageSliderPhoto[] = {R.drawable.afia,R.drawable.drrrrr,R.drawable.ppsi};
        for(int image:imageSliderPhoto){
            fliperImages(image);
       }

        setCategorysData();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);
        categoryList.add(category4);
        categoryList.add(category5);
        categoryList.add(category6);
        categoryList.add(category7);
        categoryList.add(category8);
        categoryList.add(category9);
        categoryList.add(category10);
        categoryList.add(category11);
        categoryList.add(category12);
        categoryList.add(category13);
        categoryList.add(category14);
        categoryList.add(category15);

        // Inflate the layout
        categoryRecyclerView= findViewById(R.id.category_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        categotyHomeAdapter =new CategotyHomeAdapter(categoryList,this);
        categoryRecyclerView.setAdapter(categotyHomeAdapter);
        categotyHomeAdapter.notifyDataSetChanged();


    }


    private void setCategorysData(){
        category1.setCategoryName("الرئيسية");
        category1.setCategoryImage(R.drawable.ic_home_black_24dp);
        category4.setCategoryName("المعلبات");
        category4.setCategoryImage(R.drawable.preserved);
        category5.setCategoryName("الصلصات");
        category5.setCategoryImage(R.drawable.shoppingstore);
        category6.setCategoryName("البهارات");
        category6.setCategoryImage(R.drawable.foodyy);
        category7.setCategoryName("المفرزات");
        category7.setCategoryImage(R.drawable.foodtt);
        category8.setCategoryName("اللحوم و الدواجن");
        category8.setCategoryImage(R.drawable.foodoo);
        category9.setCategoryName("المعجنات و الخبز");
        category9.setCategoryImage(R.drawable.foodee);
        category10.setCategoryName("المكسرات و المحمص");
        category10.setCategoryImage(R.drawable.fruit);
        category11.setCategoryName("العصائر و المشروبات");
        category11.setCategoryImage(R.drawable.foodkl);
        category12.setCategoryName("الخضراوات");
        category12.setCategoryImage(R.drawable.apple);
        category13.setCategoryName("الاجبان و الالبان");
        category13.setCategoryImage(R.drawable.egg);
        category14.setCategoryName("المعكرونا و النوديلز");
        category14.setCategoryImage(R.drawable.food);
        category15.setCategoryName("الشيبس");
        category15.setCategoryImage(R.drawable.foodfffff);
        category2.setCategoryName("المربيات");
        category2.setCategoryImage(R.drawable.foodddandrestaurant);
        category3.setCategoryName("المخللات و الزيتون");
        category3.setCategoryImage(R.drawable.foodgg);


    }
    public  void fliperImages(int images){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(images);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }

    private void firebaseSearch( String searchText){
        Query firebaseSearchQuery = productRef.orderByChild("pname").startAt(searchText).endAt(searchText+"\uf8ff");
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(firebaseSearchQuery,Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products,ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        //display the data in card
                        productViewHolder.txtProductName.setText(products.getPname());
                        productViewHolder.txtProductDescription.setText(products.getDescription());
                        productViewHolder.txtProductPrice.setText("السعر : "+products.getPrice()+" د.أ");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        // to set up on user click in product open details activity
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")){
                                    // i have to send the admin to another activity
                                    Intent intent=new Intent(HomeeActivity.this, AdminMaintainProductsActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);

                                }else{
                                    Intent intent=new Intent(HomeeActivity.this, ProductDetailActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    @Override
    protected void onStart() {
        super.onStart();
        Query firebaseSearchQuery = productRef.orderByChild("newornot").equalTo("Yes");

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(firebaseSearchQuery,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        //display the data in card
                        productViewHolder.txtProductName.setText(products.getPname());
                        productViewHolder.txtProductDescription.setText(products.getDescription());
                        productViewHolder.txtProductPrice.setText("السعر : "+products.getPrice()+" د.أ");
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);
                        // to set up on user click in product open details activity
                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")){
                                    // i have to send the admin to another activity
                                    Intent intent=new Intent(HomeeActivity.this, AdminMaintainProductsActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);

                                }else{
                                    Intent intent=new Intent(HomeeActivity.this, ProductDetailActivity.class);
                                    //sent the data to productActivity
                                    intent.putExtra("pid",products.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        ProductViewHolder holder=new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_option_menu,menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id==R.id.action_search){

                    Intent a = new Intent(HomeeActivity.this, SearchProductActivity.class);
                    startActivity(a);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            if (!type.equals("Admin")){
                Intent intent=new Intent(HomeeActivity.this,Profile_Settengs_Activity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_cart) {
            if (!type.equals("Admin")){
                Intent intent =new Intent(HomeeActivity.this,CartActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_search) {
            Intent intent =new Intent(HomeeActivity.this,SearchProductActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_showAllProduct) {
            Intent intent =new Intent(HomeeActivity.this,ShowAllProductsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {


        } else if (id == R.id.nav_contact_us) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:0786634648"));
                            startActivity(intent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeeActivity.this);
            builder.setMessage("هل أنت متأكد انك تريد الاتصال ؟").setPositiveButton("نعم", dialogClickListener)
                    .setNegativeButton("لا", dialogClickListener).show();

        }else if (id == R.id.nav_logout) {
            if (!type.equals("Admin")){
                Paper.book().destroy();
                Intent intent=new Intent(HomeeActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(HomeeActivity.this,"تم تسجيل الخروج بنجاح.",Toast.LENGTH_LONG).show();
                finish();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
