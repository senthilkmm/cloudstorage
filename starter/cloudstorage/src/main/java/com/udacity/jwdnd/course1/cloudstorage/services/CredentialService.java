package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public int addCredential(Credential credential, Integer userId) {
        credential.setUserId(userId);
        String encodedKey = getEncodedKey();
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
        credential.setKey(encodedKey);
        return credentialMapper.addCredential(credential);
    }

    private String getEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public int deleteCredential(int id) {
        return credentialMapper.deleteCredential(id);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public int updateCredential(Credential credential) {
        Credential credentialInDB = credentialMapper.getCredential(credential.getCredentialId());
        String encodedKey = credentialInDB.getKey();
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
        credential.setKey(encodedKey);
        return credentialMapper.updateCredential(credential);
    }
}
