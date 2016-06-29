package ru.cap.oriflameassistant;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import ru.cap.oriflameassistant.Adapters.ProductsAdapter;
import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Model.Product;

public class ProductList extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setTitle(getResources().getString(R.string.products_list) + " загрузка...");
        listView = (ListView) findViewById(R.id.productsListContainer);
        new ProductsLoader().execute();
    }

    private class ProductsLoader extends AsyncTask<Void, Void, Product[]> {
        @Override
        protected Product[] doInBackground(Void... params) {
            return new DataBaseHelper(getApplicationContext()).getProducts();
        }

        @Override
        protected void onPostExecute(Product[] products) {
            listView.setAdapter(new ProductsAdapter(ProductList.this, products));
            ProductList.this.setTitle(getResources().getString(R.string.products_list));
        }
    }
}
