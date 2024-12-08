package com.example.Backend.service;

import com.example.Backend.Entity.Reports;

import java.util.List;

public interface ReportsService {

    List<Reports> getAllReports();

    List<Reports> generateAllReports();

    Reports generateReport(String reportType, String generatedBy);
}
