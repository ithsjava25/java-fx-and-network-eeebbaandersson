package com.example;

import java.util.function.Consumer;

public interface NtfyConnection {

    public boolean send(String messageText);

    public void receive(Consumer<NtfyMessageDto> messageHandler);
}