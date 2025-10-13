package com.digital.repository;

import com.digital.entity.BookIssue;
import com.digital.entity.LibraryMember;

import com.digital.enums.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
    List<BookIssue> findByMemberAndStatus(LibraryMember member, BookIssue.IssueStatus status);
    List<BookIssue> findByStatus(BookIssue.IssueStatus status);


    List<BookIssue> findByMemberAndStatus(LibraryMember mem, IssueStatus issueStatus);
}
