package ru.cap.oriflameassistant.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.cap.oriflameassistant.Model.Product;
import ru.cap.oriflameassistant.ProductItem;

/**
 * Created by Maksim on 11.06.2016.
 */
public class ProductsAdapter extends BaseAdapter {

    private ArrayList<Product> array;
    private Context context;

    public ProductsAdapter(Context context, ArrayList<Product> array) {
        this.array = array;
        this.context = context;
    }public ProductsAdapter(Context context, Product[] array) {
        this.array = new ArrayList<>();
        Collections.addAll(this.array, array);
        this.context = context;
    }

    public void addProduct(Product product){
        array.add(product);
        notifyDataSetChanged();
    }
    public void addProducts(List<Product> products){
        for (Product product : products) {
            array.add(product);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return array.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ProductItem fragment = ProductItem.getInstance(array.get(position));
        if (view == null) {
            view = fragment.onCreateView(((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)), parent, null);
        }
        fragment.fillView(view);
        return view;
    }

    public List<Product> getProducts() {
        return array;
    }

    public void remove(int position) {
        array.remove(position);
        notifyDataSetChanged();
    }
}
