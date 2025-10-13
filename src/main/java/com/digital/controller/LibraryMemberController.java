package com.digital.controller;


import com.digital.dto.BookDTO;
import com.digital.dto.FineDTO;
import com.digital.dto.IssueDTO;
import com.digital.dto.ReservationDTO;
import com.digital.servicei.BookService;
import com.digital.servicei.FineService;
import com.digital.servicei.IssueService;
import com.digital.servicei.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
@RequiredArgsConstructor
public class LibraryMemberController {

    private final BookService bookService;
    private final IssueService issueService;
    private final FineService fineService;
    private final ReservationService reservationService;

    @GetMapping("/books")
    public List<BookDTO> searchBooks(@RequestParam(required = false) String title,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String category) {
        return bookService.searchBooks(title, author, category);
    }

    @PostMapping("/reserve")
    public ReservationDTO reserveBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        return reservationService.reserveBook(bookId, memberId);
    }

    @GetMapping("/my-issues")
    public List<IssueDTO> myIssues(@RequestParam Long memberId) {
        return issueService.listIssuesByMember(memberId);
    }

    @PostMapping("/renew/{issueId}")
    public IssueDTO renew(@PathVariable Long issueId) {
        return issueService.renewBook(issueId);
    }

    @GetMapping("/fines")
    public List<FineDTO> getFines(@RequestParam Long memberId) {
        return fineService.listFinesForMember(memberId);
    }

    @PostMapping("/fines/pay")
    public FineDTO payFine(@RequestParam Long fineId, @RequestParam Long paymentId) {
        return fineService.payFine(fineId, paymentId);
    }
}
