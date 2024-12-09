package com.example.Backend.service.IMPL;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.Entity.Reports;
import com.example.Backend.Repository.BorrowedBooksRepository;
import com.example.Backend.Repository.ReportsRepository;
import com.example.Backend.Repository.SearchLogsRepository;
import com.example.Backend.Repository.UserRepository;
import com.example.Backend.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private ReportsRepository reportsRepository;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private SearchLogsRepository searchLogsRepository;

    @Autowired
    private UserRepository usersRepository;

    @Override
    public List<Reports> getAllReports() {
        return reportsRepository.findAll();
    }

    @Override
    public List<Reports> generateAllReports() {
        List<Reports> generatedReports = new ArrayList<>();

        generatedReports.add(generateReport("PopularBooks", "System"));
        generatedReports.add(generateReport("OverdueRecords", "System"));
        generatedReports.add(generateReport("MostSearchedBook", "System"));

        reportsRepository.saveAll(generatedReports);

        return generatedReports;
    }

    public Reports generateReport(String reportType, String generatedBy) {
        Reports report = new Reports();
        report.setReportType(reportType);
        report.setGeneratedBy(generatedBy);
        report.setDateGenerated(LocalDate.now());

        switch (reportType) {
            case "PopularBooks":
                report.setData("Report on popular books...");
                break;
            case "OverdueRecords":
                report.setData("Report on overdue records...");
                break;
            case "MostSearchedBook":
                report.setData("Report on most searched books...");
                break;
            default:
                report.setData("Default report data...");
        }
        return report;
    }
    private String getPopularBooksReport() {
        List<String> popularBooks = borrowedBooksRepository.findPopularBooks();
        return String.join("\n", popularBooks);
    }

    private String getOverdueRecordsReport() {
        List<BorrowedBooks> overdueRecords = borrowedBooksRepository.findOverdueRecords(LocalDate.now());
        return overdueRecords.stream()
                .map(record -> "User: " + record.getUserEmail() + ", Book: " + record.getBookIsbn() + ", Due: " + record.getDueDate())
                .collect(Collectors.joining("\n"));
    }

    private String getMostSearchedBooksReport() {
        List<String> mostSearchedBooks = searchLogsRepository.findMostSearchedBooks();
        return String.join("\n", mostSearchedBooks);
    }
}
