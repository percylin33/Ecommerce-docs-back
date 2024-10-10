package com.carpetadigital.ecommerce.driver.controller;

import com.carpetadigital.ecommerce.driver.service.GoogleDriveService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.io.InputStreamReader;


@RestController
public class AuthControllerDriver {

    @Autowired
    GoogleDriveService googleDriveService;

    // Definición de JSON_FACTORY
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String REDIRECT_URI = "http://localhost:8081/Callback";
    private static final String CLIENT_ID = "315197815687-n0sebbi0chn8mn96lq9h5e2cgbqvttrf.apps.googleusercontent.com"; // Reemplazar con tu CLIENT_ID

    // Método para iniciar el proceso de autorización
    @GetMapping("/api/v1/drive/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        String authorizationUrl = new GoogleAuthorizationCodeRequestUrl(CLIENT_ID, REDIRECT_URI, googleDriveService.getScopes())
                .setAccessType("offline")
                .build();
        response.sendRedirect(authorizationUrl);  // Redirige al usuario a la página de autorización de Google
    }

    // Método para manejar el callback
    @GetMapping("/api/v1/drive/Callback")
    public String callback(@RequestParam("code") String code) {
        try {
            // Cargar secretos del cliente
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                    JSON_FACTORY,
                    new InputStreamReader(getClass().getResourceAsStream(CREDENTIALS_FILE_PATH))
            );

            // Crear el flujo de autorización
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    new NetHttpTransport(), JSON_FACTORY, clientSecrets, googleDriveService.getScopes())
                    .setAccessType("offline")
                    .build();

            // Intercambiar el código por un token de acceso
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            Credential credential = flow.createAndStoreCredential(tokenResponse, "user");

            System.out.println("Access Token: " + credential.getAccessToken());

            return "Autenticación exitosa, puedes cerrar esta pestaña.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error en la autenticación: " + e.getMessage();
        }
    }
}