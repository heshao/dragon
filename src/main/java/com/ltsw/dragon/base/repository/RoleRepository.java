package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author heshaobing
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 角色是否已存在
     *
     * @param id   id
     * @param role 角色
     * @return
     */
    boolean existsByIdAndRole(Long id, String role);

    /**
     * 角色是否已存在
     *
     * @param role 角色
     * @return
     */
    boolean existsByRole(String role);
}
