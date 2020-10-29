package com.bei.service;

import com.bei.dao.BlogRepositary;
import com.bei.exception.NotFoundException;
import com.bei.po.Blog;
import com.bei.po.Type;
import com.bei.util.MarkdownUtils;
import com.bei.util.MyBeanUtils;
import com.bei.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author Administrator
 * @date 2020/8/22 0022 - 22:01
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepositary blogRepositary;

    @Override
    public Blog getBlog(Long id) {
        return blogRepositary.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = blogRepositary.findById(id).orElse(null);
        if(blog == null){
            throw new NotFoundException("不好意思，你要找的博客不存在~");
        }
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content =b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //设置访问次数加1
        blogRepositary.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepositary.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepositary.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepositary.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join=root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepositary.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepositary.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepositary.findGroupYear();
        LinkedHashMap<String,List<Blog>> map = new LinkedHashMap<>();
        for(String year : years){
            map.put(year,blogRepositary.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepositary.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepositary.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBLog(Long id, Blog blog) {
        Blog b=blogRepositary.findById(id).orElse(null);
        if(b==null){
            throw new NotFoundException("该博客不存在~");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepositary.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepositary.deleteById(id);
    }

}
