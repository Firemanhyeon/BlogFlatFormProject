package org.blog.blogflatformproject.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.domain.Series;

@Entity
@Table(name = "board")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_title")
    private String boardTitle;

    @Column(name = "board_content", length = 10000)
    private String boardContent;

    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "temporary_yn")
    private boolean temporaryYn;

    @Column(name = "open_yn")
    private boolean openYn;

    @Column(name = "visit_count")
    private int visitCount;

    @Column(name = "first_image_path")
    private String firstImagePath;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @OneToMany(mappedBy = "board")
    private Set<Reply> replies;

    @OneToMany(mappedBy = "board")
    private Set<Image> images;

    @OneToMany(mappedBy = "board")
    private Set<Like> likes;

    @ManyToMany
    @JoinTable(
            name = "tag_board",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
