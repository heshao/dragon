package com.ltsw.dragon.base.repository;

import com.ltsw.dragon.base.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author heshaobing
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 分页查询
     *
     * @param name     名称
     * @param pageable 分页
     * @return
     */
    Page<Department> findByNameLike(String name, Pageable pageable);
}
