package com.example.projetg29;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    EditText editAlertInfo;
    DialogInterface.OnClickListener enterInfo;



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




        // Récupérer les infos de l'activité précédente
        Bundle extras = getIntent().getExtras();
        serviceName = extras.getString("Service");
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






    }

    public void updateFormulaire(){
        formulaireKeys = service.getInfoKeys();
        String result = "Formulaire:\n";
        for(int i = 0; i < formulaireKeys.length; i++){
            result = result+"\n"+formulaireKeys[i]+" : "+service.getInfo(formulaireKeys[i]);
        }
        textFormulaire.setText(result);
    }

    public void updateDocument(){
        documentKeys = service.getImageKeys();
        String result = "Documents:\n";
        for(int i = 0; i < documentKeys.length; i++){
            String image = "Image";
            if(service.getImage(documentKeys[i]).getStringExtra("imageUri")== null){
                image = "";
            }
            result = result+"\n"+documentKeys[i]+" : "+image;
        }
        textDocument.setText(result);
    }

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

}