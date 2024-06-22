package org.blog.blogflatformproject.board.controller;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Board;
import org.blog.blogflatformproject.board.domain.CKEditorUploadResponse;
import org.blog.blogflatformproject.board.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //글등록 폼으로 이동
    @GetMapping("/boardform")
    public String boardform(){
        return "pages/board/boardForm";
    }
    //글 등록
    @PostMapping("/addboard")
    public String addBoard(@ModelAttribute Board board,
                           @CookieValue(value="userId" , defaultValue = "") String logId){
        Board board1 = boardService.addBoard(board,logId);
        if(board1.getBoardId()!=null){
            return "redirect:/blog/"+logId;
        }else{
            return "redirect:/board/boardform";
        }
    }
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


}
