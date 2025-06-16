package com.dev.moyering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.entity.host.HostClass;

public interface ClassRepository extends JpaRepository<HostClass, Integer> {

}
