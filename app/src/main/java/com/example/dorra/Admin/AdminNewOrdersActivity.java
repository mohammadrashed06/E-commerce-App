package com.example.dorra.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dorra.Model.AdminOrders;
import com.example.dorra.Prevalent.Prevalent;
import com.example.dorra.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        ordersList=findViewById(R.id.orders_admin_recycler);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int position, @NonNull final AdminOrders adminOrders) {
                        adminOrdersViewHolder.userName.setText("الاسم: "+adminOrders.getName());
                        adminOrdersViewHolder.userPhoneNumber.setText("رقم الهاتف: "+adminOrders.getPhone());
                        adminOrdersViewHolder.userTotalPrice.setText("الحساب النهائي: "+adminOrders.getTotalAmount()+" د.أ");
                        adminOrdersViewHolder.userDateTime.setText("وقت الطلب: "+adminOrders.getDate()+" "+adminOrders.getTime());
                        adminOrdersViewHolder.userShippingAdress.setText("عنوان التسليم: "+adminOrders.getCity()+"/"+adminOrders.getStreetPlace()+"/"+adminOrders.getBuildnumber()+"/"+adminOrders.getHomenumber());
                        adminOrdersViewHolder.showAllProductBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminShowUserProductsActivity.class);
                                String uID = getRef(position).getKey();
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });
                        //to allow the admin delete the order when prder deliverd to customr
                        adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{
                                        "نعم",
                                        "لا"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("هل تم ايصال الطلب الى العميل ؟");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i==0){
                                            //the admin click yes
                                            String uID = getRef(position).getKey();
                                            RemoveOrder(uID);
                                        }else{
                                            ordersRef.child(Prevalent.currentOnlineUser.getPhone()).child("state").setValue("new state");
                                          finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_row,parent,false);
                        return new AdminOrdersViewHolder(view);

                    }

                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAdress;
        public Button showAllProductBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.user_name_orders);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAdress=itemView.findViewById(R.id.order_adress_city);
            showAllProductBtn = itemView.findViewById(R.id.show_all_products_to_admin);

        }
    }
}
