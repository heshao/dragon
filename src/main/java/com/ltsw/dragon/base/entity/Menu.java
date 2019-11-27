package com.ltsw.dragon.base.entity;

import com.ltsw.dragon.common.Tree;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author heshao
 */
@Data
@NoArgsConstructor
@Entity
public class Menu implements Tree<Menu> {
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
    private Collection<Role> roles;
    @Transient
    private Collection<Menu> children;

    public Menu(long id, Long parentId, String name, String uri, boolean enabled, boolean visible, int sort) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.uri = uri;
        this.enabled = enabled;
        this.visible = visible;
        this.sort = sort;
    }

    @Override
    public Collection<Menu> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Collection<Menu> children) {
        this.children = children;
    }

}
