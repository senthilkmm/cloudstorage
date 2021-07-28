package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void uploadFile(MultipartFile multipartFile, Integer userId) throws IOException {
        String fileSize = "" + multipartFile.getSize();
        File file = new File(null, multipartFile.getOriginalFilename(),multipartFile.getContentType(), fileSize, userId, multipartFile.getBytes());

        this.fileMapper.uploadFile(file);
    }

    public List<File> getFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public File getFile(int id) {
        return fileMapper.getFile(id);
    }

    public void deleteFile(int id) {
        fileMapper.deleteFile(id);
    }
}
