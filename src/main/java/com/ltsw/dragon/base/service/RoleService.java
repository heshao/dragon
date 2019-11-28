package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.repository.MenuRoleRepository;
import com.ltsw.dragon.base.repository.RoleRepository;
import com.ltsw.dragon.base.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

/**
 * @author heshaobing
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public Optional<Role> get(Long id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Role role) {
        if (exist(role)) {
            throw new EntityExistsException("角色已存在");
        }
        roleRepository.save(role);
    }

    /**
     * 角色是否已存在
     * <p>角色是否被占用</p>
     *
     * @param role 角色
     * @return
     */
    private boolean exist(Role role) {

        // 当前角色是否已存在
        boolean exist = roleRepository.existsByIdAndRole(role.getId(), role.getRole());
        // 忽略当前角色
        if (exist) {
            return false;
        }

        return roleRepository.existsByRole(role.getRole());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        roleRepository.deleteById(id);
        menuRoleRepository.deleteByRoleId(id);
        userRoleRepository.deleteByRoleId(id);
    }
}
