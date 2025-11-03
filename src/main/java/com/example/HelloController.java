package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel();
    public ListView<NtfyMessagesDto> messagesView;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private VBox messageArea;

    @FXML
    private TextField inputField;

    @FXML
    private Label messageLabel;


    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText(model.getGreeting());
        }
       // messagesView.setItems(model.getMessages());
    }

    //Funktion för att skicka meddelande när användaren trycker enter
    public void sendMessage(ActionEvent actionEvent) {
        String messageText = inputField.getText().trim();

        if(!messageText.isEmpty()) {
            model.sendMessage(messageText);
            inputField.clear();


            //Tillfälligt svar från "bot"
//            addMessage("Jag fick ditt meddelande: " + messageText, false);
        }
    }


    //Todo: Behåll men flytta metoden någon annanstans?
    private void addMessage(String text, boolean isUser) {
        HBox messageContainer = createMessageBubble(text, isUser);
        messageArea.getChildren().add(messageContainer);

    }

    //Todo: Behåll men flytta metoden någon annanstans?
    private HBox createMessageBubble(String text, boolean isUser) {
        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(250);

        HBox container = new HBox();
        container.getChildren().add(messageLabel);

        if (isUser) {
            messageLabel.getStyleClass().add("user-bubble");
            container.setAlignment(Pos.CENTER_RIGHT);
        } else {
            messageLabel.getStyleClass().add("sender-bubble");
            container.setAlignment(Pos.CENTER_LEFT);
        }
        return container;
    }
}
