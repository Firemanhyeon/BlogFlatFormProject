package org.blog.blogflatformproject.board.domain;


public class CKEditorUploadResponse {
    private int uploaded;
    private String url;

    public CKEditorUploadResponse(int uploaded, String url) {
        this.uploaded = uploaded;
        this.url = url;
    }

    public int getUploaded() {
        return uploaded;
    }

    public String getUrl() {
        return url;
    }
}
