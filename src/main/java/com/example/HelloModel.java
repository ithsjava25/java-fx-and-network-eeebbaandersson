package com.example;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model layer: encapsulates application data and business logic.
 */
public class HelloModel {

    private final NtfyConnection connection;
    private final ObservableList<NtfyMessageDto> messages = FXCollections.observableArrayList();
    private final StringProperty messageToSend = new SimpleStringProperty();

    private final String topicToUse;

    public HelloModel(NtfyConnection connection, String topic) {
        this.connection = connection;
        this.topicToUse = topic;
        receiveMessage();
    }

    public ObservableList<NtfyMessageDto> getMessages() {
        return messages;
    }

    public String getMessageToSend() {
        return messageToSend.get();
    }

    public StringProperty messageToSendProperty() {
        return messageToSend;
    }

    public void setMessageToSend(String messageToSend) {
        this.messageToSend.set(messageToSend);
    }

    /**
     * Returns a greeting based on the current Java and JavaFX versions.
     */
    public String getGreeting() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        return "Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".";
    }

    public void sendMessage(String messageText) {
        if (messageText.trim().isEmpty()){
            return;
        }

        NtfyMessageDto localMessage = new NtfyMessageDto(
                null, //id
                System.currentTimeMillis(), //time
                "message",// event
                "user-topic", //topic
                messageText //message
        );

        //Ger lokal visning av vårt skickade meddelande i listView
        messages.add(localMessage);

        //Skickar vidare meddelandet till Connection
        connection.send(messageText);
    }

    public void receiveMessage() {
        connection.receive(m -> {

            if (!this.topicToUse.equals(m.topic())){
                runOnFx(() -> messages.add(m));
                return;
            }
            //Filtrerar bort dubblet av eget meddelanden, genom jämförelse av message/topic
            boolean alreadyExistsLocally = messages.stream()
                    .anyMatch(localM -> localM.message().equals(m.message()) &&
                    localM.topic().equals("user-topic"));

            if (!alreadyExistsLocally) {
                runOnFx(() -> messages.add(m));
            }

        });

    }

    private static void runOnFx(Runnable task) {
        try {
            if (Platform.isFxApplicationThread()) task.run();
            else Platform.runLater(task);
        } catch (IllegalStateException notInitialized) {
            // JavaFX toolkit not initialized (e.g., unit tests): run inline
            task.run();
        }
    }

}
