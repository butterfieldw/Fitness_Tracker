package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String dataFile = "data.txt";
    private ArrayList<String> labels;
    private ArrayList<ArrayList<Integer>> values;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        labels = new ArrayList<String>();
        values = new ArrayList<ArrayList<Integer>>();

        context = this.getApplicationContext();
        createFile();
        try {
            loadData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Spinner spin = findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        labels); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spin.setAdapter(spinnerArrayAdapter);
    }

    public void createFile() {
        String filename = "data.txt";
        String[] exercises = { "push ups", "sit ups", "pull ups" };
        int[][] repetitions = {{10,15, 20}, {20, 25, 30}, {5, 7, 10}};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exercises.length; i++) {
            sb.append(exercises[i] + " ");
            int[] reps = repetitions[i];
            for (int r = 0; r < reps.length; r++) {
                sb.append(i);
                if (r < reps.length - 1) {
                    sb.append(" ");
                } else {
                    sb.append("\n");
                }
            }
        }
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadData() throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(dataFile);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            int index = 0;
            while (line != null) {
                String label = line.substring(0, line.indexOf(" "));
                labels.add(label);
                String valuesList = line.substring(line.indexOf(" ") + 1);
                String[] stringValues = valuesList.split(" ");
                values.add(new ArrayList<Integer>());
                for(String str : stringValues) {
                    int val = Integer.parseInt(str);
                    values.get(index).add(val);
                }
                line = reader.readLine();
                index++;
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            // Nothing
        }
    }
}