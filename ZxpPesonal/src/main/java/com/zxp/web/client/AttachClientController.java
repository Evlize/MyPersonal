package com.zxp.web.client;

import com.github.pagehelper.PageInfo;

import com.zxp.constant.WebConst;
import com.zxp.model.domain.AttachVo;
import com.zxp.model.dto.Types;
import com.zxp.service.IAttachService;
import com.zxp.utils.Commons;
import com.zxp.utils.TaleUtils;
import com.zxp.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 附件管理
 *
 * Created by Javanoteany on 2021/12/15.
 */
@Controller
@RequestMapping("/attach_client")
public class AttachClientController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachClientController.class);

    public static final String CLASSPATH = TaleUtils.getUplodFilePath();

    @Autowired
    private IAttachService attachService;



    /**
     * 附件页面
     *
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/attach")
    public String index(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {
        PageInfo<AttachVo> attachPaginator = attachService.getAttachs(page, limit);
        request.setAttribute("attachs", attachPaginator);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE / 1024);
        return "client/attach_client";
    }

    @GetMapping("/attach/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam String fkey) {
        try {
            File file = new File(CLASSPATH + fkey);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            byte[] data = Files.readAllBytes(file.toPath());
            ByteArrayResource resource = new ByteArrayResource(data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fkey);
            headers.setContentLength(data.length);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("下载文件失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
