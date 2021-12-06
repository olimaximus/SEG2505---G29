package com.example.projetg29;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemplirServiceActivity extends AppCompatActivity {
    private TextView textServiceName;
    private TextView textFormulaire;
    private TextView textDocument;
    private EditText editInfoName;
    private Button btn_EditFormulaire;
    private Button btn_EditDocument;
    private String serviceName;
    private Service service;
    private String[] formulaireKeys;
    private String[] documentKeys;
    private int selectedInfo;
    private EditText editAlertInfo;
    private DialogInterface.OnClickListener enterInfo;
    private String succursale;
    private Client client;
    private Button btn_SoumettreDemande;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remplir_service);

        // Définir les éléments du layout
        textServiceName = findViewById(R.id.textServiceRemplir);
        textFormulaire = findViewById(R.id.textFormulaire);
        textDocument = findViewById(R.id.textDocument);
        editInfoName = findViewById(R.id.editNomInfo);
        btn_EditFormulaire = findViewById(R.id.btn_RemplirFormulaire);
        btn_EditDocument = findViewById(R.id.btn_RemplirDocument);
        btn_SoumettreDemande = findViewById(R.id.btn_soumettreDemande);




        // Récupérer les infos de l'activité précédente
        Bundle extras = getIntent().getExtras();
        serviceName = extras.getString("Service");
        succursale = extras.getString("Employé");
        MyDBHandler dbHandler = new MyDBHandler(getApplicationContext());
        client = dbHandler.findClient(extras.getString("Client"));
        DBServices dbServices = new DBServices(getApplicationContext());
        service = dbServices.findService(serviceName);
        textServiceName.setText(service.getName());

        updateFormulaire();
        updateDocument();

        // Fonction du bouton remplir formulaire
        btn_EditFormulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInfo();
            }
        });

        // Alerte pour remplir une information
        enterInfo = new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        String result = editAlertInfo.getText().toString().trim();
                        result = result.replace(" ", "-");

                        if(result.equals("")){
                            Toast.makeText(getApplicationContext(), "Veuillez remplir l'information", Toast.LENGTH_LONG).show();
                        }
                        else{

                            service.setInfo(formulaireKeys[selectedInfo], result);
                            updateFormulaire();
                            dialog.dismiss();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        // Fonction du bouton remplir document
        btn_EditDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        // Fonction du bouton soumettre demande
        btn_SoumettreDemande.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                soumettreDemande();
            }
        });



    }


    // Mettre à jour le texte du formulaire
    public void updateFormulaire(){
        formulaireKeys = service.getInfoKeys();
        String result = "Formulaire:\n";
        for(int i = 0; i < formulaireKeys.length; i++){
            result = result+"\n"+formulaireKeys[i]+" : "+service.getInfo(formulaireKeys[i]);
        }
        textFormulaire.setText(result);
    }

    // Mettre à jour le texte du document
    public void updateDocument(){
        documentKeys = service.getImageKeys();
        String result = "Documents:\n";
        for(int i = 0; i < documentKeys.length; i++){
            String image = "Image";
            if(service.getImage(documentKeys[i]).getExtras().get("imageUri").toString().equals("")){
                image = "";
            }
            result = result+"\n"+documentKeys[i]+" : "+image;
        }
        textDocument.setText(result);
    }

    // Ajouter une information au formulaire
    public void addInfo(){
        String input = editInfoName.getText().toString().trim();
        input = input.replace(" ", "-");
        boolean isContained = false;
        for(int i = 0; i<formulaireKeys.length; i++){
            if(formulaireKeys[i].equals(input)){
                isContained = true;
                selectedInfo = i;
                break;
            }
        }
        if(!isContained){
            Toast.makeText(getApplicationContext(), "Veuillez entrer le nom d'une information contenu dans le formulaire", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(RemplirServiceActivity.this);
            editAlertInfo = new EditText(RemplirServiceActivity.this);
            builder.setMessage("Veuillez entrer votre "+formulaireKeys[selectedInfo]).setPositiveButton("Enregistrer", enterInfo).setNegativeButton("Annuler", enterInfo);
            builder.setView(editAlertInfo);
            builder.show();


        }
    }

    // Ajouter une image aux documents
    public  void addImage(){
        String input = editInfoName.getText().toString().trim();
        input = input.replace(" ", "-");
        boolean isContained = false;
        for(int i = 0; i<documentKeys.length; i++){
            if(documentKeys[i].equals(input)){
                isContained = true;
                selectedInfo = i;
                break;
            }
        }
        if(!isContained){
            Toast.makeText(getApplicationContext(), "Veuillez entrer le nom d'une information contenu dans les documents", Toast.LENGTH_LONG).show();
        }
        else {
            Intent camera_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(camera_intent, 1);
        }
    }


    // Récupérer l'image de la galerie photo
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data){
            Uri uri = data.getData();
            data.putExtra("imageUri", uri);
            service.setImage(documentKeys[selectedInfo], data);
            updateDocument();
            Toast.makeText(getApplicationContext(), "Image ajoutée aux documents", Toast.LENGTH_LONG).show();
        }
    }

    // Soumettre la demande
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void soumettreDemande(){
        service = client.demanderService(service, succursale);
        serviceName = service.getName();
        DBServices dbServices = new DBServices(getApplicationContext());
        dbServices.deleteService(serviceName);
        dbServices.addService(service);
        Toast.makeText(getApplicationContext(), serviceName, Toast.LENGTH_LONG).show();
    }
}