package com.acanimal.java.json.examples;

import com.acanimal.java.json.examples.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GsonRead {

    private static final String FILENAME = "/examples/data.json";
    private static final File file = new File(GsonRead.class.getClass().getResource(FILENAME).getFile());
    private static final InputStream stream = GsonRead.class.getClass().getResourceAsStream(FILENAME);

    /**
     * With the object model read the whole JSON file is loaded on memory and the
     * application gets the desired element.
     */
    public static void readDom() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Gson gson = new GsonBuilder().create();
            Person[] people = gson.fromJson(reader, Person[].class);

            System.out.println("Object mode: " + people[0]);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This is a mixed implementation based on stream and object model. The JSON
     * file is read in stream mode and each object is parsed in object model.
     * With this approach we avoid to load all the object in memory and we are only
     * loading one at a time.
     */
    public static void readStream() {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                // Read data into object model
                Person person = gson.fromJson(reader, Person.class);
                if (person.getId() == 0 ) {
                    System.out.println("Stream mode: " + person);
                }
                break;
            }
            reader.close();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GsonRead.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read file in object model and later in stream mode.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long ti, tf;

        ti = System.currentTimeMillis();
        System.out.println("Start reading in object mode: " + ti);
        GsonRead.readDom();
        tf = System.currentTimeMillis();
        System.out.println("Finish. Total time: " + (tf - ti));

        ti = System.currentTimeMillis();
        System.out.println("Start reading in stream mode: " + ti);
        GsonRead.readStream();
        tf = System.currentTimeMillis();
        System.out.println("Finish. Total time: " + (tf - ti));

    }

}
