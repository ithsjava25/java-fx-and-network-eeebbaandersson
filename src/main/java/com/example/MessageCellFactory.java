package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class MessageCellFactory extends ListCell<NtfyMessageDto> {

    private static final String USER_TOPIC = "user-topic";

    @Override
    protected void updateItem(NtfyMessageDto message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null){
            setText(null);
            setGraphic(null);
        } else {

            Label messageLabel = new Label(message.message());
            messageLabel.setWrapText(true);
            messageLabel.setMaxWidth(250);// Begränsar bubblans bredd

            HBox continer = new HBox();
            continer.getChildren().add(messageLabel);

            boolean isUser = USER_TOPIC.equals(message.topic());

            if (isUser) {
                messageLabel.getStyleClass().add("user-bubble");
                continer.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageLabel.getStyleClass().add("sender-bubble");
                continer.setAlignment(Pos.CENTER_LEFT);
            }

            setGraphic(continer);
        }
    }

}


