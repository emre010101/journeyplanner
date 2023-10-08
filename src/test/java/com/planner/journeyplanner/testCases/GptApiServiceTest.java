package com.planner.journeyplanner.testCases;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import com.planner.journeyplanner.exception.BadRequestException;
import com.planner.journeyplanner.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
public class GptApiServiceTest {

    @Mock
    private HttpService httpService;

    @InjectMocks
    private GptApiService gptApiService;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(gptApiService, "apiKeyTemp", "your-api-key");
    }

    @Test
    public void sendRequest_validInput_returnResponse() throws IOException, BadRequestException, UnauthorizedException {
        // Arrange
        String text = "Test text";
        HttpURLConnection con = mock(HttpURLConnection.class);
        when(httpService.createConnection(anyString())).thenReturn(con);
        when(con.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpService.readResponse(con)).thenReturn("Test response");

        // Act
        String response = gptApiService.sendRequest(text);

        // Assert
        assertEquals("Test response", response);
    }

    @Test
    public void sendRequest_badRequest_throwBadRequestException() throws IOException {
        // Arrange
        String text = "Test text";
        HttpURLConnection con = mock(HttpURLConnection.class);
        when(httpService.createConnection(anyString())).thenReturn(con);
        when(con.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);

        InputStream errorStream = new ByteArrayInputStream("Error message".getBytes());
        when(con.getErrorStream()).thenReturn(errorStream);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> gptApiService.sendRequest(text));
    }

}
