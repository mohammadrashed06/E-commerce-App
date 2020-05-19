package com.example.dorra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dorra.Model.Cart;
import com.example.dorra.Model.Products;
import com.example.dorra.Prevalent.Prevalent;
import com.example.dorra.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProssecBtn,addProductBtn,browsproductsBtn;
    private TextView textTotalAmount,textMassege,allTotalPriceTv,dFeeTv,sTotalPriceTv,odrerdTo,orderdToSmall;
    private double allTotalPrice= 0.00;
    private ImageView imgMsg1;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    private View view;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FindViewId();

        orderdToSmall.setText(Prevalent.currentOnlineUser.getName());

        nextProssecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",Double.valueOf(allTotalPrice));
                startActivity(intent);
                finish();
            }
        });

       // addProductBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View v) {
          //      Intent intent = new Intent(CartActivity.this,HomeeActivity.class);
           //     startActivity(intent);
            //}
        //});
      //  browsproductsBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
          //  public void onClick(View v) {

            //}
     //   });

      //  PushDownAnim.setPushDownAnimTo( browsproductsBtn)
        //        .setInterpolatorPush( PushDownAnim.DEFAULT_INTERPOLATOR )
         //       .setInterpolatorRelease( PushDownAnim.DEFAULT_INTERPOLATOR )
          //      .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
           //     .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
        //.setOnClickListener( new View.OnClickListener(){
         //   @Override
          //  public void onClick( View view ){
          //      Intent intent = new Intent(CartActivity.this,HomeeActivity.class);
           //     startActivity(intent);
            //}

        //} );

    }


    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        final FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                        //get the data frome data base
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                        .build();
              adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                        //total price for one item
                        double oneTeypProductTPrice=((Double.parseDouble(cart.getPrice()))) * (Double.parseDouble(cart.getQuantity()));
                        Double truncatedDouble = BigDecimal.valueOf(oneTeypProductTPrice)
                                .setScale(3, RoundingMode.HALF_UP)
                                .doubleValue();
                        cartViewHolder.txtProductdesc.setText(cart.getDescription());
                        cartViewHolder.txtProductQuntityNumber.setText(cart.getQuantity());
                        cartViewHolder.txtProductPrice.setText( truncatedDouble + " د.أ");

                        Double truncatedDouble2 = BigDecimal.valueOf(allTotalPrice)
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue();

                        allTotalPrice= (double) (truncatedDouble2+truncatedDouble);
                        textTotalAmount.setText(Double.valueOf(allTotalPrice)+ "د.أ ");
                        textTotalAmount.setVisibility(View.VISIBLE);


                        cartViewHolder.textOptionMenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //creating a popup menu
                                PopupMenu popup = new PopupMenu(CartActivity.this, cartViewHolder.textOptionMenu);
                                //inflating menu from xml resource
                                popup.inflate(R.menu.options_menu);
                                //adding click listener
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.edit_btn:
                                                Intent intent = new Intent(CartActivity.this,ProductDetailActivity.class);
                                                //to get spisefc id for the item
                                                intent.putExtra("pid",cart.getPid());
                                                startActivity(intent);
                                                finish();
                                                return true;
                                            case R.id.delete_btn:
                                               finish();
                                                cartListRef.child("User View")
                                                        .child(Prevalent.currentOnlineUser.getPhone())
                                                        .child("Products")
                                                        .child(cart.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Toast.makeText(CartActivity.this,"تم حذف المنتج بنجاح",Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }
                                });
                                //displaying the popup
                                popup.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return holder;
                    }
                    //check if the recycler view is embty or not
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();
                        if (adapter.getItemCount() == 0){
                            textMassege.setVisibility(View.VISIBLE);
                            textMassege.setText("لا يوجد شيئ في السلة الرجاء أضافة منتجات لشرائها. ");
                            recyclerView.setVisibility(View.GONE);
                            imgMsg1.setVisibility(View.VISIBLE);
                            nextProssecBtn.setVisibility(View.GONE);
                            textTotalAmount.setVisibility(View.GONE);
                            allTotalPriceTv.setVisibility(View.GONE);
                            orderdToSmall.setVisibility(View.GONE);
                            odrerdTo.setVisibility(View.GONE);
                            view.setVisibility(View.GONE);
                            relativeLayout.setVisibility(View.GONE);
                          //  addProductBtn.setVisibility(View.GONE);
                        //    browsproductsBtn.setVisibility(View.VISIBLE);
                            checkOrderState();
                        }else{
                            recyclerView.setVisibility(View.VISIBLE);
                            textMassege.setVisibility(View.GONE);
                            imgMsg1.setVisibility(View.GONE);
                            nextProssecBtn.setVisibility(View.VISIBLE);
                       //     addProductBtn.setVisibility(View.VISIBLE);
                         //   browsproductsBtn.setVisibility(View.GONE);
                            allTotalPriceTv.setVisibility(View.VISIBLE);

                        }
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }
    public void checkOrderState(){
        DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //check the shipping state
                    String sheppingState = dataSnapshot.child("state").getValue().toString();
                    //get the user name
                    String sheppingUserName = dataSnapshot.child("name").getValue().toString();

                    if (sheppingState.equals("shipped")){
                        // يعني انو الطلب تحقق وراج يوصل لل ادمن ويبعثو للزبون
                        // if the order confirmed from the admin
                        Toast.makeText(CartActivity.this,"السيد "+sheppingUserName+"تمت عملية الشراء السابقة بنجاح... سيصلك الطلب قريبا",Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.VISIBLE);
                        textMassege.setVisibility(View.GONE);
                        imgMsg1.setVisibility(View.GONE);
                        nextProssecBtn.setVisibility(View.VISIBLE);
                      //  addProductBtn.setVisibility(View.VISIBLE);
                    //    browsproductsBtn.setVisibility(View.GONE);
                          allTotalPriceTv.setVisibility(View.VISIBLE);

                    }else if(sheppingState.equals("not shipped")){
                        textMassege.setVisibility(View.VISIBLE);
                        textMassege.setText("السيد "+sheppingUserName+" لم تتم عملية الشراء السابقة بعد ... الرجاء الانتظار لحين قبول طلبك. ");
                        recyclerView.setVisibility(View.GONE);
                        imgMsg1.setVisibility(View.VISIBLE);
                        nextProssecBtn.setVisibility(View.GONE);
                        textTotalAmount.setVisibility(View.GONE);
                        allTotalPriceTv.setVisibility(View.GONE);
                      //  addProductBtn.setVisibility(View.GONE);
                       // browsproductsBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void FindViewId(){
        recyclerView=findViewById(R.id.cart_list_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        allTotalPriceTv= findViewById(R.id.sumation);
        nextProssecBtn=findViewById(R.id.next_process_btn);
        textTotalAmount=findViewById(R.id.cart_total_number);
        imgMsg1=findViewById(R.id.msg1);
       // addProductBtn=findViewById(R.id.add_item_btn);
       // browsproductsBtn=findViewById(R.id.add_new_item_btn);
        textMassege=findViewById(R.id.text_massege);
        textTotalAmount.setVisibility(View.GONE);
        dFeeTv= findViewById(R.id.d_fee_price);
        orderdToSmall= findViewById(R.id.orderd_to_small);
        odrerdTo = findViewById(R.id.orderd_to);
        view = findViewById(R.id.vieww);
        relativeLayout = findViewById(R.id.prices_layout);
    }
}
