package com.example.projetg29;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.LinkedList;
/*
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

 */

public class ClientActivity extends AppCompatActivity {
    private Client compte;
    private TextView welcomeText;
    private String[] succursales;
    private LinkedList<String> succursalesList;
    private Spinner dropdown_succursales;
    private String selected_succursale;

    private Spinner dropdown_Services;
    private String[] services;
    private String selected_service;

    private EditText editText;
    private Button btn;
    private Button btn_RemplirService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Définir les éléments du layout
        welcomeText = findViewById(R.id.textView5);
        dropdown_succursales = findViewById(R.id.dropDown_Employes);
        dropdown_Services = findViewById(R.id.dropDown_ServicesClient);
        btn_RemplirService = findViewById(R.id.btn_RemplirService);


        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        compte = dbHandler.findClient(username);

        // Créer la liste d'éléments du dropdown services
        succursalesList = dbHandler.getAllEmployes();
        if(succursalesList.size() == 0){
            succursalesList.add("");
        }
        try {
            updateSuccursales();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        // Élément sélectionné par le dropdown succursales
        dropdown_succursales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_succursale = succursalesList.get(position);
                try {
                    updateServices();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Élément sélectionné par le dropdown services
        dropdown_Services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_service = services[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Mettre à jour le texte
        welcomeText.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Client");


        //Fonction du bouton remplir service
        btn_RemplirService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemplirService();
            }
        });



    }

    public void updateSuccursales() throws JSONException {
        succursales = new String[succursalesList.size()];
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        for (int i = 0; i < succursales.length; i++) {
            Employe employe = dbHandler.findEmploye(succursalesList.get(i));
            succursales[i] = succursalesList.get(i)+" ("+employe.getHeures()+")";
        }
        ArrayAdapter<String> adaptersuccursales = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, succursales);
        dropdown_succursales.setAdapter(adaptersuccursales);
    }

    public void updateServices() throws JSONException {
        if(!selected_succursale.equals("")) {
            MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
            Employe employe = dbHandler.findEmploye(selected_succursale);
            services = employe.getServicesArray();
            if (services.length == 0) {
                services = new String[]{""};
            }
        }
        else{
            services = new String[]{""};
        }
        ArrayAdapter<String> adapterservices = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_Services.setAdapter(adapterservices);
    }

    public void openRemplirService(){
        if(selected_service.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez d'abord choisir un service", Toast.LENGTH_LONG).show();
            return;
        }
        DBServices dbServices = new DBServices(getApplicationContext());
        if(dbServices.findService(selected_service) == null){
            Toast.makeText(getApplicationContext(), "Ce service à été supprimé par l'administrateur", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(ClientActivity.this, RemplirServiceActivity.class);
            intent.putExtra("Service", selected_service);
            intent.putExtra("Employé", selected_succursale);
            intent.putExtra("Client", compte.getUsername());
            startActivity(intent);
        }
    }

}
