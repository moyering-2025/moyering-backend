package com.dev.moyering.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.HostClass;

public interface HostClassRepository extends JpaRepository<HostClass, Integer>, HostClassRepositoryCustom {

}
