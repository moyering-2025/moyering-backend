package com.dev.moyering.admin.repository;

import com.dev.moyering.admin.entity.AdminSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSettlementRepository extends JpaRepository<AdminSettlement, Integer>, AdminSettlementRepositoryCustom {
}
