package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.demo.entity.User;


/**
 * 联合使用jpa 与 dsl查询
 * @author yzy
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {

}
