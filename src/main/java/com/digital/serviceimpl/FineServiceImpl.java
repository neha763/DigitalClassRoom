package com.digital.serviceimpl;


import com.digital.dto.FineDTO;
import com.digital.entity.FineTransaction;
import com.digital.entity.LibraryMember;
import com.digital.enums.EventType;
import com.digital.enums.FineStatus;
import com.digital.exception.BusinessException;
import com.digital.exception.NotFoundException;
import com.digital.repository.FineTransactionRepository;
import com.digital.repository.LibraryMemberRepository;
import com.digital.servicei.FineService;
import com.digital.servicei.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineTransactionRepository fineRepo;
    private final LibraryMemberRepository memberRepo;
    private final NotificationService notificationService;

    @Override
    public List<FineDTO> listFinesForMember(Long memberId) {
        LibraryMember member = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with id: " + memberId));
        List<FineTransaction> fines = fineRepo.findByMemberAndFineStatus(member, FineStatus.UNPAID);
        return fines.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FineDTO payFine(Long fineId, Long paymentId) {
        FineTransaction fine = fineRepo.findById(fineId)
                .orElseThrow(() -> new NotFoundException("Fine transaction not found with id: " + fineId));
        if (fine.getFineStatus() == FineStatus.PAID) {
            throw new BusinessException("Fine is already paid for id: " + fineId);
        }

        // Integration with Payment module:
        // You must call your Payment service/Client to create payment or verify payment success.
        // Assume that paymentId passed is valid (you may validate in your payment module).
        fine.setFineStatus(FineStatus.PAID);
        fine.setPaidDate(java.time.LocalDate.now());
        fine.setPaymentId(paymentId);
        fine = fineRepo.save(fine);

        // Send notification about fine payment
        notificationService.sendNotification(fine.getMember().getUserId(),
                EventType.FINE_PAYMENT,
                "Fine of ₹" + fine.getFineAmount() + " has been paid successfully.");

        return mapToDTO(fine);
    }

    private FineDTO mapToDTO(FineTransaction fine) {
        return FineDTO.builder()
                .fineId(fine.getFineId())
                .issueId(fine.getIssue().getIssueId())
                .memberId(fine.getMember().getMemberId())
                .fineAmount(fine.getFineAmount())
                .fineReason(fine.getFineReason())
                .fineStatus(fine.getFineStatus().name())
                .paidDate(fine.getPaidDate())
                .build();
    }
}
