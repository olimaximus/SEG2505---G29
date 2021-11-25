package com.example.projetg29;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;

public class Service {
    private HashMap<String, String> formulaire; // Un formulaire contient des informations auquelles on associe une valeur.
                                                // On peut mettre autant d'informations que l'on veut alors on a seulement besoin d'un formulaire
                                                // Ex: Prénom, Maxime

    private HashMap<String, Intent> document; // Un document contient des informations auquelles on associe une image sous forme de Intent
                                              // Ex: Preuve de domicile, "photo de la preuve de domicile"

    private String name;

    public Service(String name){
        this.name = name;
        formulaire = new HashMap<>();
        document = new HashMap<>();
    }
    public Service(){
        name = "";
        formulaire = new HashMap<>();
        document = new HashMap<>();
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

    // Supprimer une image du document
    public boolean deleteImage(String name){
        boolean result = false;
        if(document.containsKey(name)){
            document.remove(name);
            result = true;
        }
        return result;
    }
}
