package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.blog.blogflatformproject.board.repository.BoardRepository;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
        private final BoardRepository boardRepository;
        private final BlogRepository blogRepository;
        private final JwtTokenizer jwtTokenizer;
    private final UserRepository userRepository;

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
            beforeBoard.setSeries(board.getSeries());
            String firstImagePath = sanitizeBoardContent(board.getBoardContent());
            firstImagePath = extractFirstImageUrl(firstImagePath) ;
            beforeBoard.setFirstImagePath(firstImagePath);

            return boardRepository.save(beforeBoard);
        }

        //해당유저의 글찾기
        public List<Board> findByUsername(String username){
            Blog blog = blogRepository.findByUser_Username(username);
            return boardRepository.findByBlogAndOpenYnTrueAndTemporaryYnTrueOrderByCreateAtAsc(blog);
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

    //조회수+1
    @Transactional
    public void updateVisitCnt(Board brd){
            boardRepository.updateVisitCnt(brd.getBoardId());
    }

    //조건별 board 리스트 불러오기
    public List<BoardDTO> selectVal(int selectVal){
            List<Board> brd = new ArrayList<>();
       switch (selectVal){
           //최근날짜별
           case 1:
               brd=boardRepository.findAllOrderByCreateAtDesc();
               break;
           case 2:
                brd=boardRepository.findAllByOpenYnAndTemporaryYnOrderByLikes();
               break;
           case 3:
                brd=boardRepository.findAllByOpenYnTrueAndTemporaryYnTrueOrderByVisitCountDesc();
                break;
       }
       List<BoardDTO> list = new ArrayList<>();
       for(Board board : brd){
           BoardDTO dto = new BoardDTO();
           dto.setBoardId(board.getBoardId());
           dto.setBoardTitle(board.getBoardTitle());
           dto.setFirstImagePath(board.getFirstImagePath());
           dto.setVisitCount(board.getVisitCount());
           list.add(dto);
       }
       return list;
    }


    //해당블로그 조건별 board 리스트 불러오기
    public List<BoardDTO> mySelectVal(int selectVal , String username){

        Blog blog = blogRepository.findByUser_Username(username);

        List<Board> brd = new ArrayList<>();
        switch (selectVal){
            //최근날짜별
            case 1:
                brd=boardRepository.findByBlogAndOpenYnTrueAndTemporaryYnTrueOrderByCreateAtAsc(blog);
                break;
            //좋아요별
            case 2:
                brd=boardRepository.findAllByOpenYnAndTemporaryYnAndBlogOrderByLikes(blog);
                break;
            //조회수별
            case 3:
                brd=boardRepository.findAllByOpenYnTrueAndTemporaryYnTrueAndBlogOrderByVisitCountDesc(blog);
                break;
        }
        List<BoardDTO> list = new ArrayList<>();
        for(Board board : brd){
            BoardDTO dto = new BoardDTO();
            dto.setBoardId(board.getBoardId());
            dto.setBoardTitle(board.getBoardTitle());
            dto.setFirstImagePath(board.getFirstImagePath());
            dto.setVisitCount(board.getVisitCount());
            list.add(dto);
        }
        log.info("list:{}",list);
        return list;
    }

    //비공개글 및 임시글 가져오기
    public List<BoardDTO> getTemporaryAndOpenList(String username){
            Blog blog = blogRepository.findByUser_Username(username);
        List<Board> boards = boardRepository.findAllByOpenYnFalseOrTemporaryYnFalseAndBlogOrderByCreateAtAsc(blog);
        List<BoardDTO> dtos = new ArrayList<>();
        for(Board board : boards){
            BoardDTO dto = new BoardDTO();
            dto.setBoardId(board.getBoardId());
            dto.setBoardTitle(board.getBoardTitle());
            dto.setFirstImagePath(board.getFirstImagePath());
            dtos.add(dto);
        }
        return dtos;
    }

}
