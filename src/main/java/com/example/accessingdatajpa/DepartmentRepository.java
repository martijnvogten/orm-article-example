package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

	Department findByName(String name);

	Department findById(long id);
}
