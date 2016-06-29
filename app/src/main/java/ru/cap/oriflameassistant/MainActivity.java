package ru.cap.oriflameassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ru.cap.oriflameassistant.Adapters.OrdersAdapter;
import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Helpers.OriflameProductFactory;
import ru.cap.oriflameassistant.Model.Order;

public class MainActivity extends AppCompatActivity {

    private ListView OrderList;

    DataBaseHelper sqlHelper;
    DataBaseHelper db;

    FloatingActionButton fab;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            updateData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void lol(MenuItem item){
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Список заказов");
        sqlHelper = new DataBaseHelper(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DataBaseHelper(getApplicationContext());
        db.printOrderToProduct();
        db.printOrders();
        OriflameProductFactory.init(db);
        fab = ((FloatingActionButton) findViewById(R.id.fab));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewOrderActivity.class);
                startActivity(intent);
            }
        });

        OrderList = ((ListView) findViewById(R.id.orderList));
        OrderList.setOnItemClickListener(new OrderListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_to_products).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, ProductList.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), "Не реализовано", Toast.LENGTH_LONG)
                        .show();
                return true;
            }
        });
        return true;
    }
    private void updateData() throws Exception {
        OrdersAdapter adapter = new OrdersAdapter(getApplicationContext(), db.getOrders());
        OrderList.setAdapter(adapter);
    }



    private class OrderListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, OrderInfo.class);
            intent.putExtra(Order.ColumnInfo.ID.getName(), id);
            startActivity(intent);
        }
    }
}
