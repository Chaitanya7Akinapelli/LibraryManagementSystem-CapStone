package com.example.Backend.service.IMPL;

import com.example.Backend.Entity.BorrowedBooks;
import com.example.Backend.Entity.Return;
import com.example.Backend.Repository.BorrowedBooksRepository;
import com.example.Backend.Repository.ReturnRepository;
import com.example.Backend.service.UserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserStatisticsServiceImpl implements UserStatisticsService {

    @Autowired
    private BorrowedBooksRepository borrowedBooksRepository;

    @Autowired
    private ReturnRepository returnRepository;

    @Override
    public Map<String, Object> getUserBorrowingHistory(String userEmail) {
        List<BorrowedBooks> borrowedBooks = borrowedBooksRepository.findByUserEmail(userEmail);

        List<Return> returns = returnRepository.findByUserEmail(userEmail);

        List<Object[]> mostBorrowedBookResults = borrowedBooksRepository.findMostBorrowedBookByUser(userEmail);
        String mostBorrowedBook = mostBorrowedBookResults.isEmpty() ? "N/A" : (String) mostBorrowedBookResults.get(0)[0];

        double totalPenaltiesPaid = returns.stream()
                .mapToDouble(Return::getPenaltyAmount)
                .sum();

        List<Map<String, Object>> borrowingHistory = borrowedBooks.stream()
                .map(b -> {
                    Map<String, Object> history = new HashMap<>();
                    history.put("bookIsbn", b.getBookIsbn());
                    history.put("borrowDate", b.getBorrowDate());
                    history.put("dueDate", b.getDueDate());
                    history.put("returnDate", returns.stream()
                            .filter(r -> r.getBookIsbn().equals(b.getBookIsbn()))
                            .map(Return::getReturnDate)
                            .findFirst().orElse(null));
                    history.put("penaltyPaid", returns.stream()
                            .filter(r -> r.getBookIsbn().equals(b.getBookIsbn()))
                            .map(Return::getPenaltyAmount)
                            .findFirst().orElse(0.0));
                    return history;
                })
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("borrowingHistory", borrowingHistory);
        result.put("mostBorrowedBook", mostBorrowedBook);
        result.put("totalPenaltiesPaid", totalPenaltiesPaid);

        return result;
    }
}
