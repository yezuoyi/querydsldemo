package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.demo.entity.Husband;

public interface HusbandRepository extends JpaRepository<Husband, Integer>, QuerydslPredicateExecutor<Husband> {

}
