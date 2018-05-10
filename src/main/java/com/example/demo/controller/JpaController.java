package com.example.demo.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Customer;
import com.example.demo.entity.QCustomer;
import com.example.demo.repository.CustomerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Controller
public class JpaController {
	// private final Logger logger = Logger.getLogger(JpaController.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CustomerRepository cr;

	private JPAQueryFactory queryFactory;

	@PostConstruct
	public void init() {
		queryFactory = new JPAQueryFactory(entityManager);
	}

	@RequestMapping("/getBob")
	@ResponseBody
	public Customer getBob() {
		QCustomer customer = QCustomer.customer;
		Customer bob = queryFactory.selectFrom(customer).where(customer.firstName.eq("Bob")).fetchOne();
		return bob;
	}

	@RequestMapping("/getAll")
	@ResponseBody
	public List<Customer> getAll() {
		return cr.findAll();
	}


	@RequestMapping("/getOrderAll")
	@ResponseBody
	public List<Customer> getOrderAll(){
		QCustomer customer = QCustomer.customer;
		return queryFactory.selectFrom(customer)
		.orderBy(customer.lastName.asc(), customer.firstName.desc())
		.fetch();
	}
	@RequestMapping("/getLastName")
	@ResponseBody
	public List<String> getLastName(){
		QCustomer customer = QCustomer.customer;
	 return	queryFactory.select(customer.lastName).from(customer)
		.groupBy(customer.lastName)
		.fetch();
	}
	
	@RequestMapping("/findById")
	@ResponseBody
	public Object getCumtomerById(Long id) {
		return cr.findById(id);
	}
}
