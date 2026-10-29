package com.bdo.exam.repository;

import com.bdo.exam.entity.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesReportRepository extends JpaRepository<SalesReport, String> {}
