package org.example.prm392_groupprojectbe.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends AbstractEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Account receiver;

    private String message;
}
