package com.example.projetg29;

import org.junit.Test;


import static org.junit.Assert.*;

import java.util.ArrayList;


public class Test1 {

    @Test
    public void testAddService(){
        Employe e = new Employe("Test","123");
        String[] servicesInitials = {"Permis-de-conduire", "Carte-sante", "Piece-d'identite"};
        e.addService(new Service("Permis-de-conduire"));
        e.addService(new Service("Carte-sante"));
        e.addService(new Service("Piece-d'identite"));
        ArrayList<String> list = e.getServicesList();
        String[] servicesFinals = new String[3];
        for(int i = 0; i < 3; i++){
            servicesFinals[i] = list.remove(0);
        }
        assertArrayEquals(servicesInitials, servicesFinals);
    }
}
