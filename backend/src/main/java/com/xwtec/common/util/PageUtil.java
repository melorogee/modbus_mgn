package com.xwtec.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;

import java.util.Map;


public class PageUtil {

    /**
     * 获取分页参数
     *
     * @param params
     * @return
     */
    public static Page getPage(Map<String, Object> params) {
        String pageNo = (String) params.get("pageNo");
        String pageSize = (String) params.get("pageSize");
        if (StringUtils.isBlank(pageNo)) pageNo = "1";
        if (StringUtils.isBlank(pageSize)) pageSize = "10";
        return new Page(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
    }
}
