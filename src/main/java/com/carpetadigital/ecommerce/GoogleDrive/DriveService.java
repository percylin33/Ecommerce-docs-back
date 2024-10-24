package com.carpetadigital.ecommerce.GoogleDrive;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Collections;

@Service
public class DriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "Google Drive API Java";
    private static final String SERVICE_ACCOUNT_KEY_PATH = getPathToGoodleCredentials();

    private static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "credencial.json");
        return filePath.toString();
    }

    public void permisos() {
        try {
            // El ID del archivo de Google Drive cuyos permisos deseas consultar
            String fileId = "1hOGR8prQJWESu1c2g6Sqv3_9BQf33SRp"; // Reemplaza con el ID real del archivo

            // Crear el servicio de Google Drive (debes tener implementado tu método de autenticación)
            Drive drive = createDriveService();

            // Obtener la lista de permisos del archivo
            PermissionList permissions = drive.permissions().list(fileId).execute();

            // Iterar y mostrar los permisos asociados
            for (Permission permission : permissions.getPermissions()) {
                System.out.println("ID del permiso: " + permission.getId());
                System.out.println("Tipo: " + permission.getType()); // Ejemplo: 'user', 'anyone'
                System.out.println("Rol: " + permission.getRole()); // Ejemplo: 'owner', 'reader', 'writer'
                System.out.println("Permisos especiales: " + permission.getAllowFileDiscovery()); // Si se permite descubrir el archivo en búsquedas
                System.out.println("=====================================");
            }

        } catch (GeneralSecurityException | IOException e) {
            // Manejo del error
            e.printStackTrace();
            System.out.println("Hubo un error al intentar acceder a Google Drive: " + e.getMessage());
        }
    }

    // recuperar archivo desde google drive
    public Res downloadFileFromDrive(String fileId) {
        Res res = new Res();
        try {
            Drive drive = createDriveService();

            // obtener el archivo de google drive por Id
            File fileMetadata = drive.files().get(fileId)
                    .setFields("id, name, mimeType, webViewLink, webContentLink")
                    .execute();

            Path downloadPath = Files.createTempFile("downloaded-", fileMetadata.getName());
            try (OutputStream outputStream = Files.newOutputStream(downloadPath)) {
                drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            }
            res.setStatus(200);
            res.setMessage("File successfully downloaded");
            res.setIdFile(fileId);
            res.setFileName(fileMetadata.getName());
            res.setMimeType(fileMetadata.getMimeType());
            res.setFilePath(downloadPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(500);
            res.setMessage("Failed to download file");
        }
        System.out.println(res);
        return res;
    }

    // METODO PARA GUARDAR UN ARCHIVO EN GOOGLE DRIVE
    public Res uploadFileToDrive(MultipartFile file) throws GeneralSecurityException, IOException {

        // se define una instancia de Res que es una clase para la respuesta
        Res res = new Res();
        try {
            // especificación de que carpeta se trata en google drive
            String folderId = "1Kw3PoiYLFdrgXx4n4DYBSs7pJgVzzeHd"; // el id que sale en la url de google drive dentro de la carpeta

            // se crea servicio de Google Drive y createDriveService es la función que se encarga de crear y autenticar un servicio para interactuar con la api. Drive se usa para subir el archivo
            Drive drive = createDriveService();

            // configuración de los metadatos del archivo
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File(); // crea un objeto que contiene los metadatos del archivos
            fileMetaData.setName(file.getOriginalFilename()); // establece el nombre del archivo a subir
            fileMetaData.setParents(Collections.singletonList(folderId)); // especifica el archivo que se subirá dentro de la carpeta cuyo Id fue definido en folderId

            // Guardar MultipartFile en un archivo temporal en el sistema
            Path tempFile = Files.createTempFile(null, file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            // se define el contenido del archivo
            FileContent mediaContent = new FileContent(file.getContentType(), tempFile.toFile()); // mediaContent representa el contenido del archivo (tipo=MIME Type, archivo=file)

            // subida del archivo a Google Drive
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent) // crea el archivo a subir
                    .setFields("id, webViewLink, webContentLink") // se define que se va a recuperar del archivo subido
                    .execute();

            // crear la url de la imagen o archivo
            String imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId(); // esta última parte representa el identificador único del archivo

            // crear un permiso que solo permita ver el archivo
            Permission viewPermission = new Permission();
            viewPermission.setType("anyone"); // Permitir que cualquier persona con el enlace lo vea
            viewPermission.setRole("reader"); // Solo lectura
            viewPermission.setAllowFileDiscovery(false); // No aparece en los resultados de búsqueda de Drive

            // Aplicar el permiso de solo visualización
            drive.permissions().create(uploadedFile.getId(), viewPermission).execute();

            // Restringir la copia, impresión y descarga
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setViewersCanCopyContent(false); // Restringir la copia, impresión y descarga
            drive.files().update(uploadedFile.getId(), fileMetadata).execute();

            Files.delete(tempFile); // elimina el archivo local una vez subido a Google Drive

            res.setStatus(200);
            res.setMessage("Image successfully uploaded to drive");
            res.setUrl(imageUrl);
            res.setIdFile(uploadedFile.getId());
            res.setWebViewLink(uploadedFile.getWebViewLink());
            res.setWebContentLink(uploadedFile.getWebContentLink());


        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            res.setStatus(500);
            res.setMessage(exception.getMessage());
        }
        return res;
    }

    // METODO PARA BORRAR UN ARCHIVO DE GOOGLE DRIVE
    public Res deleteFileDeDrive(String fileId) throws Exception {
        Res res = new Res();

        try {
            Drive drive = createDriveService();

            drive.files().delete(fileId).execute();

            res.setStatus(200);
            res.setMessage("Archivo borrado con éxito de Google Drive");
            res.setIdFile(fileId);
        } catch (Exception e) {
            res.setStatus(500);
            res.setMessage("Error al borrar el archivo de Google Drive");
            throw e;
        }
        return res;
    }

    // servicio para buscar un file de google drive
    public File buscarFileDrive(String fileId) throws GeneralSecurityException, IOException {
        Res res = new Res();

        try {
            Drive drive = createDriveService();

            // obtener el archivo desde Google Drive
            File file = drive.files().get(fileId)
                    .setFields("id, name, mimeType, md5Checksum")
                    .execute();
/*
            // Obtener el contenido del archivo como InputStream
            InputStream inputStream = drive.files().get(fileId).executeMediaAsInputStream();

            // Convertir el InputStream a un byte[]
            byte[] fileBytes = inputStream.readAllBytes();

            // Cofificar el archivo en base64
            String fileBase64 = Base64.getEncoder().encodeToString(fileBytes);

            System.out.println(file.getMd5Checksum() + "-------------------");

            res.setStatus(200);
            res.setMessage("búsqueda exitosa");
//            res.setIdFile(fileBase64);
            res.setFileName(file.getName());
            res.setMimeType(file.getMimeType());
            res.setHashFile(file.getMd5Checksum());
            res.setIdFile(file.getId());
            return res;

 */
            return file;
        } catch (Exception e) {
            res.setStatus(500);
            res.setMessage("Error al borrar el archivo de Google Drive");
            throw e;
        }
    }



    private Drive createDriveService() throws GeneralSecurityException, IOException {

        GoogleCredentials credential = GoogleCredentials
                .fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
