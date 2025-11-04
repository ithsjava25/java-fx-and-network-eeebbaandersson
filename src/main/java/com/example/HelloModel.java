package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final String hostName;
    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private final ObservableList<NtfyMessagesDto> messages = FXCollections.observableArrayList();

    public HelloModel(){
        //Läser in information från vår env
        Dotenv dotenv = Dotenv.load();
        hostName = Objects.requireNonNull(dotenv.get("HOST_NAME"));
        receiveMessage();
    }

    public ObservableList<NtfyMessagesDto> getMessages() {
        return messages;
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }


    public void sendMessage(String text){
        //Todo: Send Message using HTTPCLIENT

        //Kontrollera att texten vi får inte är tom
        if (text.trim().isEmpty()){
            return;
        }

        //Todo: se till att texten som skickas till server blir detsamma som användarinmatningen och inte Hello World!
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(text))
                .uri(URI.create(hostName + "/mytopic"))
                .build();

        try {
            //Todo: Handdle long blocking send request to not freeze the JavaFX thread
            //1. Use thread send message?
            //2. Use async?
            var response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.out.println("Error: sending");
        } catch (InterruptedException e) {
            System.out.println("Interrupted sending message");
        }
    }

    public void receiveMessage(){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(hostName + "/mytopic/json"))
                .build();

        http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> response.body()
                        .map(s ->
                                mapper.readValue(s, NtfyMessagesDto.class))
                        .filter(messages ->messages.event().equals("message"))
                        .peek(System.out::println)
                        .forEach(s ->
                                Platform.runLater(() -> messages.add(s))));

    }
}
