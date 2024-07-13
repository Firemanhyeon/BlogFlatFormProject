package org.blog.blogflatformproject.board.service;

import org.blog.blogflatformproject.blog.domain.Series;
import org.blog.blogflatformproject.board.dto.SeriesDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeriesService {
    Series addSeries(String seriesTitle, String username);
    List<SeriesDto> getSeries(String username);
    Series findById(Long seriesId);
}
