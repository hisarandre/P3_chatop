package com.chatop.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtils {
    private static String baseUrl;

    @Value("${app.base-url}")
    public void setBaseUrl(String baseUrl) {
        UrlUtils.baseUrl = baseUrl;
    }

    public static String buildFileUrl(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            return null;
        }
        return baseUrl + relativePath;
    }
}
