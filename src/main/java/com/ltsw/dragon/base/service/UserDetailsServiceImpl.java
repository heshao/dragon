package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.entity.User;
import com.ltsw.dragon.base.entity.UserRole;
import com.ltsw.dragon.base.repository.UserRepository;
import com.ltsw.dragon.base.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        optional.ifPresent(user -> {
            List<Role> roles = new ArrayList<>();
            List<UserRole> list = userRoleRepository.findByUser_Id(user.getId());
            list.forEach(userRole -> roles.add(userRole.getRole()));
            user.setAuthorities(roles);
        });
        return optional.orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
