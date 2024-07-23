package org.blog.blogflatformproject.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.Like;
import org.blog.blogflatformproject.board.domain.Reply;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.blog.blogflatformproject.board.dto.ReplyDto;
import org.blog.blogflatformproject.board.dto.SeriesDto;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.LikeService;
import org.blog.blogflatformproject.board.service.ReplyService;
import org.blog.blogflatformproject.board.service.SeriesService;
import org.blog.blogflatformproject.board.service.impl.BoardServiceImpl;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardRestController {


    private final BoardService boardService;

    private final ReplyService replyService;
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final SeriesService seriesService;
    private final LikeService likeService;

    @PutMapping("/updateboard")
    public ResponseEntity<String> updateBoard(@ModelAttribute Board board,
                                              @RequestParam(value = "seriesId", required = false) Long seriesId){
        if(seriesId!=null){
            board.setSeries(seriesService.findById(seriesId));
        }
        if(boardService.updateBoard(board)!=null){
            return ResponseEntity.ok("ok");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물 수정 실패");
        }
    }
    //댓글등록
    @PostMapping("/addReply")
    public ResponseEntity<ReplyDto> addReply(@RequestParam("replyContent") String replyContent , @RequestParam("boardId") String boardId,
                                             @CookieValue(value = "username",defaultValue = "") String username){
        if(username.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }

        User user = userService.findByUserName(username);
        Reply reply = replyService.addReply(replyContent,boardId,user.getUserId());
        ReplyDto replyDto = new ReplyDto();
        replyDto.setReplyCreated(reply.getReplyCreated());
        replyDto.setReplyContent(reply.getReplyContent());
        replyDto.setPreReplyId(reply.getPreReplyId());
        replyDto.setUsername(username);
        replyDto.setUserImgPath(user.getImagePath());

        if(replyDto.getReplyContent()!=null){
            return new ResponseEntity<>(replyDto,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
    //댓글불러오기
    @GetMapping("/getReplies")
    public Page<ReplyDto> getReplies(@RequestParam("boardId") Long boardId,
                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ReplyDto> replyPage = replyService.getReplies(boardId, pageable);
        return replyPage;
    }

    //답글등록
    @PostMapping("/addCommentReply")
    public ResponseEntity addCommentReply(@RequestBody ReplyDto replyDto,
                                          @CookieValue(value = "username",defaultValue = "") String username){
        User user = userService.findByUserName(username);
        replyDto.setUsername(username);
        Reply reply = replyService.addCommentReply(replyDto);
        ReplyDto replyDto1 = new ReplyDto();
        replyDto1.setReplyCreated(reply.getReplyCreated());
        replyDto1.setReplyContent(reply.getReplyContent());
        replyDto1.setPreReplyId(reply.getPreReplyId());
        replyDto1.setUsername(username);
        replyDto1.setUserImgPath(user.getImagePath());
        replyDto1.setPreReplyId(reply.getPreReplyId());
        if(reply.getReplyContent()!=null){
            return new ResponseEntity(replyDto1, HttpStatus.OK);
        }else{
            return new ResponseEntity(null,HttpStatus.BAD_REQUEST);
        }
    }

    //대댓글불러오기
    @GetMapping("/getCommentReplies")
    public List<ReplyDto> commentReplies(@RequestParam("replyId") Long replyId){
        log.info("replyId:{}",replyId);
        return replyService.getCommentReplies(replyId);
    }

    //접속한 유저의 시리즈불러오기
    @GetMapping("/getSeries")
    public List<SeriesDto> getSeries(@CookieValue(value="username" , defaultValue = "") String username){
        return seriesService.getSeries(username);
    }

    //접속한유저가 해당게시글에 좋아요를 눌렀는지 안눌렀는지 확인
    @GetMapping("/checkLike")
    public ResponseEntity checkLike(@RequestParam("boardId") Long boardId,
                                    @CookieValue(value="accessToken" , defaultValue = "") String accessTokenToken){

        Long userId =jwtTokenizer.getUserIdFromToken(accessTokenToken);
        Like like = likeService.findByBoardAndUser(boardId,userId);
        if(like!=null){
            return new ResponseEntity(like.getLikeId(),HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    //좋아요 등록
    @PostMapping("/addLike")
    public ResponseEntity addLike(@RequestParam("boardId") Long boardId,
                                  @CookieValue(value ="accessToken") String accessToken){
        Long userId = jwtTokenizer.getUserIdFromToken(accessToken);
        Like like = likeService.addLike(boardId,userId);
        return new ResponseEntity(like.getLikeId(),HttpStatus.OK);
    }

    //좋아요해제
    @DeleteMapping("/removeLike")
    public void removeLike(@RequestParam("boardId") Long boardId,
                                     @CookieValue(value ="accessToken") String accessToken){
        Long userId = jwtTokenizer.getUserIdFromToken(accessToken);
        likeService.removeLike(boardId,userId);
    }

    //조건별 정렬 board가져오기
    @GetMapping("/selectVal")
    public List<BoardDTO> selectVal(@RequestParam("selectVal") int selectVal){

        return boardService.selectVal(selectVal);
    }
    //검색해서 board 가져오기
    @GetMapping("/searchVal")
    public List<BoardDTO> searchVal(@RequestParam("searchVal") String searchVal){
        return boardService.searchVal(searchVal);
    }

    //해당블로그 조건별 정렬 board가져오기
    @GetMapping("/mySelectVal")
    public List<BoardDTO> mySelectVal(@RequestParam("selectVal") int selectVal,
                                      @RequestParam("username") String username){
        return boardService.mySelectVal(selectVal,username);
    }
}
