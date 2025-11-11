package com.example;

import java.util.function.Consumer;

public class NtfyConnectionSpy implements NtfyConnection {
    String message;
    Consumer<NtfyMessageDto> messageHandler;


    @Override
    public boolean send(String messageText) {
        this.message = messageText;
        return true;
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> messageHandler) {
        this.messageHandler = messageHandler;

    }

    public void simulateIncomingMessage(NtfyMessageDto message) {
        if(messageHandler!=null) {
            messageHandler.accept(message);
        }
    }
}
