package com.voltsolutions;

import static org.junit.Assert.*;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.*;

import com.voltsolutions.security.CryptoProvider;

public class TestSalt {

    private CryptoProvider crypto = new CryptoProvider();
    
    private String plaintext = "hello world";
    private String encrypted = "V74Csn3chR63DQ4=";
    
//    @BeforeClass
//    public static void configureProvider(){
//        Security.insertProviderAt(new BouncyCastleProvider(), 1);
//    }
    
    @Test
    public void shouldRoundTripValue() {
        String ciphertext = crypto.encrypt(plaintext);
        String recoveredPlaintext = crypto.decrypt(ciphertext);
        assertEquals(plaintext, recoveredPlaintext);
    }
    
    @Test
    public void shouldProvideBase64EncodedBytes() {
        assertEquals(encrypted, crypto.encrypt(plaintext));
    }
    
    @Test
    public void shouldDecodeBase64EncodedBytesAndDecrypt() {
        assertEquals(plaintext, crypto.decrypt(encrypted));
    }
}
