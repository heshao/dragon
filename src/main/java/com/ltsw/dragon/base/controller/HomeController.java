package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.service.MenuService;
import com.ltsw.dragon.common.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author heshaobing
 */
@Slf4j
@Controller
public class HomeController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/")
    public String index(Model model) {
        List<Menu> menus = menuService.findAllWithGranted();
        TreeUtil.parse(menus);
        log.info(menus.toString());
        model.addAttribute(menus);
        return "index";
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
