package com.example.projetg29;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InscriptionEmploye extends AppCompatActivity {

    private EditText username_edit;
    private EditText password_edit;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Récupérer le type d'utilisateur de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String user_type = extras.getString("user_type");

        // Définir les éléments du layout
        Button creer = findViewById(R.id.button4);
        this.username_edit = findViewById(R.id.editTextTextPersonName);
        this.password_edit = findViewById(R.id.editTextTextPersonName4);


        // Fonction du bouton "Enregistrer"
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupérer le nom d'utilisateur et le mot de passe
                username = username_edit.getText().toString();
                password = password_edit.getText().toString();


                // Login d'un administrateur: Un seul compte est possible pour l'instant, User: admin et Password: 123admin456
                if(user_type.equals("administrateur")) {
                    if (username.equals("admin") && password.equals("123admin456")) {
                        Employe compte = new Employe(username, password);
                        signup(compte);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Utilisateur ou mot de passe invalide", Toast.LENGTH_LONG).show();
                    }
                }

                else if(username.equals("delete")){
                    MyDBHandler db = new MyDBHandler(getApplicationContext());
                    db.deleteAll();
                    Toast.makeText(getApplicationContext(), "Tous les comptes ont été supprimés", Toast.LENGTH_LONG).show();
                }

                // Login d'un employé: Pas de nom ou de mot de passe vide
                else if(user_type.equals("employé")){
                    if(username.equals("") || password.equals("")){
                        Toast.makeText(getApplicationContext(), "Utilisateur ou mot de passe invalide", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Employe compte = new Employe(username, password);
                        signup(compte);
                    }
                }

                // Login d'un client: même chose que l'employé, mais séparé pour les implémentations futures
                else if(user_type.equals("client")){
                    if(username.equals("") || password.equals("")){
                        Toast.makeText(getApplicationContext(), "Utilisateur ou mot de passe invalide", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Client compte = new Client(username, password);
                        signup(compte);
                    }
                }
            }
        });
    }

    public void signup(Administrateur compte){

        MyDBHandler dbHandler = new MyDBHandler(this);
        if(dbHandler.findCompte(compte.getUsername()) == null) {
            dbHandler.addCompte(compte);
            Toast.makeText(getApplicationContext(), "Compte créé avec succès", Toast.LENGTH_LONG).show();
        }
        else{
            Compte existant = dbHandler.findCompte(compte.getUsername());
            if(!existant.getPassword().equals(compte.getPassword())){
                Toast.makeText(getApplicationContext(), "Mot de passe incorrect", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
            }
        }
        Intent otherActivity = new Intent(getApplicationContext(), AdminActivity.class);
        // Transférer les informations à l'écran de bienvenue
        otherActivity.putExtra("user", compte.getUsername());
        otherActivity.putExtra("password", compte.getPassword());
        startActivity(otherActivity);
    }

    public void signup(Employe compte){

        MyDBHandler dbHandler = new MyDBHandler(this);
        if(dbHandler.findCompte(compte.getUsername()) == null) {
            dbHandler.addCompte(compte);
            Toast.makeText(getApplicationContext(), "Compte créé avec succès", Toast.LENGTH_LONG).show();
        }
        else{
            Compte existant = dbHandler.findCompte(compte.getUsername());
            if(!existant.getPassword().equals(compte.getPassword())){
                Toast.makeText(getApplicationContext(), "Mot de passe incorrect", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
            }
        }
        Intent otherActivity = new Intent(getApplicationContext(), EmployeActivity.class);
        // Transférer les informations à l'écran de bienvenue
        otherActivity.putExtra("user", compte.getUsername());
        otherActivity.putExtra("password", compte.getPassword());
        startActivity(otherActivity);
    }

    public void signup(Client compte) {

        MyDBHandler dbHandler = new MyDBHandler(this);
        if (dbHandler.findCompte(compte.getUsername()) == null) {
            dbHandler.addCompte(compte);
            Toast.makeText(getApplicationContext(), "Compte créé avec succès", Toast.LENGTH_LONG).show();
        } else {
            Compte existant = dbHandler.findCompte(compte.getUsername());
            if (!existant.getPassword().equals(compte.getPassword())) {
                Toast.makeText(getApplicationContext(), "Mot de passe incorrect", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
            }
        }
        Intent otherActivity = new Intent(getApplicationContext(), ClientActivity.class);
        // Transférer les informations à l'écran de bienvenue
        otherActivity.putExtra("user", compte.getUsername());
        otherActivity.putExtra("password", compte.getPassword());
        startActivity(otherActivity);
    }
