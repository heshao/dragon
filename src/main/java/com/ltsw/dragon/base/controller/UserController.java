package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.entity.User;
import com.ltsw.dragon.base.service.RoleService;
import com.ltsw.dragon.base.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author heshaobing
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("get")
    public void get(long id, Model model) {
        User user = userDetailsService.get(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute(user);
    }

    @RequestMapping("edit")
    public void edit(Long id, Model model) {
        User user = userDetailsService.get(id).orElseGet(User::new);
        List<Role> roles = roleService.findAll();
        model.addAttribute(user);
        model.addAttribute(roles);
    }

    @RequestMapping("list")
    public void list(Pageable pageable, Model model) {
        Page<User> page = userDetailsService.findAll(pageable);
        model.addAttribute(page);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity save(User user) {
        userDetailsService.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(long id) {
        userDetailsService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
