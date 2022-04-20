/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonmanager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

/**
 *
 * @author mauri
 */
public class JSONManager {
    
    public static String JSON_FILE;
    
    public static void main(String[] args){
        ArrayList<Libro> libri = new ArrayList<Libro>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Inserire nome file: ");
            JSON_FILE = br.readLine();
            JSONWriter(libri);
            JSONReader(libri);
        } catch (IOException ex) {
            System.err.print(ex);
        }
    }
    
    public static void JSONReader(ArrayList<Libro> libri){
        try {
            InputStream input = new FileInputStream(JSON_FILE);
            
            JsonReader jsonReader = Json.createReader(input);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            JsonObject innerJsonObject = jsonObject.getJsonObject("libreria");
            JsonArray jsonArray = innerJsonObject.getJsonArray("libri");
            
            Iterator<JsonValue> jsonIt = jsonArray.iterator();
            while(jsonIt.hasNext()){
                JsonValue element = jsonIt.next();
                Libro libro = new Libro(element.asJsonObject().getString("genere"), element.asJsonObject().getString("titolo"),
                        element.asJsonObject().getString("autore"), (float)element.asJsonObject().getJsonNumber("prezzo").doubleValue());
                libri.add(libro); 
            }

            Iterator<Libro> libriIt = libri.iterator();
            while(libriIt.hasNext()){
                System.out.println(libriIt.next().getTitolo());
            }
            
        } catch (FileNotFoundException ex) {
            System.err.print(ex);
        }
    }
    
    public static void JSONWriter(ArrayList<Libro> libri){
        try {
            libri.add(new Libro("fantasy", "Il signore degli anelli", "J.R.R. Tolkien", 30.0f));
            libri.add(new Libro("fantasy", "Lo Hobbit", "J.R.R. Tolkien", 9.9f));

            JsonObjectBuilder rootObject = Json.createObjectBuilder();
            JsonObjectBuilder booksObject = Json.createObjectBuilder();
            JsonArrayBuilder bookArray = Json.createArrayBuilder();
            
            Iterator<Libro> libriIt = libri.iterator();
            while(libriIt.hasNext()){
                Libro libro = libriIt.next();
                JsonObjectBuilder bookObject = Json.createObjectBuilder();
                bookObject.add("genere", libro.getGenere());
                bookObject.add("titolo", libro.getTitolo());
                bookObject.add("autore", libro.getAutore());
                bookObject.add("prezzo", libro.getPrezzo());
                bookArray.add(bookObject.build());           
            }

            booksObject.add("libri", bookArray.build());
            rootObject.add("libreria", booksObject.build());
        
            OutputStream output = new FileOutputStream(JSON_FILE);
            JsonWriter jsonWriter = Json.createWriter(output);
        
            jsonWriter.writeObject(rootObject.build());

            jsonWriter.close();
            output.close();
            
        } catch (FileNotFoundException ex) {
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }    
    }
}
