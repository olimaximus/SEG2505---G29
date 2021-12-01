package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class VoirDemandesClients extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner dropdown_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_demandes_clients);
        //Afficher les demandes des clients
        //Dropdown pour afficher les demandes des clients
        dropdown_service = findViewById(R.id.dropDown_demandes);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ####);
        dropdown_service.setAdapter(adapter2);
        dropdown_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = ####[position];
                int i = select.indexOf(' ');
                #### = select.substring(0, i);
                #### = select.substring(i+2, select.length()-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Option du spinner
        //Spinner des confirmation
        // Text dans values -> strings... selection
        Spinner spinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    
    //Option du bouton valider
    //A ecrire apres avoir fait les fonctionnalites du client




}
