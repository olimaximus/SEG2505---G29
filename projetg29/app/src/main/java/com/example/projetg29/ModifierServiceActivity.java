package com.example.projetg29;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifierServiceActivity extends AppCompatActivity {
    String serviceName;
    Service service;
    TextView formulaire;
    TextView document;
    EditText input;
    Button btnFormulaire;
    Button btnDocument;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_service);

        // Récupérer les infos de l'activité précédente
        Bundle extras = getIntent().getExtras();
        serviceName = extras.getString("Service");
        service = new Service(serviceName);


        // Définir les éléments du layout
        TextView titre = findViewById(R.id.text_service_modifier);
        formulaire = findViewById(R.id.textDisplayFormulaire);
        document = findViewById(R.id.textDisplayDocuments);
        input = findViewById(R.id.editInfoService);
        btnFormulaire = findViewById(R.id.btn_addFormulaire);
        btnDocument = findViewById(R.id.btn_addDocument);


        // Mettre à jour le texte
        updateText();
        titre.setText(serviceName);

        // Fonction du bouton formulaire
        btnFormulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterFormulaire();
            }
        });

        // Fonction du bouton formulaire
        btnDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajouterDocument();
            }
        });




    }

    public void updateText(){
        // Mettre à jour le texte du formulaire
        String[] keysFormulaire = service.getInfoKeys();
        String resultFormulaire = "Formulaire:"+"\n \n";
        for(int i = 0; i < keysFormulaire.length; i++){
            resultFormulaire += keysFormulaire[i] + "\n";
        }
        formulaire.setText(resultFormulaire);

        // Mettre à jour le texte du document
        String[] keysDocument = service.getImageKeys();
        String resultDocument = "Documents:"+"\n \n";
        for(int i = 0; i < keysDocument.length; i++){
            resultDocument += keysDocument[i] + "\n";
        }
        document.setText(resultDocument);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ajouterFormulaire(){
        String info = input.getText().toString();
        if(info.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez d'abord entrer une information", Toast.LENGTH_LONG).show();
        }
        else {
            service.setInfo(info, "");
            updateText();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ajouterDocument(){
        String info = input.getText().toString();
        if(info.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez d'abord entrer une information", Toast.LENGTH_LONG).show();
        }
        else {
            service.setImage(info, null);
            updateText();
        }
    }
}