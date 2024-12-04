package com.zxp.web.admin;

import com.github.pagehelper.PageInfo;

import com.zxp.constant.WebConst;
import com.zxp.exception.TipException;
import com.zxp.model.ResponseData.RestResponseAttach;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件管理
 *
 * Created by Javanoteany on 2021/12/15.
 */
@Controller
@RequestMapping("/admin")
public class AttachController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);

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
        return "back/attach";
    }

    /**
     * 上传文件接口
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/attach/upload")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseAttach upload(HttpServletRequest request, @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
        List<String> errorFiles = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String fname = multipartFile.getOriginalFilename();
                if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
                    String fkey = TaleUtils.getFileKey(fname);
                    String ftype = TaleUtils.isImage(multipartFile.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType();
                    File file = new File(CLASSPATH + fkey);
                    FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
                    attachService.save(fname, fkey, ftype);
                } else {
                    errorFiles.add(fname);
                }
            }
        } catch (Exception e) {
            LOGGER.error("上传文件失败", e); // 记录详细的错误信息
            return RestResponseAttach.fail();
        }
        return RestResponseAttach.ok(errorFiles);
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


    /**
     * 删除附件
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/attach/delete",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseAttach delete(@RequestParam Integer id, HttpServletRequest request) {
        try {
            AttachVo attach = attachService.selectById(id);
            if (null == attach){ return RestResponseAttach.fail("不存在该附件");}
            attachService.deleteById(id);
            new File(CLASSPATH+attach.getFkey()).delete();
        } catch (Exception e) {
            String msg = "附件删除失败";
            if (e instanceof TipException) msg = e.getMessage();
            else LOGGER.error(msg, e);
            return RestResponseAttach.fail(msg);
        }
        return RestResponseAttach.ok();
    }

}
