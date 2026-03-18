package com.learn.stomp.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatMessage {

    private String type;

    private String sender;

    private String recipient;

    private String content;

    private Long timestamp;
}
