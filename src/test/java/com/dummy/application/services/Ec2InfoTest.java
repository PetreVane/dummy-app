package com.dummy.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class Ec2InfoTest {

    private HttpClient mockHttpClient;
    private HttpResponse<String> mockTokenResponse;
    private HttpResponse<String> mockIpResponse;
    private HttpResponse<String> mockAzResponse;

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockTokenResponse = Mockito.mock(HttpResponse.class);
        mockIpResponse = Mockito.mock(HttpResponse.class);
        mockAzResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockTokenResponse.body()).thenReturn("fakeToken");
        Mockito.when(mockIpResponse.body()).thenReturn("192.0.2.1");
        Mockito.when(mockAzResponse.body()).thenReturn("us-west-2b");

        Mockito.when(mockHttpClient.send(any(HttpRequest.class), Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockTokenResponse, mockIpResponse, mockAzResponse);
    }

    @Test
    void getPublicInfoTest() throws IOException, InterruptedException {
        try (MockedStatic<HttpClient> mockedStatic = Mockito.mockStatic(HttpClient.class)) {
            mockedStatic.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);

            Map<String, String> ec2Info = Ec2Info.getPublicInfo();

            assertEquals("192.0.2.1", ec2Info.get("ip"));
            assertEquals("us-west-2b", ec2Info.get("az"));
        }
    }
}
