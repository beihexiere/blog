package com.bei.service;

import com.bei.po.Comment;

import java.util.List;

/**
 * @author Administrator
 * @date 2020/9/10 0010 - 15:09
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
