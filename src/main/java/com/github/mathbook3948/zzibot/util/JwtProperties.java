package com.github.mathbook3948.zzibot.util;

import io.jsonwebtoken.io.Encoders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class JwtProperties {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtProperties.class);

    public static final String SECRET_KEY;
    public static final Long EXPIRATION_TIME;
    public static final Long REFRESH_EXPIRATION_TIME;

    private JwtProperties() {}

    static {
        Properties properties = new Properties();

        String propertiesName = "application-"+ System.getenv("SPRING_PROFILES_ACTIVE") +".properties";

        try (InputStream inputStream = JwtProperties.class.getClassLoader().getResourceAsStream(propertiesName)) {
            if (inputStream == null) {
                throw new IOException("Resource 'application-{env}.properties' not found in classpath.");
            }
            properties.load(inputStream);

            SECRET_KEY = Encoders.BASE64.encode(properties.getProperty("jwt.secret.key").getBytes());

            logger.info("jwt.expiration.time: {}", properties.getProperty("jwt.expiration.time"));

            EXPIRATION_TIME = Long.parseLong(properties.getProperty("jwt.expiration.time"))*60*1000;
            REFRESH_EXPIRATION_TIME = Long.parseLong(properties.getProperty("jwt.refresh.expiration.time"))*60*1000;

            logger.info("JwtProperties loaded successfully from resources: {}, {}", SECRET_KEY, EXPIRATION_TIME);
        } catch (IOException | NumberFormatException e) {
            logger.info("Failed to load JwtProperties: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
}
