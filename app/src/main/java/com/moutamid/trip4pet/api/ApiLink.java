package com.moutamid.trip4pet.api;

public class ApiLink {

    private static final String API_KEY = "AIzaSyAuIxeEpQQgN84bBitDRksZTcLHtIKSAeY";
    public static final String GET_PLACES = "GET_PLACES";
    private static final String AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";

    public static String searchCities(String input) {
        return AUTOCOMPLETE_URL + "?input=" + input + "&types=(cities)&key=" + API_KEY;
    }
    public static String getPlace(String placeId) {
        return PLACE_DETAILS_URL + "?place_id=" + placeId + "&key=" + API_KEY;
    }
}
