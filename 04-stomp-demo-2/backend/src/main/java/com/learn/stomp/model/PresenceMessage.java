package com.learn.stomp.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PresenceMessage {
    private Set<String> onlineUsers;
}
