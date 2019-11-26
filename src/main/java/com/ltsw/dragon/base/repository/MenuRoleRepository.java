package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.MenuRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author heshaobing
 */
@Repository
public interface MenuRoleRepository extends JpaRepository<MenuRole, Long> {

    /**
     * 关联查询所有菜单角色关系
     *
     * @return
     */
    @Override
    @EntityGraph(value = "withAll")
    List<MenuRole> findAll();

    /**
     * 查询菜单角色关系
     *
     * @param menuId 菜单Id
     * @return
     */
    List<MenuRole> findByMenuId(long menuId);

    /**
     * 查询菜单角色关系
     *
     * @param roleId 角色Id
     * @return
     */
    List<MenuRole> findByRoleId(long roleId);

    /**
     * 删除关系
     *
     * @param menuId 菜单ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "delete from menu_role where menu_id = ?1 ", nativeQuery = true)
    void deleteByMenuId(long menuId);

    /**
     * 删除关系
     *
     * @param roleId 角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "delete from menu_role where role_id = ?1 ", nativeQuery = true)
    void deleteByRoleId(Long roleId);
}
