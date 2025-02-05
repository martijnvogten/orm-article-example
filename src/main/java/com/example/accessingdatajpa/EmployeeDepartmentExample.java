package com.example.accessingdatajpa;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.persistence.EntityManager;

@SpringBootApplication
public class EmployeeDepartmentExample {

	private static final Logger log = LoggerFactory.getLogger(EmployeeDepartmentExample.class);

	public static void main(String[] args) {
		SpringApplication.run(EmployeeDepartmentExample.class);
	}

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	DepartmentRepository departmentRepository;	

	void addEmployeeToDepartment(String firstName, String lastName, String departmentName) {
		runInTransaction(() -> {
			Department department = departmentRepository.findByName(departmentName);
			
			Employee newEmployee = new Employee(firstName, lastName);
			newEmployee.setDepartment(department);
			employeeRepository.save(newEmployee);
			
			entityManager.flush(); // Need to force save to get the employee to show up in the email :(
			
			sendEmailWithEmployeeList(departmentName);
		});
	}
	
	public void sendEmailWithEmployeeList(String departmentName) {
		Department department = departmentRepository.findByName(departmentName);
		List<Employee> employeesList = department.getEmployees();
		String emailbody = "Employee list of department " + departmentName + ": \n";
		for (Employee emp : employeesList) {
			emailbody += emp.getFirstName() + " " + emp.getLastName() + "\n";
		}
		sendEmail(emailbody);
	}
	
	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			
			createInitialData();
			
			addEmployeeToDepartment("John", "Doe", "Finance");
		};
	}

	private void createInitialData() {
		departmentRepository.save(new Department("Finance"));
	}
	
	private void sendEmail(String emailbody) {
		log.info("""
			
			--------- EMAIL START -------
			{}
			--------- EMAIL END ---------
			""", emailbody);
	}
	
	private void runInTransaction(Runnable runnable) {
		new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
			runnable.run();
		});
	}
}
