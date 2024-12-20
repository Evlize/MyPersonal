package com.zxp.service;

import com.github.pagehelper.PageInfo;

import com.zxp.model.domain.AttachVo;

/**
 * Created by Javanoteany on 2021/12/15.
 */
public interface IAttachService  {
    /**
     * 分页查询附件
     * @param page
     * @param limit
     * @return
     */
    PageInfo<AttachVo> getAttachs(Integer page, Integer limit);

    /**
     * 保存附件
     *
     * @param fname
     * @param fkey
     * @param ftype
     * @param
     */
    void save(String fname, String fkey, String ftype);

    /**
     * 根据附件id查询附件
     * @param id
     * @return
     */
    AttachVo selectById(Integer id);

    /**
     * 删除附件
     * @param id
     */
    void deleteById(Integer id);
}
