package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.AccessLog;
import com.ltsw.dragon.base.service.AccessLogService;
import com.ltsw.dragon.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wujj
 * @version 1.0
 * @date 2019-11-28 16:36
 */
@Controller
@RequestMapping("accessLog")
public class AccessLogController {
    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping("list")
    public void list() {
    }

    @RequestMapping("search")
    @ResponseBody
    public ResponseEntity search(int page, int limit, AccessLog accessLog) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "id");
        Page<AccessLog> accessLogs = accessLogService.findAll(pageable, accessLog);
        return ResponseEntity.success(accessLogs);
    }

    @RequestMapping("get")
    public void get(Long id, Model model) {
        model.addAttribute(accessLogService.get(id).orElseGet(AccessLog::new));
    }
}
