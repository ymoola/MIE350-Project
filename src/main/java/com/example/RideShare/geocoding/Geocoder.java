package com.example.RideShare.geocoding;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

//App ID: hQYGf8MkYoytWpKcNVah
//API key: jjeVTW3p62ycAM7w1ysCqmfZGXSU67j65DXytVaoVio

public class Geocoder {

    private static final String GEOCODING_RESOURCE = "https://geocode.search.hereapi.com/v1/geocode";
    private static final String API_KEY = "jjeVTW3p62ycAM7w1ysCqmfZGXSU67j65DXytVaoVio";

    /**
     *
     * @param query
     * @return json object of coordinates with keys lng, lat
     * @throws IOException
     * @throws InterruptedException
     */
    public static JSONObject GeocodeSync(String query) throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();

        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + encodedQuery;
        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();

        HttpResponse geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        Object arg = geocodingResponse.body();
        JSONObject responseObject = new JSONObject((String) arg);
        JSONArray items = responseObject.getJSONArray("items");
        JSONObject itemsFirst = (JSONObject) items.get(0);
        JSONObject coordinates = itemsFirst.getJSONObject("position");

        return coordinates;
    }

}
