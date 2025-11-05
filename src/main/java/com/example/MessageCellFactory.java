package com.example;

import javafx.scene.control.ListCell;

public class MessageCellFactory extends ListCell<NtfyMessageDto> {
}

//Addera logik för skapande av chattbubblor?

//Todo: Behåll men flytta metoden någon annanstans?
//    private void addMessage(String text, boolean isUser) {
//
//        HBox messageContainer = createMessageBubble(text, isUser);
//        messageArea.getChildren().add(messageContainer);
//
//    }

//Todo: Behåll men flytta metoden någon annanstans?
//    private HBox createMessageBubble(String text, boolean isUser) {
//        Label messageLabel = new Label(text);
//        messageLabel.setWrapText(true);
//        messageLabel.setMaxWidth(250);
//
//        HBox container = new HBox();
//        container.getChildren().add(messageLabel);
//
//        if (isUser) {
//            messageLabel.getStyleClass().add("user-bubble");
//            container.setAlignment(Pos.CENTER_RIGHT);
//        } else {
//            messageLabel.getStyleClass().add("sender-bubble");
//            container.setAlignment(Pos.CENTER_LEFT);
//        }
//        return container;
//    }