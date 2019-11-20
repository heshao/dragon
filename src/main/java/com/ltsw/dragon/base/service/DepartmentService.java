package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Department;
import com.ltsw.dragon.base.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

/**
 * @author heshaobing
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    public Department get(long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Page<Department> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void save(Department department) {
        repository.save(department);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
