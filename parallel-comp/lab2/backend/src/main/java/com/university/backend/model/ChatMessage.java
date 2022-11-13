package com.university.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private String recipient;

    @Override
    public String toString() {
        String result = "Message from " + sender;
        if (recipient != null) {
            result += " to " + recipient;
        }
        return result;
    }
}
