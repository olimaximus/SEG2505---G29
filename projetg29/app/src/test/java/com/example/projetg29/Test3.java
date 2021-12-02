package com.example.projetg29;

import org.junit.Test;


import static org.junit.Assert.*;

import java.util.ArrayList;


public class Test2 {

    @Test
    public void getTypeTest(){
    Compte compte = new Client("test","123");
    Assert.AreEqual(compte.GetType(), typeof(compte));
    }
}
