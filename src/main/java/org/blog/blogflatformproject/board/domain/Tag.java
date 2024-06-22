package org.blog.blogflatformproject.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(name = "tag_name")
    private String tagName;


}
