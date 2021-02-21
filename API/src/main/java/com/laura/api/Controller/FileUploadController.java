package com.laura.api.controller;

import com.laura.api.model.User;
import com.laura.api.service.UserService;
import com.laura.api.service.UtilsService;
import com.laura.api.storage.FileResponse;
import com.laura.api.storage.StorageService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private StorageService storageService;
    private UserService userService;
	private UtilsService utilsService;

    public FileUploadController(StorageService storageService, UserService userService, UtilsService utilsService) {
        this.storageService = storageService;
        this.userService = userService;
        this.utilsService = utilsService;
    }

    @GetMapping(value = "/users/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        User user = utilsService.getUser();

        String name = storageService.store(file, String.valueOf(user.getId()));
        
        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/users/")
                .path(name)
                .toUriString();
        
        user.setPicture(uri);
        userService.editUser(user);

        return  new FileResponse(name, uri, file.getContentType(), file.getSize());
    }
}
