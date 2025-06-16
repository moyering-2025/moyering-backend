package com.dev.moyering.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.host.entity.Host;

public interface HostRepository extends JpaRepository<Host, Integer> {

}
