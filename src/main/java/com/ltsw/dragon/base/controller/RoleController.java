package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.service.RoleService;
import com.ltsw.dragon.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;

/**
 * @author heshaobing
 */
@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("get")
    public void get(long id, Model model) {
        Role role = roleService.get(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute(role);
    }

    @RequestMapping("edit")
    public void edit(@RequestParam(required = false) Long id, Model model) {
        Role role = roleService.get(id).orElseGet(Role::new);
        model.addAttribute(role);
    }

    @RequestMapping("list")
    public void list() {

    }

    /**
     * 权限配置
     *
     * @param id    角色Id
     * @param model
     */
    @RequestMapping("config")
    public void config(long id, Model model) {

    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity save(Role role) {
        roleService.save(role);
        return ResponseEntity.success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(long id) {
        roleService.delete(id);
        return ResponseEntity.success();
    }


    @RequestMapping("search")
    @ResponseBody
    public ResponseEntity search(int page, int limit, String name) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "id");
        Page<Role> rolesPage = roleService.findAll(pageable, name);
        return ResponseEntity.success(rolesPage);
    }
}
