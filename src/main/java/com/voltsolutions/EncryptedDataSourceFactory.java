package com.voltsolutions;

import java.util.*;
import javax.naming.*;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.voltsolutions.security.CryptoProvider;

public class EncryptedDataSourceFactory 
    extends BasicDataSourceFactory {

    private CryptoProvider cryptoProvider = new CryptoProvider();
    
    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        if (obj instanceof Reference) {
            setUsername((Reference) obj);
            setPassword((Reference) obj);
        }
        return super.getObjectInstance(obj, name, nameCtx, environment);
    }

    private void setUsername(Reference ref) throws Exception {
        findDecryptAndReplace("username", ref);
    }

    private void setPassword(Reference ref) throws Exception {
        findDecryptAndReplace("password", ref);
    }

    private void findDecryptAndReplace(String refType, Reference ref) throws Exception {
        int idx = find(refType, ref);
        String decrypted = decrypt(idx, ref);
        replace(idx, refType, decrypted, ref);
    }

    private void replace(int idx, String refType, String newValue, Reference ref) throws Exception {
        ref.remove(idx);
        ref.add(idx, new StringRefAddr(refType, newValue));
    }

    private String decrypt(int idx, Reference ref) throws Exception {
        return cryptoProvider.decrypt(ref.get(idx).getContent().toString());
    }

    private int find(String addrType, Reference ref) throws Exception {
        Enumeration enu = ref.getAll();
        for (int i = 0; enu.hasMoreElements(); i++) {
            RefAddr addr = (RefAddr) enu.nextElement();
            if (addr.getType().compareTo(addrType) == 0)
                return i;
        }

        throw new Exception("The \"" + addrType
                + "\" name/value pair was not found"
                + " in the Reference object.  The reference Object is" + " "
                + ref.toString());
    }
}
