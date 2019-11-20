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
    List<UserRole> findByUser_Id(long userId);

    /**
     * 删除用户角色关系
     *
     * @param userId 用户Id
     */
    void deleteByUser_Id(long userId);
}
