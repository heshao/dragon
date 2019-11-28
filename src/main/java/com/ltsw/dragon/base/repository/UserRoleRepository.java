package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author heshaobing
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 查询用户角色关系
     *
     * @param userId 用户Id
     * @return
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * 删除用户角色关系
     *
     * @param userId 用户Id
     */
    void deleteByUserId(Long userId);

    /**
     * 删除用户角色关系
     *
     * @param roleId 角色Id
     */
    void deleteByRoleId(Long roleId);

}
