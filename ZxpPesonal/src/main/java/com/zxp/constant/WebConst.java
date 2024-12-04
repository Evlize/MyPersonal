package com.zxp.constant;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Javanoteany on 2021/12/15.
 */
@Component
public class WebConst {

    public static String LOGIN_SESSION_KEY = "login_user";



    /**
     * 上传文件最大1M
     */
    public static Integer MAX_FILE_SIZE = 10000000;

}
