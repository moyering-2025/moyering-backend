package com.dev.moyering.common.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.moyering.common.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Integer>, AlarmRepositoryCustom {


}
