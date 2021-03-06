package com.example.projetg29;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Set;

public class Service {
    private HashMap<String, String> formulaire; // Un formulaire contient des informations auquelles on associe une valeur.
    // On peut mettre autant d'informations que l'on veut alors on a seulement besoin d'un formulaire
    // Ex: Prénom, Maxime

    private HashMap<String, Intent> document; // Un document contient des informations auquelles on associe une image sous forme de Intent
    // Ex: Preuve de domicile, "photo de la preuve de domicile"

    private String name;
    private String employe;
    private String client;

    public Service(String name){
        this.name = name;
        formulaire = new HashMap<>();
        document = new HashMap<>();
        employe = null;
        client = null;
    }
    public Service(){
        name = "";
        formulaire = new HashMap<>();
        document = new HashMap<>();
        employe = null;
        client = null;
    }

    public Service(String name, HashMap<String, String> formulaire, HashMap<String, Intent> document){
        this.name = name;
        this.formulaire = formulaire;
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Ajouter/modifier une information du formulaire
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setInfo(String info, String contenu){
        if(!formulaire.containsKey(info)){
            formulaire.put(info, contenu);
        }
        else{
            formulaire.replace(info, contenu);
        }
    }

    // Obtenir une information du formulaire
    public String getInfo(String info){
        return formulaire.get(info);
    }

    // Obtenir le tableau des clés du formulaire
    public String[] getInfoKeys(){
        Set<String> set = formulaire.keySet();
        String[] result = Arrays.copyOf(set.toArray(), set.toArray().length, String[].class);
        return result;
    }

    // Supprimer une information du formulaire
    public boolean deleteInfo(String info){
        boolean result = false;
        if(formulaire.containsKey(info)){
            formulaire.remove(info);
            result = true;
        }
        return result;
    }

    // Ajouter/modifier une image du document
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setImage(String name, Intent i) {
        if(!document.containsKey(name)){
            document.put(name, i);
        }
        else{
            document.replace(name, i);
        }
    }

    // Obtenir une image du document
    public Intent getImage(String name){
        return document.get(name);
    }

    // Obtenir le tableau des clés du document
    public String[] getImageKeys(){
        Set<String> set = document.keySet();
        String[] result = Arrays.copyOf(set.toArray(), set.toArray().length, String[].class);

        return result;
    }

    // Supprimer une image du document
    public boolean deleteImage(String name){
        boolean result = false;
        if(document.containsKey(name)){
            document.remove(name);
            result = true;
        }
        return result;
    }

    // Retourner la map en String pour la base de données
    public String getFormulaireMap(){
        JSONObject jsonObject = new JSONObject(formulaire);
        String result = jsonObject.toString();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDocumentMap(){
        HashMap<String, String> newMap = new HashMap<>();
        String[] keys = getImageKeys();
        for(int i = 0; i < keys.length; i++){
            Intent intent = document.get(keys[i]);
            if(intent!=null) {
                Uri uri = intent.getParcelableExtra("imageUri");
                newMap.put(keys[i], uri.toString());
            }
            else{
                newMap.put(keys[i], "");
            }
        }
        JSONObject jsonObject = new JSONObject(newMap);
        String result = jsonObject.toString();
        return result;
    }

    public String getEmploye(){
        return employe;
    }

    public String getClient(){
        return client;
    }

    public void assignTo(String employe, String client){
        this.employe = employe;
        this.client = client;
    }


}
