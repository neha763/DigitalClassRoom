package com.digital.controller;


import com.digital.dto.IssueDTO;
import com.digital.dto.ReservationDTO;
import com.digital.servicei.IssueService;
import com.digital.servicei.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/librarian")
@RequiredArgsConstructor
public class LibrarianController {

    private final IssueService issueService;
    private final ReservationService reservationService;

    @PostMapping("/issue")
    public IssueDTO issueBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        return issueService.issueBook(bookId, memberId);
    }

    @PostMapping("/return/{issueId}")
    public IssueDTO returnBook(@PathVariable Long issueId) {
        return issueService.returnBook(issueId);
    }

    @PostMapping("/renew/{issueId}")
    public IssueDTO renewBook(@PathVariable Long issueId) {
        return issueService.renewBook(issueId);
    }

    @GetMapping("/issues/member/{memberId}")
    public List<IssueDTO> issuesByMember(@PathVariable Long memberId) {
        return issueService.listIssuesByMember(memberId);
    }

    @GetMapping("/issues/overdue")
    public List<IssueDTO> overdueIssues() {
        return issueService.listOverdueIssues();
    }

    @PostMapping("/reservation/approve/{resId}")
    public ReservationDTO approveRes(@PathVariable Long resId) {
        return reservationService.approveReservation(resId);
    }

    @PostMapping("/reservation/cancel/{resId}")
    public ReservationDTO cancelRes(@PathVariable Long resId) {
        return reservationService.cancelReservation(resId);
    }
}
