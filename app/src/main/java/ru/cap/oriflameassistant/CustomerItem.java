package ru.cap.oriflameassistant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.cap.oriflameassistant.Model.Customer;
import ru.cap.oriflameassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerItem extends Fragment {

    private TextView customerNameTV;


    public CustomerItem() {
        // Required empty public constructor
    }

    public CustomerItem getInstance(Customer customer){
        CustomerItem fragment = new CustomerItem();
        Bundle bundle = new Bundle();
        bundle.putString("NameCustomer", customer.getName());
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_item, container, false);
        customerNameTV = ((TextView) view.findViewById(R.id.CustomerName));
        customerNameTV.setText(getArguments().getString("NameCustomer"));
        return view;
    }

}
