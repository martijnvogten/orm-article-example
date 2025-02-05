package com.example.accessingdatajpa;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	@OneToMany(mappedBy = "department")
	private List<Employee> employees;

	protected Department() {}

	public Department(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format(
				"Department[id=%d, name='%s']",
				id, name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}
}
