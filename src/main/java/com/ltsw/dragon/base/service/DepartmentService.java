package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Department;
import com.ltsw.dragon.base.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author heshaobing
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;


    public Optional<Department> get(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return repository.findById(id);
    }

    public Page<Department> findAll(Pageable pageable, String name) {
        if (StringUtils.isEmpty(name)) {
            return repository.findAll(pageable);
        }
        return repository.findByNameLike(name, pageable);
    }

    public Page<Department> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void save(Department department) {
        repository.save(department);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

}
