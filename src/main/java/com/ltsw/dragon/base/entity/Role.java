package com.ltsw.dragon.base.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * 角色
 *
 * @author heshaobing
 */
@Data
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String role;
    private String description;

    @Transient
    private boolean checked;

    @Override
    public String getAuthority() {
        return role;
    }
}
