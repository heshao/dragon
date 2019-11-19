package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.MenuRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author heshaobing
 */
@Repository
public interface MenuRoleRepository extends JpaRepository<MenuRole, Long> {

    /**
     * 查询菜单角色关系
     *
     * @param menuId 菜单Id
     * @return
     */
    List<MenuRole> findByMenu_Id(long menuId);

}
