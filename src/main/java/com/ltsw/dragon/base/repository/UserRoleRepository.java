package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author heshaobing
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
