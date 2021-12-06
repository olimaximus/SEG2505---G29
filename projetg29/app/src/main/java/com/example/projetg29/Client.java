package com.example.projetg29;

public class Client extends Compte{

    public Client(String username, String password){
        super(username, password);
        setType("Client");
    }

    public Service demanderService(Service service, String employeName){
        String name = service.getName();
        name = name+"_E"+employeName+"_C"+this.getUsername();
        service.setName(name);
        service.assignTo(employeName, this.getUsername());
        return service;
    }
}
