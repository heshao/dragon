package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.MenuRole;
import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.repository.MenuRepository;
import com.ltsw.dragon.base.repository.MenuRoleRepository;
import com.ltsw.dragon.base.security.SecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author heshaobing
 */
@Service
@Transactional(readOnly = true)
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;
    @Autowired
    private SecurityMetadataSource metadataSource;
    /**
     * 初始化加载菜单权限
     */
    @PostConstruct
    public void init() {

        List<MenuRole> menuRoles = menuRoleRepository.findAll();
        metadataSource.put(menuRoles);

    }

    public Optional<Menu> get(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<Menu> optional = menuRepository.findById(id);
        optional.ifPresent(menu -> {
            List<MenuRole> menuRoles = menuRoleRepository.findByMenuId(id);
            List<Role> roles = new ArrayList<>();
            menuRoles.forEach(menuRole -> roles.add(menuRole.getRole()));
            menu.setRoles(roles);
        });
        return optional;
    }

    /**
     * 分页查询菜单
     *
     * @param pageable 分页
     * @param name   菜单名称
     * @return
     */
    public Page<Menu> findAll(Pageable pageable, String name) {
        if (StringUtils.isEmpty(name)) {
            return menuRepository.findAll(pageable);
        }
        return menuRepository.findByNameLike(name, pageable);
    }

    /**
     * 获取当前用户已授权菜单
     * <p>可用、可见</p>
     *
     * @return
     */
    public List<Menu> findAllWithGranted() {

        Menu filter = new Menu();
        filter.setEnabled(true);
        filter.setVisible(true);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("sort");
        return menuRepository.findAll(Example.of(filter, matcher)).stream().filter(menu -> {
            if (StringUtils.isEmpty(menu.getUri())) {
                return true;
            }
            return metadataSource.decide(menu.getUri());
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Menu menu) {

        menuRepository.save(menu);
        List<MenuRole> menuRoles = menuRoleRepository.findByMenuId(menu.getId());
        metadataSource.put(menuRoles);
        throw new RuntimeException();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Optional<Menu> optional = menuRepository.findById(id);
        optional.ifPresent(menu -> {
            menuRepository.delete(menu);
            menuRoleRepository.deleteByMenuId(id);
            metadataSource.remove(menu);
        });

        throw new RuntimeException();
    }

    /**
     * 获取菜单名称
     *
     * @param uri 路径
     * @return
     */
    public String getNameByUri(String uri) {
        Optional<Menu> optional = menuRepository.findFirstByUri(uri);
        String name = optional.orElseGet(Menu::new).getName();
        return StringUtils.isEmpty(name) ? uri : name;
    }


}
