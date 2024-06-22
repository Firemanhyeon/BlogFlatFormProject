package org.blog.blogflatformproject.board.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "pre_category")
    private Long preCategory;

//    @OneToMany(mappedBy = "category")
//    private Set<Board> boards;
}
