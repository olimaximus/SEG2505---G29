package com.example.projetg29;

import org.junit.Test;


import static org.junit.Assert.*;

import java.util.ArrayList;


public class Test1 {

    @Test
    public void testTypeAdministrateur(){
        Compte compte = new Administrateur("Test", "123");
        assertEquals("Administrateur", compte.getType());
    }

    @Test
    public void testTypeEmploye(){
        Compte compte = new Employe("Test", "123");
        assertEquals("Employé", compte.getType());
    }

    @Test
    public void testTypeClient(){
        Compte compte = new Client("Test", "123");
        assertEquals("Client", compte.getType());
    }

    @Test
    public void testAddService() {
        Employe e = new Employe("Test", "123");
        String[] servicesInitials = {"Permis-de-conduire", "Carte-sante", "Piece-d'identite"};
        e.addService(new Service("Permis-de-conduire"));
        e.addService(new Service("Carte-sante"));
        e.addService(new Service("Piece-d'identite"));
        ArrayList<String> list = e.getServicesList();
        String[] servicesFinals = new String[3];
        for (int i = 0; i < 3; i++) {
            servicesFinals[i] = list.remove(0);
        }
        assertArrayEquals(servicesInitials, servicesFinals);
    }

    @Test
    public void testAddFormulaire() {
        Service service = new Service("Test");
        service.setInfo("Prénom", "Maxime");
        assertEquals("Maxime", service.getInfo("Prénom"));
    }

    @Test
    public void testDeleteFormulaireTrue(){
        Service service = new Service("Test");
        service.setInfo("Prénom", "Maxime");
        assertTrue(service.deleteInfo("Prénom"));
    }

    @Test
    public void testDeleteFormulaireFalse(){
        Service service = new Service("Test");
        service.setInfo("Prénom", "Maxime");
        assertFalse(service.deleteInfo("Nom"));
    }

    // Livrable 4

    @Test
    public void testSetHeure(){
        Employe employe = new Employe("test", "123");
        employe.setHeures("6h-18h");
        assertEquals("6h-18h", employe.getHeures());
    }

    @Test
    public void testAssignServiceName(){
        Service service1 = new Service("Test");
        Employe employe = new Employe("Bob", "123");
        Client client = new Client("Steve", "123");
        Service service2 = client.demanderService(service1, employe.getUsername());
        assertEquals("Test_EBob_CSteve", service2.getName());
    }

    @Test
    public void testAssignServiceClient(){
        Service service1 = new Service("Test");
        Employe employe = new Employe("Bob", "123");
        Client client = new Client("Steve", "123");
        Service service2 = client.demanderService(service1, employe.getUsername());
        assertEquals("Steve", service2.getClient());
    }

    @Test
    public void testAssignServiceEmploye(){
        Service service1 = new Service("Test");
        Employe employe = new Employe("Bob", "123");
        Client client = new Client("Steve", "123");
        Service service2 = client.demanderService(service1, employe.getUsername());
        assertEquals("Bob", service2.getEmploye());
    }

    @Test
    public void testEmployeContainsService(){
        Service service = new Service("Test");
        Employe employe = new Employe("Bob", "123");
        employe.addService(service);
        assertTrue(employe.containsService(service));
    }

}
