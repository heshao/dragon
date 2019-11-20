package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.AccessLog;
import com.ltsw.dragon.base.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author heshaobing
 */
@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public void save(AccessLog accessLog) {
        accessLogRepository.save(accessLog);
    }
}
