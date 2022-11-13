package com.university.backend.dto;

import com.university.backend.model.ChatMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {

    private EventType type;
    private ChatMessage message;

}
