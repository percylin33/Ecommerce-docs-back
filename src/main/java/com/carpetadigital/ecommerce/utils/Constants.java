package com.carpetadigital.ecommerce.utils;

public class Constants {
    public static final String S3_URL = "https://s3.us-east-2.amazonaws.com/backoffice.documents/";
    public static final String API_BASE_PATH = "/api/v1/";

    public static final class ValidationMessages {
        public static final String NOT_FOUND = "File not found ";
        public static final String ERROR_DELETE = "Error deleting file, ";
        public static final String ERROR_DOWN = "Error downloading file from S3: ";

    }
}
