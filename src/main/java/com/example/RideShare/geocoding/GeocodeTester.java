package com.example.RideShare.geocoding;

import org.json.JSONObject;

import java.io.IOException;

public class GeocodeTester {

    public static void main(String[] args) throws IOException, InterruptedException {
        String address = "427 Euclid Ave Toronto ON";
        JSONObject result = Geocoder.GeocodeSync(address);
        System.out.println(result);
        double lat = result.getDouble("lat");
        System.out.println(lat);
    }
}
