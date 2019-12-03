package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.AccessLog;
import com.ltsw.dragon.base.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author heshaobing
 */
@Service
public class AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    public Optional<AccessLog> get(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return accessLogRepository.findById(id);
    }

    public Page<AccessLog> findAll(Pageable pageable, AccessLog accessLog) {
        return accessLogRepository.findAll((root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(accessLog.getTitle())) {
                Predicate predicate = cb.like(root.get("title"), accessLog.getTitle());
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(accessLog.getIp())) {
                Predicate predicate = cb.equal(root.get("ip"), accessLog.getIp());
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(accessLog.getUri())) {
                Predicate predicate = cb.like(root.get("uri"), accessLog.getUri());
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(accessLog.getHttpMethod())) {
                Predicate predicate = cb.equal(root.get("httpMethod"), accessLog.getHttpMethod());
                predicateList.add(predicate);
            }
            if (!StringUtils.isEmpty(accessLog.getCreateTime())) {
                Date start = accessLog.getCreateTime();
                LocalDateTime end = LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault()).plusDays(2);

                Predicate startPredicate = cb.greaterThanOrEqualTo(root.get("createTime"), start);
                predicateList.add(startPredicate);

                Predicate endPredicate = cb.lessThan(root.get("createTime"), Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));
                predicateList.add(endPredicate);
            }
            Predicate[] predicates = predicateList.toArray(new Predicate[0]);
            return query.where(predicates).getRestriction();
        }, pageable);
    }

    /**
     * 保存日志（异步）
     *
     * @param accessLog 日志
     */
    @Async
    public void save(AccessLog accessLog) {
        accessLogRepository.save(accessLog);
    }

}
