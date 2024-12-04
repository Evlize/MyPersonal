package com.zxp.service;

import com.github.pagehelper.PageInfo;
import com.zxp.model.domain.Article;
import com.zxp.model.domain.Comment;

import java.util.List;

public interface ICommentService {
    //获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid, int page, int count);
    // 用户发表评论
    public void pushComment(Comment comment);

    // 分页查询评论列表
    public PageInfo<Comment> selectCommentWithPage(Integer page, Integer count);

    //    // 统计前10的热度评论信息

    // 根据主键删除评论
    public void deleteCommentWithId2(int id);

    Comment getCommentById(Integer id);
}
