package org.blog.blogflatformproject.common.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class CommonRestController {

    private static final String UPLOAD_DIR = "/Users/jeonghohyeon/Desktop/blogUserImage/";

    // 이미지 불러오기
    @GetMapping("/Users/jeonghohyeon/Desktop/blogUserImage/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            // 파일 경로를 생성하고, 리소스를 로드
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                // 파일 이름을 UTF-8로 인코딩
                String encodedFilename = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
        }
    }
}
