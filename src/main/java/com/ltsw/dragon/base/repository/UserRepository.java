package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author heshaobing
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 获取用户信息
     * @param username 用户名
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * 用户是否存在
     *
     * @param id       id
     * @param username 用户名
     * @return
     */
    boolean existsByIdAndUsername(Long id, String username);

    /**
     * 用户是否存在
     *
     * @param username 用户名
     * @return
     */
    boolean existsByUsername(String username);
}
