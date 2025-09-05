package org.kickmyb.serveur.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class ConfigFirebase {

    @Value("${firebase.config.path:}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            if (firebaseConfigPath.isEmpty()) {
                // Utilise les credentials par défaut si aucun chemin n'est spécifié
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } else {
                FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath);

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'initialisation de Firebase: " + e.getMessage(), e);
        }
    }
}
