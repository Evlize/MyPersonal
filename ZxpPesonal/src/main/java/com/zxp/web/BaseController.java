package com.zxp.web;

import com.zxp.model.domain.User;
import com.zxp.utils.MapCache;
import com.zxp.utils.TaleUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Javanoteany on 2021/12/15.
 */
public abstract class BaseController {

    public static String THEME = "themes/default";

    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     * @param viewName
     * @return
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public User user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getId(HttpServletRequest request){
        return this.user(request).getId();
    }

    public String render_404() {
        return "comm/error_404";
    }

}
