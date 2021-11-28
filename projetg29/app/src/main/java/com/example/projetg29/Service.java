package com.example.projetg29;

public class Service {
    private Formulaire formulaire;
    private Document document;
    private String name;

    public Service(String name){
        this.name = name;
        formulaire = new Formulaire();
        document = new Document();
    }
    public Service(){
        name = "";
        formulaire = new Formulaire();
        document = new Document();
    }

    public Formulaire getFormulaire() {
        return formulaire;
    }

    public Document getDocument() {
        return document;
    }

    public String getName() {
        return name;
    }
}
