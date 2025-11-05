package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

/**
 * Controller layer: mediates between the view (FXML) and the model.
 */
public class HelloController {

    private final HelloModel model = new HelloModel(new NtfyConnectionImpl());
    public ListView<NtfyMessageDto> messageView;


    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField inputField;

    @FXML
    private Label messageLabel;


    @FXML
    private void initialize() {
        if (messageLabel != null) {
            messageLabel.setText(model.getGreeting());
        }
        messageView.setItems(model.getMessages());

    }


    //Funktion för att skicka meddelande när användaren trycker enter
    public void sendMessage(ActionEvent actionEvent) {
        String messageText = inputField.getText().trim();
        if (!messageText.isEmpty()) {
            model.sendMessage(messageText);
            inputField.clear();
        }
    }

}
