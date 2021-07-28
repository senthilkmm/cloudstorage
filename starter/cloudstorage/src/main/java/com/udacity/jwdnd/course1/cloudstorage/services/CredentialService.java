package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public void addCredential(Credential credential, Integer userId) {
        credential.setUserId(userId);
        credentialMapper.addCredential(credential);
    }

    public void deleteCredential(int id) {
        credentialMapper.deleteCredential(id);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public void updateCredential(Credential crdential) {
        credentialMapper.updateCredential(crdential);
    }
}
