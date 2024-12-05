package com.huongbien.utils;

import com.google.gson.JsonArray;
import com.huongbien.config.Constants;

public class RefreshJSON {
    public static void clearAllJSON() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.LOGIN_SESSION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }

    public static void clearAllJSONWithoutLoginSession() {
        Utils.writeJsonToFile(new JsonArray(), Constants.CUISINE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.CUSTOMER_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.PAYMENT_QUEUE_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.RESERVATION_PATH);
        Utils.writeJsonToFile(new JsonArray(), Constants.TABLE_PATH);
    }
}
