package org.blog.blogflatformproject.board.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.domain.Series;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.CKEditorUploadResponse;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.blog.blogflatformproject.board.dto.ReplyDto;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.ReplyService;
import org.blog.blogflatformproject.board.service.SeriesService;
import org.blog.blogflatformproject.board.service.TagService;
import org.blog.blogflatformproject.board.service.ViewService;
import org.blog.blogflatformproject.jwt.util.JwtTokenizer;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final BlogService blogService;
    private final TagService tagService;
    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final ReplyService replyService;
    private final SeriesService seriesService;
    private final ViewService viewService;

    //글등록 폼으로 이동
    @GetMapping("/boardform")
    public String boardform() {
        return "pages/board/boardForm";
    }

    //글 등록
    @PostMapping("/addboard")
    public String addBoard(@ModelAttribute Board board,
                           @CookieValue(value = "username", defaultValue = "") String username,
                           @RequestParam("tagName") String tags,
                           @RequestParam("seriesId") Optional<Long> seriesId) {
        //태그 서비스호출 (이미 있는 태그면 그냥넣고 없는태그면 새로생성 후 Set안에넣기)
        Set<Tag> tagSet = tagService.addOrFind(tags);
        //시리즈 찾기
        if (seriesId.isPresent()) {
            Series series = seriesService.findById(seriesId.get());
            board.setSeries(series);
        }
        //글등록
        Board board1 = boardService.addBoard(board, username, tagSet);
        if (board1.getBoardId() != null) {
            return "redirect:/blog/" + username;
        } else {
            return "redirect:/board/boardform";
        }
    }

    //ckeditor 이미지업로드
    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadBlogImg(@RequestParam("upload") MultipartFile upload,
                                           RedirectAttributes redirectAttributes) {
        final String UPLOAD_DIR = "/Users/jeonghohyeon/Desktop/blogUserImage";
        try {
            // 업로드할 디렉토리가 없으면 생성
            if (!Files.exists(Paths.get(UPLOAD_DIR))) {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
            }

            // 고유한 파일명 생성
            String fileName = UUID.randomUUID().toString() + "_" + upload.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // 파일 저장
            Files.write(filePath, upload.getBytes());

            // CKEditor에서 필요한 JSON 반환
            return ResponseEntity.ok()
                    .body(new CKEditorUploadResponse(1, "/Users/jeonghohyeon/Desktop/blogUserImage/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 실패!");
        }
    }


    //게시글 상세창 이동
    @GetMapping("/boardInfo/{boardId}")
    public String getBoardInfo(@PathVariable("boardId") Long boardId, Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size,
                               @CookieValue(value = "accessToken", required = false) String accessToken) {

        Board brd = boardService.findById(boardId);
        //해당글의 유저 가져오기
        Long userId = brd.getBlog().getUser().getUserId();
        if (accessToken != null) {
            Long myId = jwtTokenizer.getUserIdFromToken(accessToken);

            //자기자신일때는 조회수 증가 안됨.
            if (userId != myId) {
                boardService.updateVisitCnt(brd, myId);
            }
        }
        User user = userService.findByUserId(userId);

        BoardDTO dto = new BoardDTO();
        Set<Tag> set = brd.getTags();
        //해당글의 댓글 가져오기
        Pageable pageable = PageRequest.of(page, size);
        Page<ReplyDto> replies = replyService.getReplies(brd.getBoardId(), pageable);

        dto.setBoardContent(brd.getBoardContent());
        dto.setBoardTitle(brd.getBoardTitle());
        dto.setBlogName(brd.getBlog().getBlogName());
        dto.setBlogId(brd.getBlog().getBlogId());
        dto.setBoardId(brd.getBoardId());
        dto.setCreateAt(brd.getCreateAt());
        dto.setUserImgPath(user.getImagePath());
        dto.setUserName(user.getUsername());
        dto.setVisitCount(brd.getVisitCount());

        model.addAttribute("board", dto);
        model.addAttribute("tags", set);
        model.addAttribute("replies", replies);
        model.addAttribute("accessToken", accessToken);

        return "pages/board/boardInfo";
    }

    //게시글 수정창 이동
    @GetMapping("/update/{boardId}")
    public String getBoardUpdate(@PathVariable("boardId") Long boardId,
                                 @CookieValue(value = "username", defaultValue = "") String username,
                                 Model model) {

        Blog blog = blogService.findByUsername(username);
        Set<Board> set = blog.getBoard();
        for (Board post : set) {
            if (Objects.equals(post.getBoardId(), boardId)) {
                Board board = boardService.findById(boardId);
                model.addAttribute("board", board);
                model.addAttribute("series", board.getSeries());
                return "pages/board/updateForm";
            }
        }
        return null;
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> getBoardDelete(@PathVariable("boardId") Long boardId) {

        if (boardService.deleteBoard(boardId)) {
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("fail");
    }

    //시리즈상세
    @GetMapping("/getSeriesInfo/{seriesId}")
    public String getSeriesInfo(@PathVariable("seriesId") Long seriesId, Model model) {
        Set<Board> brd = seriesService.findById(seriesId).getBoard();
        Set<BoardDTO> dto = new HashSet<>();
        for (Board board : brd) {
            if (board.isTemporaryYn() && board.isOpenYn()) {
                BoardDTO boardDTO = new BoardDTO();
                boardDTO.setBoardId(board.getBoardId());
                boardDTO.setBoardTitle(board.getBoardTitle());
                boardDTO.setFirstImagePath(board.getFirstImagePath());
                dto.add(boardDTO);
            }
        }
        model.addAttribute("board", dto);
        return "pages/board/seriesInfo";
    }

    //임시글 및 비공개 글 목록 페이지 이동
    @GetMapping("/temporary")
    public String getTemporary(@CookieValue(value = "accessToken", defaultValue = "") String accessToken, Model model) {

        Long userId = jwtTokenizer.getUserIdFromToken(accessToken);
        User user = userService.findByUserId(userId);
        List<BoardDTO> list = boardService.getTemporaryAndOpenList(user.getUsername());
        log.info("list:{}", list);
        model.addAttribute("board", list);
        return "pages/board/temporaryInfo";
    }

    //내가 읽은 게시글 보기
    @GetMapping("/readBoard")
    public String readBoard(@CookieValue(value = "accessToken", defaultValue = "", required = false) String accessToken,
                            Model model) {

        Long userId = jwtTokenizer.getUserIdFromToken(accessToken);
        List<BoardDTO> list = viewService.findByUserId(userId);
        model.addAttribute("board", list);
        return "pages/board/readMyBoard";
    }

}
