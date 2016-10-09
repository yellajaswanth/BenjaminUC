package com.jashlabs.bunkin.Utils;

import java.util.ArrayList;

/**
 * Created by Jash on 3/18/2016.
 */
public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);

        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            data.add("SAMPLE #");
        }

        return data;
    }

}
