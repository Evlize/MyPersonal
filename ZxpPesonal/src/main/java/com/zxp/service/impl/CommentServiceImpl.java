package com.zxp.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxp.dao.CommentMapper;
import com.zxp.dao.StatisticMapper;
import com.zxp.model.domain.Article;
import com.zxp.model.domain.Comment;
import com.zxp.model.domain.Statistic;
import com.zxp.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    // 根据文章id分页查询评论

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }
    @Autowired
    private StatisticMapper statisticMapper;
    // 用户发表评论
    @Override
    public void pushComment(Comment comment){
        commentMapper.pushComment(comment);
        // 更新文章评论数据量
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }
    //根据id查询单个评论详情，并使用Redis进行缓存管理
    public Comment selectCommentWithId(Integer id){
        Comment comment = null;
        Object o = redisTemplate.opsForValue().get("comment_" + id);
        if(o!=null){
            comment=(Comment)o;
        }else{
            comment = commentMapper.selectCommentWithId(id);
            if(comment!=null){
                redisTemplate.opsForValue().set("comment_" + id,comment);
            }
        }
        return comment;
    }
    // 分页查询评论列表
    @Override
    public PageInfo<Comment> selectCommentWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage2();
        // 封装评论统计数据
        for (int i = 0; i < commentList.size(); i++) {
            Comment comment = commentList.get(i);
            Statistic statistic= statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
            comment.setAuthor(comment.getAuthor());
            comment.setCommentsNum2(statistic.getCommentsNum());
        }
        PageInfo<Comment> pageInfo=new PageInfo<>(commentList);
        return pageInfo;
    }
    // 删除评论
    @Autowired
    private CommentMapper commentMapper2;
    @Override
    public void deleteCommentWithId2(int id) {
        // 删除评论的同时，删除对应的缓存
        commentMapper.deleteCommentWithId2(id);
        redisTemplate.delete("comment_" + id);
    }
    //getById
    @Override
    public Comment getCommentById(Integer id) {
        if (null != id) {
            return commentMapper.selectByPrimaryKey(id);
        }
        return null;
    }
}
