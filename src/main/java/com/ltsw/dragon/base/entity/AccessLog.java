package com.ltsw.dragon.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author heshaobing
 */
@Data
@Entity
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String httpMethod;
    private String uri;
    private String params;
    private String method;
    private String exception;
    private String ip;
    private Long take;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public AccessLog() {

    }

    public AccessLog(String httpMethod, String uri, String params, String method, String ip, Date createTime) {
        this.httpMethod = httpMethod;
        this.uri = uri;
        this.params = params;
        this.method = method;
        this.ip = ip;
        this.createTime = createTime;
    }


}
