package org.blog.blogflatformproject.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follow",
        uniqueConstraints = @UniqueConstraint(columnNames={"follower_id", "following_id"}, name="chk_follows_unique_users"))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long follow_id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;


}
