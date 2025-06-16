package com.dev.moyering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.entity.host.Host;

public interface HostRepository extends JpaRepository<Host, Integer> {

}
