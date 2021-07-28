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

    public int addCredential(Credential credential, Integer userId) {
        credential.setUserId(userId);
        return credentialMapper.addCredential(credential);
    }

    public int deleteCredential(int id) {
        return credentialMapper.deleteCredential(id);
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public int updateCredential(Credential crdential) {
        return credentialMapper.updateCredential(crdential);
    }
}
