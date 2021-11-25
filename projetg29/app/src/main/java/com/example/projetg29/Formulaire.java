package com.example.projetg29;

import java.util.HashMap;

public class Formulaire {
    private HashMap<String, String> map;

    // Créer un formulaire
    public Formulaire(){
        map = new HashMap<>();
    }

    // Ajouter/modifier une information
    public void setInfo(String info, String contenu){
        if(!map.containsKey(info)){
            map.put(info, contenu);
        }
        else{
            map.replace(info, contenu);
        }
    }

    // Obtenir une information
    public String getInfo(String info){
        return map.get(info);
    }



}
