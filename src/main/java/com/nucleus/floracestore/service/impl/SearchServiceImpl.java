package com.nucleus.floracestore.service.impl;

import com.nucleus.floracestore.repository.SearchRepository;
import com.nucleus.floracestore.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;

    @Autowired
    public SearchServiceImpl(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public List<?> searchQuery(String title) {
        return searchRepository.findByTitleLike(title);
    }
}
