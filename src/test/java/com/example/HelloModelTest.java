package com.example;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;


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
        var model = new HelloModel(spy, "mytopic");
        model.setMessageToSend(" ");
        //Act  When
        model.sendMessage("Hello World");
        //Assert   Then
        assertThat(spy.message).isEqualTo("Hello World");

    }

    @Test
    void sendMessageToFakeServer(WireMockRuntimeInfo wmRuntimeInfo){
        //Arrange
        var con = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());
        var model = new HelloModel(con, "mytopic");
        model.setMessageToSend("");
        stubFor(post("/mytopic").willReturn(ok()));

        //Act
        model.sendMessage("Hello World");

        //Ser till att det Asynkrona-anropet hinner klart innan verify call
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Verify call made to server
        verify(postRequestedFor(urlEqualTo("/mytopic"))
                .withRequestBody(matching("Hello World")));
    }


    @Test
    void receiveMessageFromFakeServer(WireMockRuntimeInfo wmRuntimeInfo) {

        //Simulerar en ström av två JSON-objekt
        String fakeServerResponse = """
                {"event":"keepalive", "id":"1","time": 170000, "topic":"mytopic","message":"Detta meddedelande ska filtreras bort"}
                {"event":"message", "id":"2","time": 150000, "topic":"mytopic", "message":"Hej!"}
                
                """;

        //Simulerar servern
        stubFor(get(urlPathEqualTo("/mytopic/json"))
                .willReturn(aResponse().withStatus(200)
                        .withBody(fakeServerResponse)));

        var con = new NtfyConnectionImpl("http://localhost:" + wmRuntimeInfo.getHttpPort());

        //Skapar en falsk mottagare
        Consumer<NtfyMessageDto> mockMessageHandler = Mockito.mock(Consumer.class);

        con.receive(mockMessageHandler);

        //Initierar funktion från Mockito som kan fånga objekten som skickas till MessageHandler
        ArgumentCaptor<NtfyMessageDto> captor = ArgumentCaptor.forClass(NtfyMessageDto.class);

        //Kontrollerar att det asynkrona flödet slutförts korrekt genom att pausa testtråden
        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {

                    //Verifierar att mockMesageHandler anropades 1 gång och objektet fångas
                    Mockito.verify(mockMessageHandler,Mockito.times(1))
                            .accept(captor.capture());
                });

        //Kontrollera innehållet på det uppfångade meddelandet
        assertThat(captor.getValue().message()).isEqualTo("Hej!");
        //Säkerställer att det fångade meddedelandet är av rätt typ
        assertThat(captor.getValue().event()).isEqualTo("message");
    }


    @Test
    void addMessageToObservableList(){
        var spy = new NtfyConnectionSpy();
        var model = new HelloModel(spy,"mytopic");

       var exampleText = new NtfyMessageDto("id1",3000,"message","user-topic","Hej!");
       spy.simulateIncomingMessage(exampleText);

       //Assert
       assertThat(model.getMessages()).extracting(NtfyMessageDto::message).contains("Hej!");

    }
}





