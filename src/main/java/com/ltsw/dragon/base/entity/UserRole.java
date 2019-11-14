package com.ltsw.dragon.base.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author heshaobing
 */
@Data
@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Role role;
}
