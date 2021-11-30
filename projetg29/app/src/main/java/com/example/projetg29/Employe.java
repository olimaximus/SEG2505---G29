package com.example.projetg29;

import java.util.ArrayList;

public class Employe extends Compte{
    private ArrayList<String> services;

    public Employe(String username, String password){
        super(username, password);
        setType("Employé");
        services = new ArrayList<>();
    }

    public ArrayList<String> getServicesList() {
        return services;
    }

    public void addService(Service service){
        services.add(service.getName());
    }

    public boolean deleteService(String service_name){
        boolean result = false;
        if(services.contains(service_name)){
            services.remove(service_name);
            result = true;
        }
        return result;
    }
}
