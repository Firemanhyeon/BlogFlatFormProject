package org.blog.blogflatformproject.board.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.blog.domain.Blog;
import org.blog.blogflatformproject.blog.domain.Series;
import org.blog.blogflatformproject.blog.repository.BlogRepository;
import org.blog.blogflatformproject.board.dto.SeriesDto;
import org.blog.blogflatformproject.board.repository.SeriesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesRepository seriesRepository;
    private final BlogRepository blogRepository;
    //시리즈등록
    public Series addSeries(String seriesTitle,String username){
        Blog blog = blogRepository.findByUser_Username(username);

        Series series = new Series();
        series.setSeriesTitle(seriesTitle);
        series.setBlog(blog);

        return seriesRepository.save(series);
    }
    //해당블로그의 시리즈 찾기
    public List<SeriesDto> getSeries(String username){
        Blog blog = blogRepository.findByUser_Username(username);
        List<Series> series = seriesRepository.findAllByBlog(blog);
        List<SeriesDto> dto = new ArrayList<>();
        for(Series series1 : series){
            SeriesDto seriesDto = new SeriesDto();
            seriesDto.setSeriesId(series1.getSeriesId());
            seriesDto.setSeriesTitle(series1.getSeriesTitle());
            dto.add(seriesDto);
        }
        return dto;
    }
    //시리즈아이디로 시리즈찾기
    public Series findById(Long seriesId){
        return seriesRepository.findById(seriesId).orElse(null);
    }
}
