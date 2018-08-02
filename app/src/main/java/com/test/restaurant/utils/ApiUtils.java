package com.test.restaurant.utils;

import com.test.restaurant.Call.APIService;
import com.test.restaurant.Call.RetrofitClient;
public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://crm.hivetech.co/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
