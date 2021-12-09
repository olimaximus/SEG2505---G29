package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.util.LinkedList;

public class VoirDemandesClients extends AppCompatActivity {

    private static final String TAG = "Permission";
    private Spinner dropdown_service;
    private LinkedList<Service> servicesList;
    private Employe compte;
    private String[] services;
    private ImageView image;
    private Service service_selected;
    private TextView text_formulaire;
    private TextView text_image;
    private int selected_image;
    private Button nextPhoto;
    private Button save;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_demandes_clients);
        //Afficher les demandes des clients
        //Initialiser les éléments du layout
        dropdown_service = findViewById(R.id.dropDown_demandes);
        image = findViewById(R.id.imageDocument);
        text_formulaire = findViewById(R.id.text_formulaireDemandes);
        text_image = findViewById(R.id.text_ImageName);
        nextPhoto = findViewById(R.id.btn_NextPhoto);
        save = findViewById(R.id.button7);
        selected_image = 0;


        //Récupérer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        try {
            compte = dbHandler.findEmploye(extras.getString("Employé"));
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        //Créer la liste du dropdown services
        DBServices dbServices = new DBServices(getApplicationContext());
        servicesList = dbServices.getAllServicesRemplis(compte);
        if(servicesList.size() == 0){
            servicesList.add(new Service(""));
        }
        service_selected = servicesList.get(0);


        updateServices();
        updateDisplay();




        // Option du spinner
        //Spinner des confirmation
        // Text dans values -> strings... selection
        Spinner spinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.Selection);
                choice = array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdown_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                service_selected = servicesList.get(position);
                updateDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Fonction du bouton next image
        nextPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!service_selected.getName().equals("")) {
                    int n = service_selected.getImageKeys().length;
                    selected_image++;
                    selected_image = selected_image % n;
                    updateDisplay();
                }
            }
        });

        //Option du bouton valider
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }


    






    // Mettre le dropdown services à jour
    public void updateServices(){
        services = new String[servicesList.size()];
        for (int i = 0; i < services.length; i++) {
            String name = servicesList.get(i).getName();
            String employe = servicesList.get(i).getEmploye();
            name = name.replace("_E"+employe+"_C"," (");
            services[i] = name + ")";
        }
        if(services[0].equals(")")){
            services[0] = "";
        }
        ArrayAdapter<String> adapterservice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        dropdown_service.setAdapter(adapterservice);
    }

    // Mettre à jour les éléments démontrant la demande choisie
    public void updateDisplay(){
        if(service_selected.getName().equals("")){
            text_formulaire.setText("Formulaire:\n\nDocuments:");
            text_image.setText("Aucune service sélectionné");
            image.setImageResource(R.drawable.ic_launcher_background);
        }
        else{
            String formulaire = "Formulaire:\n";
            String[] keys = service_selected.getInfoKeys();
            for(int i = 0; i < keys.length; i++){
                formulaire += "\n" + keys[i]+ " : " +service_selected.getInfo(keys[i]);
            }
            formulaire += "\n\nDocuments:";
            text_formulaire.setText(formulaire);
            String imageKey = service_selected.getImageKeys()[selected_image];
            text_image.setText(imageKey);
            if(isReadStoragePermissionGranted()) {

                Intent intent = service_selected.getImage(imageKey);
                Uri uri = (Uri) intent.getExtras().get("imageUri");
                image.setImageURI(uri);
            }

        }
    }

    // Vérifier que la permission de lire les photos est donnée
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                }
                break;
        }
    }

    public void save(){
        if(service_selected.getName().equals("")){
            Toast.makeText(getApplicationContext(), "Aucun service n'est sélectionné", Toast.LENGTH_LONG).show();
            return;
        }
        else if(choice.equals("Approuver")){
            Toast.makeText(getApplicationContext(), "Demande approuvée", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Demande rejetée", Toast.LENGTH_LONG).show();
        }
        DBServices dbServices = new DBServices(getApplicationContext());
        dbServices.deleteService(service_selected.getName());
        servicesList.remove(service_selected);
        if(servicesList.isEmpty()){
            servicesList.add(new Service(""));
        }
        updateServices();
        updateDisplay();
    }


}


