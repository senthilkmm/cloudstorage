package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;


    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService,
                          UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(@ModelAttribute("note") Note note, @ModelAttribute("credential") Credential credential,
                              Authentication auth, Model model) {
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        model.addAttribute("files", this.fileService.getFiles(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));
        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth)
            throws IOException {
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        this.fileService.uploadFile(fileUpload, userId);
        return "redirect:/result";
    }

    @PostMapping("/add-note")
    public String addNode(@ModelAttribute("note") Note note, Authentication auth) {
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        if (noteService.getNote(note.getNoteId()) != null) {
            this.noteService.updateNote(note);
        } else {
            this.noteService.addNote(note, userId);
        }
        return "redirect:/result";
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute("credential") Credential credential, Authentication auth) {
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        if (credentialService.getCredential(credential.getCredentialId()) != null) {
            this.credentialService.updateCredential(credential);
        } else {
            this.credentialService.addCredential(credential, userId);
        }
        return "redirect:/result";
    }

    @GetMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable("id") int id) {
        this.fileService.deleteFile(id);
        return "redirect:/result";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id") int id) {
        this.noteService.deleteNote(id);
        return "redirect:/result";
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable("id") int id) {
        this.credentialService.deleteCredential(id);
        return "redirect:/result";
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") int id) {
        File file = fileService.getFile(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");

        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(Long.parseLong(file.getFileSize()))
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(resource);
    }
}
