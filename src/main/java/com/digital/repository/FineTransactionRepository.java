package com.digital.repository;

import com.digital.entity.FineTransaction;
import com.digital.entity.LibraryMember;

import com.digital.enums.FineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineTransactionRepository extends JpaRepository<FineTransaction, Long> {
    List<FineTransaction> findByMemberAndFineStatus(LibraryMember member, FineStatus fineStatus);


}
