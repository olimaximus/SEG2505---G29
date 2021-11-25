package com.example.projetg29;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class Document {
    private HashMap<String, Intent> map;

    public Document(){
        map = new HashMap<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setImage(String name, Intent i) {
        if(!map.containsKey(name)){
            map.put(name, i);
        }
        else{
            map.replace(name, i);
        }
    }

    public Intent getImage(String name){
        return map.get(name);
    }
}
