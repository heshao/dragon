package com.ltsw.dragon.base.controller;

import com.ltsw.dragon.base.entity.Department;
import com.ltsw.dragon.base.service.DepartmentService;
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

/**
 * @author heshaobing
 */
@Controller
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("get")
    public void get(Long id, Model model) {
        model.addAttribute(departmentService.get(id).orElseThrow(EntityNotFoundException::new));
    }

    @RequestMapping("list")
    public void list() {
    }

    @RequestMapping("save")
    @ResponseBody
    public ResponseEntity save(Department department) {
        departmentService.save(department);
        return ResponseEntity.success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResponseEntity delete(long id) {
        departmentService.delete(id);
        return ResponseEntity.success();
    }


    @RequestMapping("search")
    @ResponseBody
    public ResponseEntity search(int page, int limit, String name) {
        Page<Department> departmentsPage = departmentService.findAll(PageRequest.of(page - 1, limit), name);
        return ResponseEntity.success(departmentsPage);
    }


    @RequestMapping("edit")
    public void edit(@RequestParam(required = false) Long id, Model model) {
        Department department = departmentService.get(id).orElseGet(Department::new);
        model.addAttribute(department);
    }

}
