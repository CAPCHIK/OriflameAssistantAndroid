package ru.cap.oriflameassistant.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.cap.oriflameassistant.Model.Order;
import ru.cap.oriflameassistant.R;

/**
 * Created by Maksim on 20.06.2016.
 */
public class OrdersAdapter extends BaseAdapter {

    private List<Order> orders;
    private Context context;
    LayoutInflater inflater;

    public OrdersAdapter(Context context, List<Order> orders){
        this.orders = orders;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public OrdersAdapter(Context context, Order[] orders) {
        this(context, Arrays.asList(orders));
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i("OrdersAdapter", "getItemId : "+ position + "order : " + new Gson().toJson(orders.get(position)));
        return orders.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view= inflater.inflate(R.layout.fragment_order, null);
        ((TextView) view.findViewById(R.id.customer)).setText(orders.get(position).getCustomer().getName());
        ((TextView) view.findViewById(R.id.price)).setText(String.valueOf(orders.get(position).getTotalPrice()));
        return view;
    }
}
