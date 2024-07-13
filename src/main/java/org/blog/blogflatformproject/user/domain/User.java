package org.blog.blogflatformproject.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.board.domain.Like;
import org.blog.blogflatformproject.board.domain.Views;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true , name = "username",nullable = false)
    private String username;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;
    //데이터베이스 저장되지않는 임시 필드
    @Transient
    private MultipartFile imageFile;

    @Column(name="email_status")
    private boolean emailStatus;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "follower" , cascade = CascadeType.ALL)
    private Set<Follow> followers;

    @OneToMany(mappedBy = "following" , cascade = CascadeType.ALL)
    private Set<Follow> following;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private Set<Like> likes;

}
