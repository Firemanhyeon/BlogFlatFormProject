package org.blog.blogflatformproject.board.repository;

import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {



    //조회수많은순
    List<Board> findAllByOpenYnTrueAndTemporaryYnTrueOrderByVisitCountDesc();
    //내게시글 조회수많은순
    List<Board> findAllByOpenYnTrueAndTemporaryYnTrueAndBlogOrderByVisitCountDesc(Blog blog);

    //생성날짜순
    List<Board> findAllByOpenYnFalseOrTemporaryYnFalseAndBlogOrderByCreateAtAsc(Blog blog);
    //내게시글 생성날짜순
    List<Board> findByBlogAndOpenYnTrueAndTemporaryYnTrueOrderByCreateAtAsc(Blog blog);

    //좋아요많은순
    @Query("SELECT b FROM Board b WHERE b.openYn = true AND b.temporaryYn = true ORDER BY SIZE(b.likes) DESC")
    List<Board> findAllByOpenYnAndTemporaryYnOrderByLikes();
    //내게시글 좋아요많은순
    @Query("SELECT b FROM Board b WHERE b.openYn = true AND b.temporaryYn = true AND b.blog = :blog ORDER BY SIZE(b.likes) DESC")
    List<Board> findAllByOpenYnAndTemporaryYnAndBlogOrderByLikes(@Param("blog") Blog blog);


    @Query("SELECT b FROM Board b where b.openYn=true and b.temporaryYn=true ORDER BY b.createAt DESC")
    List<Board> findAllOrderByCreateAtDesc();

    @Modifying
    @Query("UPDATE Board b SET b.visitCount = b.visitCount + 1 WHERE b.boardId = :boardId")
    void updateVisitCnt(@Param("boardId")Long boardId);
    }

