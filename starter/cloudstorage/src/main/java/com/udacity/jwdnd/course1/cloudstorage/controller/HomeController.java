package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;
    private EncryptionService encryptionService;


    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService,
                          UserService userService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(@ModelAttribute("note") Note note, @ModelAttribute("credential") Credential credential,
                              Authentication auth, Model model) {
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        model.addAttribute("files", this.fileService.getFiles(userId));
        model.addAttribute("notes", this.noteService.getNotes(userId));
        model.addAttribute("credentials", this.credentialService.getCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication auth,
                             RedirectAttributes redirectAttributes) throws IOException {
        String actionError = null;
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);

        if (fileUpload.isEmpty()) {
            System.out.println("empty file");
            actionError = "Please select a file to upload.";
        } else if (fileService.getFile(fileUpload.getOriginalFilename(), userId) != null) {
            actionError = "The file already exists, cannot upload.";
        }

        if (actionError == null) {
            int rowsAdded = this.fileService.uploadFile(fileUpload, userId);
            if (rowsAdded < 1) {
                actionError = "There file upload failed. Please try again.";
            }
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }

        return "redirect:/result";
    }

    @PostMapping("/add-note")
    public String addNote(@ModelAttribute("note") Note note, Authentication auth,
                          RedirectAttributes redirectAttributes) {
        String actionError = null;
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        if (noteService.getNote(note.getNoteId()) != null) {
            if (this.noteService.updateNote(note) < 1) {
                actionError = "Update note failed. Please try again.";
            }
        } else {
            if (this.noteService.addNote(note, userId) < 1) {
                actionError = "Add note failed. Please try again.";
            }
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }
        return "redirect:/result";
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute("credential") Credential credential, Authentication auth,
                                RedirectAttributes redirectAttributes) {
        // TODO: encrypt/decrypt credentials
        String actionError = null;
        String username = auth.getName();
        Integer userId = this.userService.getUserId(username);
        if (credentialService.getCredential(credential.getCredentialId()) != null) {
            if (this.credentialService.updateCredential(credential) < 1) {
                actionError = "Update credential failed. Please try again.";
            }
        } else {
            if (this.credentialService.addCredential(credential, userId) < 1) {
                actionError = "Add credential failed. Please try again.";
            }
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }
        return "redirect:/result";
    }

    @GetMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String actionError = null;

        if (this.fileService.deleteFile(id) < 1) {
            actionError = "File delete failed. Please try again.";
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }

        return "redirect:/result";
    }

    @GetMapping("/note/delete/{id}")
    public String deleteNote(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String actionError = null;

        if (this.noteService.deleteNote(id) < 1) {
            actionError = "Note delete failed. Please try again.";
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }

        return "redirect:/result";
    }

    @GetMapping("/credential/delete/{id}")
    public String deleteCredential(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        String actionError = null;

        if (this.credentialService.deleteCredential(id) < 1) {
            actionError = "Credential delete failed. Please try again.";
        }

        if (actionError == null) {
            redirectAttributes.addFlashAttribute("actionSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("actionError", actionError);
        }

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
