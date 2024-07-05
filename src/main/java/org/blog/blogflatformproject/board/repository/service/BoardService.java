package org.blog.blogflatformproject.board.repository.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardService {
        private final BoardRepository boardRepository;
        private final BlogRepository blogRepository;

        //최근날짜별로 boardList가져오기
        public List<Board> getBoardListOrderByCreatedAt(){
            return boardRepository.findAllOrderByCreateAtDesc();
        }

        //게시글생성
        public Board addBoard(Board board , String username , Set<Tag> tags) {
            Blog blog = blogRepository.findByUser_Username(username);
            board.setTags(tags);
            board.setBlog(blog);
            board.setCreateAt(LocalDate.now());
            String firstImagePath = sanitizeBoardContent(board.getBoardContent());
            firstImagePath = extractFirstImageUrl(firstImagePath) ;
            board.setFirstImagePath(firstImagePath);
            return boardRepository.save(board);
        }

        //게시글 수정
        public Board updateBoard(Board board){
            Board beforeBoard = findById(board.getBoardId());
            System.out.println(beforeBoard);
            System.out.println(board);

            beforeBoard.setBoardTitle(board.getBoardTitle());
            beforeBoard.setBoardContent(board.getBoardContent());
            beforeBoard.setOpenYn(board.isOpenYn());
            beforeBoard.setTemporaryYn(board.isTemporaryYn());
            String firstImagePath = sanitizeBoardContent(board.getBoardContent());
            firstImagePath = extractFirstImageUrl(firstImagePath) ;
            beforeBoard.setFirstImagePath(firstImagePath);

            return boardRepository.save(beforeBoard);
        }

        //해당유저의 글찾기
        public List<Board> findByUsername(String username){
            Blog blog = blogRepository.findByUser_Username(username);
            return boardRepository.findByBlog(blog);
        }


    //게시글번호로 글 정보 가져오기
    public Board findById(Long userId){
            return boardRepository.findById(userId).orElse(null);
    }

    //이미지 태그를 제외한 나머지 태그 모두 제거하는 로직
    public String sanitizeBoardContent(String content) {
        Document document = Jsoup.parse(content);

        // HTML 태그 제거하면서 이미지 태그 유지
        Whitelist whitelist = Whitelist.none().addTags("img").addAttributes("img", "src");
        String sanitizedContent = Jsoup.clean(document.html(), whitelist);

        return sanitizedContent;
    }

    // board.content에 저장되어있는 이미지 중에서 첫 번째 이미지 scr 추출하는 로직
    public String extractFirstImageUrl(String content) {
        Document doc = Jsoup.parse(content);
        Elements imgTags = doc.select("img");

        if (!imgTags.isEmpty()) {
            Element firstImg = imgTags.first();
            return firstImg.attr("src");
        }

        // 이미지가 없는 경우 null을 반환한다
        return null;
    }

    //게시글삭제
    public boolean deleteBoard(Long boardId){
            Board brd = boardRepository.findById(boardId).orElse(null);
            if(brd!=null){
                boardRepository.delete(brd);
                return true;
            }
            return false;
    }
}
