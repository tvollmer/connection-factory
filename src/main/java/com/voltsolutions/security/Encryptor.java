package com.voltsolutions.security;

import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Encryptor {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        CryptoProvider crypto = new CryptoProvider();
        
//        for (Provider provider : Security.getProviders()){
//            System.out.println(provider);
//            for (Enumeration en = provider.keys(); en.hasMoreElements();){
//                System.out.println("\t" + en.nextElement());
//            }
//        }
        
        String plaintext = args[0];
        System.out.println(crypto.encrypt(plaintext));
        
    }

}
