package com.dev.moyering.socialing.repository;

import com.dev.moyering.socialing.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Integer>, ScrapRepositoryCustom {
}
