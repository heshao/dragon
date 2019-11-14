package com.ltsw.dragon.base.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * 角色
 *
 * @author heshaobing
 */
@Data
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String role;
    private String description;

    @Transient
    private Collection<MenuRole> menuRoles;

    @Override
    public String getAuthority() {
        return role;
    }
}
