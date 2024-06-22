package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.board.domain.Tag;
import org.blog.blogflatformproject.board.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Set<Tag> addOrFind(String tags){
        //찾아보고 없으면 tags테이블에 먼저 save 하고 tags-board 테이블에 save
        String[] tagList = tags.split(",");
        Set<Tag> tagSet = new LinkedHashSet<>();
        for(String str : tagList){
            Tag tag = tagRepository.findByTagName(str);
            if(tag==null){
                tag=new Tag();
                tag.setTagName(str);
                tagSet.add(tagRepository.save(tag));
            }else{
                tagSet.add(tag);
            }
        }

        return tagSet;
    }
}
