package ru.cap.oriflameassistant.Model;

import ru.cap.oriflameassistant.Helpers.DataBaseHelper;

/**
 * Created by Maksim on 17.06.2016.
 */
public class InfoBox {
    boolean Success;
    String Message;
    Product Data;

    public InfoBox(boolean success, String message, Product product) {
        Success = success;
        Message = message;
        Data = product;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Product getData() {
        return Data;
    }

    public void setData(Product data) {
        Data = data;
    }
}
