package com.carpetadigital.ecommerce.GoogleDrive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/v1/document")
public class Controller {

    @Autowired
    private DriveService driveService;

    @PostMapping("/uploadToGoogleDrive")
    public Object handleFileUpload(@RequestParam("image") MultipartFile file) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return "file is empty";
        }
        Res res = driveService.uploadFileToDrive(file);
        return res;
    }
    @GetMapping("/permisos")
    public void permisos() {
        driveService.permisos();
    }

    @DeleteMapping("/delete/{fileId}")
    public Object deleteFile(@PathVariable String fileId) {
        try {
            return driveService.deleteFileDeDrive(fileId);
        } catch (Exception e) {
            Res errorRes = new Res();
            errorRes.setStatus(500);
            errorRes.setMessage(e.getMessage());
            return errorRes;
        }
    }

    @GetMapping("/uploadToGoogleDrive/{idFile}")
    public Object GoogleDriveController(@PathVariable String idFile) {
        return  driveService.downloadFileFromDrive(idFile);
    }
}
