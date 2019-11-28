package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author heshaobing
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {

    /**
     * 分页查询菜单
     *
     * @param name     菜单名称
     * @param pageable 分页
     * @return
     */
    Page<Menu> findByNameLike(String name, Pageable pageable);

    /**
     * 获取菜单
     *
     * @param uri 路径
     * @return
     */
    Optional<Menu> findFirstByUri(String uri);
}
