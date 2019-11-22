package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.MenuRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    List<MenuRole> findByMenu_Id(long menuId);

    /**
     * 查询菜单角色关系
     *
     * @param roleId 角色Id
     * @return
     */
    List<MenuRole> findByRole_Id(long roleId);

    /**
     * 删除关系
     *
     * @param menuId 菜单ID
     */
    void deleteByMenu_Id(long menuId);

    /**
     * 删除关系
     *
     * @param roleId 角色ID
     */
    void deleteByRole_Id(long roleId);
}
