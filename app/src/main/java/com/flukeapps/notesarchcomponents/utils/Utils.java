package com.flukeapps.notesarchcomponents.utils;

import com.flukeapps.notesarchcomponents.model.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    private static List<String> names = Arrays.asList("Sheldon Cooper","Penny","Leonard Hofstadter","Howard Wolowitz",
            "Raj Koothrappali");
    private static List<String> places = Arrays.asList("Sheldon's spot", "The elevator", "Comic con",
            "Penny's apartment", "Sheldon's apartment", "Comic store", "Cafeteria");

    public static Scene generateRandomScene(){

        ArrayList<String> names_list = new ArrayList<>(names);
        ArrayList<String> places_list = new ArrayList<>(places);

        String random_character1 = names_list.get(new Random().nextInt(names.size()));
        String random_character2 = names_list.get(new Random().nextInt(names.size()));
        String random_location = places_list.get(new Random().nextInt(places.size()));

        return new Scene(random_character1,random_character2,random_location);
    }
}
