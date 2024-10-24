package com.carpetadigital.ecommerce.GoogleDrive;

import lombok.Data;

@Data
public class Res {
    private int status;
    private String message;
    private String idFile;
    private String url;
    private String webViewLink;
    private String webContentLink;
    private String hashFile;

    private String fileName;
    private String mimeType;
    private String filePath;
}
