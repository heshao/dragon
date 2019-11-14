package com.ltsw.dragon.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author heshao
 */
@Data
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String name;
    private String uri;
    private boolean enabled;
    private boolean visible;
    private int sort;

    @Transient
    private Collection<MenuRole> menuRoles;
}
