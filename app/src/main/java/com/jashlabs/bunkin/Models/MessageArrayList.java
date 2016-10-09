package com.jashlabs.bunkin.Models;

import java.util.ArrayList;

/**
 * Created by Jash on 29-03-2016.
 */
public class MessageArrayList {

    private String headerTitle;
    private ArrayList<String> allItemsInSection;

    public MessageArrayList() {

    }
    public MessageArrayList(String headerTitle, ArrayList<String> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<String> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<String> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

}
