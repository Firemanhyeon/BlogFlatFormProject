package org.blog.blogflatformproject.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.blog.blogflatformproject.user.domain.User;

import java.time.LocalDateTime;

//방문사이트
@Entity
@Table(name = "views")
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Views {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long viewId;

    @Column(name="view_created")
    private LocalDateTime viewCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


}
