package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author heshaobing
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {

}
