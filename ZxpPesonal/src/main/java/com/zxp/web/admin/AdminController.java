package com.zxp.web.admin;


import com.github.pagehelper.PageInfo;
import com.zxp.model.ResponseData.ArticleResponseData;
import com.zxp.model.ResponseData.CommentResponseData;
import com.zxp.model.ResponseData.StaticticsBo;
import com.zxp.model.domain.Article;
import com.zxp.model.domain.Comment;

import com.zxp.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    //打印日志在控制台上
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ISiteService siteServiceImpl;
    @Autowired
    private IArticleService articleServiceImpl;
    @Autowired
    private ICommentService commentServiceImpl;
    // 管理中心起始页
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        // 获取最新的5篇博客、评论以及统计数据
        List<Article> articles = siteServiceImpl.recentArticles(5);
        List<Comment> comments = siteServiceImpl.recentComments(5);
        StaticticsBo staticticsBo = siteServiceImpl.getStatistics();
        // 向Request域中存储数据
        request.setAttribute("comments", comments);//将comments作为request对象的一个属性存放到request对象中
        request.setAttribute("articles", articles);
        request.setAttribute("statistics", staticticsBo);
        return "back/index";
    }
    // 向文章发表页面跳转
    @GetMapping(value = "/article/toEditPage")
    public String newArticle( ) {
        return "back/article_edit";//跳转页面
    }
    // 发表文章
    @PostMapping(value = "/article/publish")//点击发表会跳转到此路径
    @ResponseBody
    public ArticleResponseData publishArticle(Article article) {
        if (StringUtils.isBlank(article.getCategories())) {
            article.setCategories("默认分类");
        }
        try {
            articleServiceImpl.publish(article);//调用articleServiceImpl.publish（）方法来保存文章到数据库
            logger.info("文章发布成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章发布失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
    // 跳转到后台文章列表页面
    @GetMapping(value = "/article")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "count", defaultValue = "10") int count,
                        HttpServletRequest request) {
        PageInfo<Article> pageInfo = articleServiceImpl.selectArticleWithPage(page, count);
        request.setAttribute("articles", pageInfo);
        return "back/article_list";
    }
    // 向文章修改页面跳转
    @GetMapping(value = "/article/{id}")
    public String editArticle(@PathVariable("id") String id, HttpServletRequest request) {
        Article article = articleServiceImpl.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents", article);
        request.setAttribute("categories", article.getCategories());
        return "back/article_edit";
    }

    // 文章修改处理
    @PostMapping(value = "/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article) {
        try {
            articleServiceImpl.updateArticleWithId(article);
            logger.info("文章更新成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章更新失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
    // 文章删除
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            articleServiceImpl.deleteArticleWithId(id);
            logger.info("文章删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
    //跳转到评论管理页面
    @GetMapping(value = "/comment")
    public String index2(@RequestParam(value = "page", defaultValue = "1") int page1,
                         @RequestParam(value = "count", defaultValue = "10") int count1,
                         HttpServletRequest request2) {
        PageInfo<Comment> pageInfo = commentServiceImpl.selectCommentWithPage(page1, count1);
        request2.setAttribute("comments", pageInfo);//把值传给浏览器应该是这句
        return "back/comment_list";
    }
    //评论删除处理
    @PostMapping(value = "/comment/delete")
    @ResponseBody
    public CommentResponseData delete(@RequestParam Integer id) {
        try {
            Comment comments = commentServiceImpl.getCommentById(id);
            if(null == comments){
                return CommentResponseData.fail("不存在该评论");
            }
            commentServiceImpl.deleteCommentWithId2(id);
        } catch (Exception e) {
            logger.error("评论删除失败，错误信息: "+e.getMessage());
            return CommentResponseData.fail();
        }
        return CommentResponseData.ok();
    }

}
