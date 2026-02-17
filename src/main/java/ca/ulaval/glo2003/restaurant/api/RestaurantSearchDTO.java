package ca.ulaval.glo2003.restaurant.api;

import java.util.HashMap;

public record RestaurantSearchDTO(String name, HashMap<String, String> opened) {}
