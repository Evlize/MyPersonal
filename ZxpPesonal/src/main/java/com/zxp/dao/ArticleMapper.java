package com.zxp.dao;

import com.zxp.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //根据ID查询文章信息
    @Select("SELECT * FROM t_article WHERE id=#{id}")
    public Article selectArticleWithId(Integer id);
    //发表文章，同时使用@Option注解获取自动生成的逐渐id
    @Insert("INSERT INTO t_article(title,created,modified,tags,categories,"+"allow_comment,thumbnail,content)"+
            "VALUES(#{title},#{created},#{modified},#{tags},#{categories},"+"#{allowComment},#{thumbnail},#{content})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public Integer publishArticle(Article article);
    //文章分页查询
    @Select("SELECT * FROM t_article ORDER BY id DESC")
    public List<Article> selectArticleWithPage();
    //通过id删除文章
    @Delete("DELETE  FROM t_article WHERE id=#{id}")
    public void deleteArticleWithId(int id);
    //站点服务统计，统计文章数量
    @Select("SELECT COUNT(1) FROM t_article")
    public  Integer countArticle();
    //通过id更新文章
    public Integer updateArticleWithId(Article article);

    @Select("SELECT * FROM t_meta ORDER BY mid DESC")
    public List<Article> selectArticleWithCategories(Integer mid);
}
