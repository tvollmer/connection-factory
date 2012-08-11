package com.voltsolutions.security;

import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


public class CryptoProvider {

    private static final SecureRandom DEFAULT_RANDOM = new SecureRandom();
    
    private static final String DEFAULT_SALT = "A long, but constant phrase that will be used each time as the salt.";
    private static final int DEFAULT_ITERATIONS = 1007;
    private static final int DEFAULT_KEYLENGTH = 1024;
    private static final String DEFAULT_CIPHER_KEYGENERATOR = "AES/CTR/NOPADDING";
    private static final String DEFAULT_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";
    private static final String DEFAULT_PASSPHRASE = "The quick brown fox jumps over the lazy dog";

    private Cipher eCipher = null;
    private Cipher dCipher = null;
    
    private String salt = DEFAULT_SALT;
    private int iterations = DEFAULT_ITERATIONS;
    private int keyLength = DEFAULT_KEYLENGTH;
    private String cipherKeygenerator = DEFAULT_CIPHER_KEYGENERATOR;
    private String algorithm = DEFAULT_ALGORITHM;
    private String passphrase = DEFAULT_PASSPHRASE;
    
    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
    
    public CryptoProvider() {
        init();
    }
    
    public CryptoProvider(String algorithm, String cipherKeygenerator, String salt, String passphrase) {
        this.algorithm = algorithm;
        this.cipherKeygenerator = cipherKeygenerator;
        this.salt = salt;
        this.passphrase = passphrase;
        init();
    }
    
    public CryptoProvider(String algorithm, String cipherKeygenerator, String salt, String passphrase, int iterations, int keyLength) {
        this.algorithm = algorithm;
        this.cipherKeygenerator = cipherKeygenerator;
        this.salt = salt;
        this.passphrase = passphrase;
        this.iterations = iterations;
        this.keyLength = keyLength;
        init();
    }
    
    protected void init(){
        try {
            SecretKey key = generateKey();
            
            eCipher = Cipher.getInstance(cipherKeygenerator, "BC");
            eCipher.init(Cipher.ENCRYPT_MODE, key, generateIV(eCipher), DEFAULT_RANDOM);
            
            dCipher = Cipher.getInstance(cipherKeygenerator, "BC");
            dCipher.init(Cipher.DECRYPT_MODE, key, generateIV(dCipher), DEFAULT_RANDOM);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to initialize crypto.", ex);
        }
    }
    public String encrypt(String plaintext) {
        try {
            byte[] encrypted = eCipher.doFinal(plaintext.getBytes());
            String encoded = new String(Base64.encode(encrypted));
            return encoded;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to encrypt string.", ex);
        }
    }

    public String decrypt(String encodedText) {
        try {
            byte[] encrypted = Base64.decode(encodedText);
            return new String(dCipher.doFinal(encrypted));
        } catch (Exception ex) {
            throw new RuntimeException("Unable to decrypt string.", ex);
        }
    }

    private SecretKey generateKey() throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(), iterations, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm, "BC");
        return keyFactory.generateSecret(keySpec);
    }

    private IvParameterSpec generateIV(Cipher cipher) throws Exception {
        byte [] ivBytes = new byte[cipher.getBlockSize()];
        DEFAULT_RANDOM.nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }
}
