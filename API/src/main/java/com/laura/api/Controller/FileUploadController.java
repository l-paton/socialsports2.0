package com.laura.api.Controller;

import java.io.IOException;

import com.laura.api.Service.UserService;
import com.laura.api.model.User;
import com.laura.api.storage.FileResponse;
import com.laura.api.storage.StorageService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private StorageService storageService;
    private UserService userService;

    public FileUploadController(StorageService storageService, UserService userService) {
        this.storageService = storageService;
        this.userService = userService;
    }

    @GetMapping(value = "/users/{filename:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public byte[] downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        try {
            byte[] b = resource.getInputStream().readAllBytes();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        
    }

    @PostMapping("/upload")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        try{

            User user = getUser();

            String name = storageService.store(file);

            String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/users/")
                    .path(name)
                    .toUriString();
            
            user.setPicture(uri);
            userService.editUser(user);

            return  new FileResponse(name, uri, file.getContentType(), file.getSize());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userService.getUser(auth.getName());
	}
}
