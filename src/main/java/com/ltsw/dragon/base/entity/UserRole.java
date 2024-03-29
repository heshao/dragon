package com.ltsw.dragon.base.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author heshaobing
 */
@Data
@ToString(exclude = {"user", "role"})
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
