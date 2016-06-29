package ru.cap.oriflameassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.cap.oriflameassistant.Helpers.DataBaseHelper;
import ru.cap.oriflameassistant.Model.Customer;

public class NewCustomer extends AppCompatActivity {
    private Button submitButton;
    private EditText customerNameET;
    private EditText customerNumberET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        setTitle("Новый заказчик");
        submitButton = ((Button) findViewById(R.id.new_customer_submit_button));
        customerNameET = (EditText) findViewById(R.id.new_customer_customer_name);
        customerNumberET = (EditText) findViewById(R.id.new_customer_customer_number);
        final DataBaseHelper DB = new DataBaseHelper(getApplicationContext());
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DB.addCustomer(new Customer(customerNameET.getText().toString(),
                                customerNumberET.getText().toString()));
                        Intent intent = new Intent();
                        intent.putExtra(Customer.ColumnInfo.NAME.getName(), customerNameET.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }
}
