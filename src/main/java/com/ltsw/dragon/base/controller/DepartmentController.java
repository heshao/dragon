package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Department;
import com.ltsw.dragon.base.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author heshaobing
 */
@Controller
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("get")
    public void get(long id, Model model) {
        model.addAttribute(departmentService.get(id));
    }

    @RequestMapping("list")
    public void list(@PageableDefault Pageable pageable, Model model) {
        Page<Department> page = departmentService.findAll(pageable);
        model.addAttribute(page);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity save(Department department) {
        departmentService.save(department);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(long id) {
        departmentService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
