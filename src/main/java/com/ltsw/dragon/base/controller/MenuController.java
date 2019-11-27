package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.service.MenuService;
import com.ltsw.dragon.base.service.RoleService;
import com.ltsw.dragon.common.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author heshaobing
 */
@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("get")
    public void get(long id, Model model) {
        Menu menu = menuService.get(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute(menu);
    }

    @RequestMapping("edit")
    public void edit(@RequestParam(required = false) Long id, Model model) {
        Menu menu = menuService.get(id).orElseGet(Menu::new);
        List<Role> roles = roleService.findAll();
        model.addAttribute(menu);
        model.addAttribute(roles);
    }

    @RequestMapping("list")
    public void list() {
    }

    @RequestMapping("search")
    @ResponseBody
    public ResponseEntity search(int page, int limit, String name) {

        Page<Menu> menuPage = menuService.findAll(PageRequest.of(page - 1, limit), name);
        return ResponseEntity.success(menuPage);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity save(Menu menu) {
        menuService.save(menu);
        return ResponseEntity.success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(long id) {
        menuService.delete(id);
        return ResponseEntity.success();
    }
}
