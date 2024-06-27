package org.blog.blogflatformproject.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.blog.blogflatformproject.board.domain.Board;

import java.time.LocalDateTime;

//방문사이트
@Entity
@Table(name = "views")
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Views {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "view_id")
    private Long viewId;

    @Column(name="view_created")
    private LocalDateTime viewCreated;

    @OneToOne
    @JoinColumn(name = "user_id" , unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "board_id" ,unique = true)
    private Board board;


}
