package com.example.projetg29;

import java.util.ArrayList;

public class Employe extends Compte{
    private ArrayList<String> services;
    private String heures;

    public Employe(String username, String password){
        super(username, password);
        setType("Employé");
        services = new ArrayList<>();
        heures = "";
    }

    public ArrayList<String> getServicesList() {
        return services;
    }

    public String[] getServicesArray(){
        String[] result = new String[services.size()];
        for(int i = 0; i < services.size(); i++){
            result[i] = services.get(i);
        }
        return result;
    }

    public void addService(Service service){
        if(!containsService(service)) {
            services.add(service.getName());
        }
    }

    public boolean deleteService(String service_name){
        boolean result = false;
        if(services.contains(service_name)){
            services.remove(service_name);
            result = true;
        }
        return result;
    }

    public boolean containsService(Service service){
        boolean result = false;
        if(services.contains(service.getName())){
            result = true;
        }
        return result;
    }

    public void setHeures(String h){
        heures = h;
    }

    public String getHeures(){
        return heures;
    }


}
