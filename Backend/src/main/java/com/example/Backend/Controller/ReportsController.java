package com.example.Backend.controller;

import com.example.Backend.Entity.Reports;
import com.example.Backend.Repository.BorrowedBooksRepository;
import com.example.Backend.service.ReportsService;
import com.example.Backend.Repository.BooksRepository;
import com.example.Backend.Repository.SearchLogsRepository;
import com.example.Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/admin/reports")
public class ReportsController {

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private BooksRepository bookRepository;

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private SearchLogsRepository searchLogsRepository;

    @GetMapping
    public List<Reports> getAllReports() {
        return reportsService.getAllReports();
    }

    @GetMapping("/generate")
    public List<Reports> generateAllReports() {
        return reportsService.generateAllReports();
    }

    @GetMapping("/kpi")
    public Map<String, Object> getKpi() {
        Map<String, Object> kpi = new HashMap<>();
        kpi.put("totalActiveUsers", usersRepository.count());
        kpi.put("totalAvailableBooks", bookRepository.sumCopiesAvailable());
        kpi.put("mostSearchedBook", searchLogsRepository.findMostSearchedBooks().stream().findFirst().orElse("No data"));
        return kpi;
    }

    @GetMapping("/additional-metrics")
    public Map<String, Object> getAdditionalMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalBorrowings", borrowedBooksRepository.count());
        metrics.put("totalOverdueBooks", borrowedBooksRepository.countOverdueBooks(LocalDate.now()));
        metrics.put("totalNotReturnedBooks", borrowedBooksRepository.countNotReturnedBooks());
        return metrics;
    }

}
