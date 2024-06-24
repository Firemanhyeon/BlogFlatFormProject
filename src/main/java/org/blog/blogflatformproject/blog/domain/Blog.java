package org.blog.blogflatformproject.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.user.domain.User;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "blog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    @Column(name = "blog_name")
    private String blogName;

    @Column(name = "blog_description")
    private String blogDescription;

    @Column(name = "blog_registration_date")
    private LocalDate blogRegistrationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "blog" ,cascade = CascadeType.ALL)
    private Set<Board> board;

    @OneToMany(mappedBy = "blog")
    private Set<Series> series;
}
