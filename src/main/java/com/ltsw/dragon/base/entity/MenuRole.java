package com.ltsw.dragon.base.entity;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author heshaobing
 */
@Data
@ToString(exclude = {"menu", "role"})
@Entity
@NamedEntityGraph(name = "withAll", attributeNodes = {@NamedAttributeNode("menu"), @NamedAttributeNode("role")})
public class MenuRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Menu menu;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Role role;


}
