package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.entity.User;
import com.ltsw.dragon.base.entity.UserRole;
import com.ltsw.dragon.base.repository.UserRepository;
import com.ltsw.dragon.base.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author heshaobing
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByUsername(username);
        load(optional);
        return optional.orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    public Optional<User> get(Long id) {
        Optional<User> optional = userRepository.findById(id);
        load(optional);
        return optional;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 加载角色信息
     *
     * @param optional
     */
    private void load(Optional<User> optional) {
        optional.ifPresent(user -> {
            List<Role> roles = new ArrayList<>();
            List<UserRole> list = userRoleRepository.findByUser_Id(user.getId());
            list.forEach(userRole -> roles.add(userRole.getRole()));
            user.setAuthorities(roles);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        if (exist(user)) {
            throw new EntityExistsException("用户已存在");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        userRoleRepository.deleteByUser_Id(user.getId());
        Collection<? extends GrantedAuthority> roles = user.getAuthorities();
        if (!roles.isEmpty()) {
            roles.forEach(o -> {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole((Role) o);
                userRoleRepository.save(userRole);
            });
        }
    }

    /**
     * 用户是否已存在
     * <p>判断用户名是否被占用</p>
     *
     * @param user 用户
     * @return
     */
    private boolean exist(User user) {

        // 当前用户是否已存在
        boolean exist = userRepository.existsByIdAndUsername(user.getId(), user.getUsername());
        // 忽略当前用户
        if (exist) {
            return false;
        }

        return userRepository.existsByUsername(user.getUsername());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        userRepository.deleteById(id);
        userRoleRepository.deleteByUser_Id(id);
    }
}
