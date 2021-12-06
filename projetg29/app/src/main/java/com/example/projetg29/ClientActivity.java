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

    /*
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Object StorageReference;

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Définir les éléments du layout
        editText = findViewById(R.id.editTextClient);
        btn = findViewById(R.id.button8);
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
        updateSuccursales();

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

        /*
        StorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");
        btn.setEnabled(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

         */

        /*
        @Override
        protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData()!=null){{
                btn.setEnabled(true);
                editText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadPDFfileFirebase(data.getData());
                    }
                });
            }
            }


        }

         */

    }

    /*
    private void uploadPDFfileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();
        storageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");
        reference.File(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(uploadTask.TaskSnapshot taskSnapshot){

                Task<Uri> uriTask = taskSnapshot.getStrorage().getDownloadUri();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                putPDF putPDF = new putPDF(editText.getText().toString(),uri.toString());
                databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                Toast.makeText(ClientActivity.this, "file upload",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                )
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot){

                double progress = (100.0 * snapshot.getBytesTrandferres())/getTotalByteCount();
                progressDialog.setCancelMessage("File Upload..." + (int) progress+"%");


            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("applocation/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

     */
    public void updateSuccursales(){
        succursales = new String[succursalesList.size()];
        for (int i = 0; i < succursales.length; i++) {
            succursales[i] = succursalesList.get(i);
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
        else{
            Intent intent = new Intent(ClientActivity.this, RemplirServiceActivity.class);
            intent.putExtra("Service", selected_service);
            intent.putExtra("Employé", selected_succursale);
            intent.putExtra("Client", compte.getUsername());
            startActivity(intent);
        }
    }

}
