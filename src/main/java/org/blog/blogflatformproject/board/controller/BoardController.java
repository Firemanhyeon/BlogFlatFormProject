package org.blog.blogflatformproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.service.BlogService;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.dto.BoardDTO;
import org.blog.blogflatformproject.board.domain.CKEditorUploadResponse;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.service.BoardService;
import org.blog.blogflatformproject.board.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BlogService blogService;
    private final TagService tagService;

    //글등록 폼으로 이동
    @GetMapping("/boardform")
    public String boardform(){
        return "pages/board/boardForm";
    }
    //글 등록
    @PostMapping("/addboard")
    public String addBoard(@ModelAttribute Board board,
                           @CookieValue(value="userId" , defaultValue = "") String logId,
                           @RequestParam("tagName") String tags){
        //태그 서비스호출 (이미 있는 태그면 그냥넣고 없는태그면 새로생성 후 Set안에넣기)
        Set<Tag> tagSet = tagService.addOrFind(tags);
        System.out.println("addboard탓음");
        //글등록
        Board board1 = boardService.addBoard(board,logId,tagSet);
        if(board1.getBoardId()!=null){
            return "redirect:/blog/"+logId;
        }else{
            return "redirect:/board/boardform";
        }
    }
    //ckeditor 이미지업로드
    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadBlogImg(@RequestParam("upload") MultipartFile upload,
                                           RedirectAttributes redirectAttributes){
         final String UPLOAD_DIR="/Users/jeonghohyeon/Desktop/blogUserImage";
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
            return ResponseEntity.ok().body(new CKEditorUploadResponse(1, "/Users/jeonghohyeon/Desktop/blogUserImage/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 업로드 실패!");
        }
    }
    //게시글 상세창 이동
    @GetMapping("/boardInfo/{boardid}")
    public String getBoardInfo(@PathVariable("boardid") Long boardId , Model model){
        Board brd = boardService.findById(boardId);
        BoardDTO dto = new BoardDTO();
        Set<Tag> set = brd.getTags();

        dto.setBoardContent(brd.getBoardContent());
        dto.setBoardTitle(brd.getBoardTitle());
        dto.setBlogName(brd.getBlog().getBlogName());
        dto.setBlogId(brd.getBlog().getBlogId());
        dto.setBoardId(brd.getBoardId());
        dto.setCreateAt(brd.getCreateAt());
        model.addAttribute("board" , dto);
        model.addAttribute("tags" , set);
        return "pages/board/boardInfo";
    }
    //게시글 수정창 이동
    @GetMapping("/update/{boardId}")
    public String getBoardUpdate(@PathVariable("boardId") Long boardId ,
                                @CookieValue(value="userId" , defaultValue = "") Long logId ,
                                 Model model){

        Blog blog = blogService.findByUserId(logId);
        Set<Board> set = blog.getBoard();
        for(Board post : set){
            if(Objects.equals(post.getBoardId(), boardId)){
                Board board = boardService.findById(boardId);
                System.out.println(board);
                model.addAttribute("board",board);
                return "pages/board/updateForm";
            }
        }
        return null;
    }

    @DeleteMapping("/delete/{boardId}")
    public  ResponseEntity<String> getBoardDelete(@PathVariable("boardId") Long boardId){

        if(boardService.deleteBoard(boardId)){
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("fail");
    }

}
