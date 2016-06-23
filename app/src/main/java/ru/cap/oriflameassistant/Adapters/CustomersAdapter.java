package ru.cap.oriflameassistant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import ru.cap.oriflameassistant.Model.Customer;
import ru.cap.oriflameassistant.R;

/**
 * Created by Maksim on 11.06.2016.
 */
public class CustomersAdapter extends BaseAdapter {

    private ArrayList<Customer> array;
    private Context context;
    private LayoutInflater inflater;

    public CustomersAdapter(Context context, ArrayList<Customer> customers){
        this.context = context;
        array = customers;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public CustomersAdapter(Context context, Customer[] customers){
        this.context = context;
        array = new ArrayList<Customer>();
        for (Customer customer : customers)
            array.add(customer);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_customer_item, parent, false);
        }
        ((TextView) view.findViewById(R.id.CustomerName)).setText(array.get(position).getName());


        return view;
    }
}
