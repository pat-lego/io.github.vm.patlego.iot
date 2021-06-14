package io.github.vm.patlego.iot.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class SimpleSecurity implements Security {


    private StandardPBEStringEncryptor enc;
    
    public SimpleSecurity() {
        enc = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig env = new EnvironmentStringPBEConfig();
        env.setAlgorithm("PBEWithMD5AndDES");
        env.setPassword("ENCRYPTION_PASSWORD");
        enc.setConfig(env);
    }
    
    @Override
    public String decrypt(String token) {
       return this.enc.decrypt(token);
    }

    @Override
    public String encrypt(String token) {
        return this.enc.encrypt(token);
    }
    
}
