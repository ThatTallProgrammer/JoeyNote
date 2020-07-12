package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public enum TextEditorManager {

    INSTANCE;

    public String getStringFromEditor(String title) {

        Path notePath = null;

        String body = "";
        try {

            notePath = Files.createTempFile(title + " - ", ".txt");
            ProcessBuilder processBuilder = new ProcessBuilder("notepad", notePath.toString()); // TODO: lookup default text editor
            Process process = processBuilder.start();

            System.out.println("Waiting for file to close...");
            while(process.isAlive()) continue;

            body = new String(Files.readAllBytes(notePath));

            Files.delete(notePath);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return body;

    }

}
