package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@Nested
@WireMockTest
class HelloModelTest {

    @BeforeAll
    static void initJavaFX() {

        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {

        }
    }

    @Test
    @DisplayName("Given a valid message argument, when sendMessage is called, then NtfyConnection´s sendmethod should be called")
    void sendMessage_GivenValidArgument_shouldCallConnectionWithArgument() {
        //Arrange  Given
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);
        model.setMessageToSend(" ");
        //Act  When
        model.sendMessage("Hello World");
        //Assert   Then
        assertThat(spy.message).isEqualTo("Hello World");
    }

    @Test
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo) throws InterruptedException {
        var con = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);
        model.setMessageToSend("");
        stubFor(post("/mytopic").willReturn(ok()));

        model.sendMessage("Hello World");

        //Ser till att det Asynkrona-anropet hinner klart innan verify call
        Thread.sleep(100);

        //Verify call made to server
        verify(postRequestedFor(urlEqualTo("/mytopic"))
                .withRequestBody(matching("Hello World")));
    }

    @Test
    void receciveMessageFromFakeServer(WireMockRuntimeInfo wmRuntimeInfo) {
        var con = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con);



    }

    @Test
    void addMessageToObservableList(){
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy);

       var exampleText = new NtfyMessageDto("id1",3000,"message","user-topic","Hej!");
       spy.simulateIncomingMessage(exampleText);

       //Assert that
       assertThat(model.getMessages()).extracting(NtfyMessageDto::message).contains("Hej!");

    }

    //Test för att kontrollera om meddelanden skickas till Nfty-servern

    //Test för att kontrollera att meddelanden tas emot från Ntfy-servern

}





