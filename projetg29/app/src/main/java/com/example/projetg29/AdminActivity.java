package com.example.projetg29;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;

public class AdminActivity extends AppCompatActivity {
    private Administrateur compte;
    private String compte_a_supprimer;
    private String type_a_supprimer;
    private LinkedList<Service> servicesList;
    private String[] services;
    private Spinner dropdown_service;
    private Service selected_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);
        Spinner dropdown = findViewById(R.id.dropDown_comptes);
        Button supprimerCompte = findViewById(R.id.supprimer_compte);
        dropdown_service = findViewById(R.id.dropDown_Services);
        EditText serviceName = findViewById(R.id.editServiceName);
        Button addService = findViewById(R.id.button_addService);
        Button supprimerService = findViewById(R.id.supprimer_service);
        Button modifierService = findViewById(R.id.button_modifier);




        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Administrateur(username, password);

        // Créer la liste d'éléments du dropdown Compte
        LinkedList<String> comptesList;
        MyDBHandler dbHandler = new MyDBHandler(this);
        comptesList = dbHandler.getAllComptes();
        String[] comptes = new String[comptesList.size()/2];
        for(int i = 0; i < comptes.length; i++){
            String temp = comptesList.removeFirst();
            comptes[i] = temp+" ("+comptesList.removeFirst()+")";
        }

        // Créer un adapter pour le dropdown comptes
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, comptes);
        dropdown.setAdapter(adapter);

        // Élément sélectionné par le dropdown comptes
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select = comptes[position];
                int i = select.indexOf(' ');
                compte_a_supprimer = select.substring(0, i);
                type_a_supprimer = select.substring(i+2, select.length()-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Créer la liste d'éléments du dropdown Service
        DBServices dbServices = new DBServices(AdminActivity.this);
        servicesList = dbServices.getAllServices();
        if(servicesList.size() == 0){
            servicesList.add(new Service());
        }
        updateServices();



        // Élément sélectionné par le dropdown Service
        dropdown_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_service = servicesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Administrateur");

        // Message de confirmation de suppression de compte
        DialogInterface.OnClickListener confirm_delete = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHandler.deleteCompte(compte_a_supprimer, type_a_supprimer);
                        dialog.dismiss();
                        finish();
                        startActivity(getIntent());
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        // Fonction du bouton supprimer compte
        supprimerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type_a_supprimer.equals("Administrateur")){
                    Toast.makeText(getApplicationContext(),"Vous ne pouvez pas supprimer ce compte", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    builder.setMessage("Voulez-vous vraiment supprimer " + compte_a_supprimer + "?").setPositiveButton("Oui", confirm_delete).setNegativeButton("Non", confirm_delete).show();
                }
            }
        });

        // Fonction du bouton addService
        addService.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String name = serviceName.getText().toString().trim();
                name = name.replace(" ", "-");
                if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "Nom de service invalide", Toast.LENGTH_LONG).show();
                }
                else {
                    Service service = new Service(name);
                    // Retirer l'élément bidon qui fait fonctionner le dropdown
                    if(servicesList.getFirst().getName().equals("")){
                        servicesList.removeFirst();
                    }
                    // Vérifier si le service existe déja
                    for(int i = 0; i < servicesList.size(); i++){
                        if(service.getName().equals(servicesList.get(i).getName())){
                            Toast.makeText(getApplicationContext(), "Ce service existe déja", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    DBServices dbServices1 = new DBServices(AdminActivity.this);
                    dbServices1.addService(service);
                    servicesList.add(service);
                    Toast.makeText(getApplicationContext(), "Service ajouté avec succès", Toast.LENGTH_LONG).show();
                    updateServices();
                    dropdown_service.setSelection(services.length-1);
                }
            }

        });

        // Fonction du bouton supprimer service
        supprimerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si un élément bidon est sélectionné
                if(selected_service.getName().equals("")){
                    Toast.makeText(getApplicationContext(), "Aucun service à supprimer", Toast.LENGTH_LONG).show();
                }
                else{
                    DBServices dbServices1 = new DBServices(AdminActivity.this);
                    dbServices1.deleteService(selected_service.getName());
                    servicesList.remove(selected_service);
                    if(servicesList.isEmpty()){
                        servicesList.add(new Service(""));
                    }
                    updateServices();
                }
            }
        });

        // Fonction du bouton modifier service
        modifierService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_service.getName().equals("")){
                    Toast.makeText(getApplicationContext(), "Aucun service à modifier", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(AdminActivity.this, ModifierServiceActivity.class);
                    intent.putExtra("Service", selected_service.getName());
                    startActivity(intent);
                }
            }
        });


    }
    public void updateServices(){
        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            services[i] = servicesList.get(i).getName();
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);

    }

}
