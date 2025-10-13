package com.klnsdr.axon.shs.mail;

import com.klnsdr.axon.mail.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ShsMailRendererTest {

    private ShsMailRenderer renderer;
    private Mail originalMail;

    @BeforeEach
    void setUp() {
        renderer = new ShsMailRenderer();
        originalMail = new Mail();
        originalMail.setFrom("sender@example.com");
        originalMail.setTo("recipient@example.com");
        originalMail.setSubject("Test Subject");
        originalMail.setBody("Hello <<name>>, your order <<orderId>> is confirmed.");
    }

    @Test
    void shouldReplacePlaceholdersWithProvidedData() {
        Map<String, String> data = Map.of(
                "name", "Alice",
                "orderId", "12345"
        );

        Mail result = renderer.renderMail(originalMail, data);

        assertEquals("Hello Alice, your order 12345 is confirmed.", result.getBody());
        assertEquals(originalMail.getFrom(), result.getFrom());
        assertEquals(originalMail.getTo(), result.getTo());
        assertEquals(originalMail.getSubject(), result.getSubject());
    }

    @Test
    void shouldLeaveUnmatchedPlaceholdersIntact() {
        Map<String, String> data = Map.of("name", "Bob");

        Mail result = renderer.renderMail(originalMail, data);

        assertEquals("Hello Bob, your order <<orderId>> is confirmed.", result.getBody());
    }

    @Test
    void shouldReturnCopyOfOriginalMail() {
        Map<String, String> data = Map.of("name", "Charlie", "orderId", "9876");

        Mail result = renderer.renderMail(originalMail, data);

        assertNotSame(originalMail, result);

        assertEquals("Hello <<name>>, your order <<orderId>> is confirmed.", originalMail.getBody());
    }

    @Test
    void shouldHandleEmptyDataMapGracefully() {
        Map<String, String> data = Map.of();

        Mail result = renderer.renderMail(originalMail, data);

        assertEquals(originalMail.getBody(), result.getBody());
    }

    @Test
    void shouldHandleBodyWithoutPlaceholders() {
        originalMail.setBody("No placeholders here.");
        Map<String, String> data = Map.of("name", "John");

        Mail result = renderer.renderMail(originalMail, data);

        assertEquals("No placeholders here.", result.getBody());
    }

    @Test
    void shouldHandleEmptyBody() {
        originalMail.setBody("");
        Map<String, String> data = Map.of("key", "value");

        Mail result = renderer.renderMail(originalMail, data);

        assertEquals("", result.getBody());
    }
}
