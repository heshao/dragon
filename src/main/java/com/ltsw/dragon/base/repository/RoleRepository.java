package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param authority 角色
     * @return
     */
    boolean existsByIdAndAuthority(Long id, String authority);

    /**
     * 角色是否已存在
     *
     * @param authority 角色
     * @return
     */
    boolean existsByAuthority(String authority);

    /**
     * 分页查询角色
     *
     * @param name     名称
     * @param pageable 分页
     * @return
     */
    Page<Role> findByNameContains(String name, Pageable pageable);

}
