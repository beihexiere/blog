package com.bei.service;

import com.bei.po.Blog;
import com.bei.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020/8/22 0022 - 22:00
 */
public interface BlogService {

    Blog getBlog(Long id);

    Blog getBlogAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBLog(Long id,Blog blog);

    void deleteBlog(Long id);

}
