package com.example;

import java.util.function.Consumer;

public class NtfyConnectionSpy implements NtfyConnection {

    String message;

    @Override
    public boolean send(String messageText) {
        this.message = messageText;
        return true;
    }

    @Override
    public void receive(Consumer<NtfyMessageDto> messageHandler) {

    }
}
