package org.blog.blogflatformproject.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(name = "reply_content")
    private String replyContent;

    @Column(name = "reply_created")
    private LocalDateTime replyCreated;

    @Column(name = "pre_reply_id")
    private Long preReplyId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
