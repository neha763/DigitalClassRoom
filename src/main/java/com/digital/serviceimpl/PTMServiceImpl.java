package com.digital.serviceimpl;

import com.digital.dto.PTMRequest;
import com.digital.dto.PTMResponse;
import com.digital.entity.*;
import com.digital.enums.NotificationType;
import com.digital.enums.PTMStatus;
import com.digital.events.PTMScheduledEvent;
import com.digital.repository.*;
import com.digital.servicei.GoogleMeetService;
import com.digital.servicei.PTMService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service // <- make sure this is present
@Transactional
@RequiredArgsConstructor // <- generates constructor for final fields
public class PTMServiceImpl implements PTMService {

    private final PTMRepository ptmRepository;
    private final StudentRepository studentRepository; // <- injected automatically
    private final ParentStudentMappingRepository parentStudentMappingRepository;
    private final ExamNotificationRepository examNotificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GoogleMeetService googleMeetService;

    @Transactional
    @Override
    public PTMResponse schedulePTM(PTMRequest request) {
        // Fetch students
        List<Student> students = studentRepository.findAllById(request.getStudentIds());

        // Create Google Meet link
        String meetLink = null;
        try {
            meetLink = googleMeetService.createGoogleMeetLink(
                    request.getTitle(),
                    request.getMeetingDateTime(),
                    request.getDurationMinutes()
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        // Create and save PTM
        PTM ptm = PTM.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .meetingDateTime(request.getMeetingDateTime())
                .durationMinutes(request.getDurationMinutes())
                .type(request.getType())
                .venue(request.getVenue())
                .joinLink(meetLink)
                .status(PTMStatus.SCHEDULED)
                .students(students)
                .build();

        ptm = ptmRepository.save(ptm);

        // Publish event for notifications
        List<Long> studentIds = students.stream()
                .map(Student::getStudentRegId)
                .toList();
        eventPublisher.publishEvent(new PTMScheduledEvent(ptm, studentIds));

        // Build and return response
        return PTMResponse.builder()
                .ptmId(ptm.getPtmId())
                .title(ptm.getTitle())
                .description(ptm.getDescription())
                .meetingDateTime(ptm.getMeetingDateTime())
                .durationMinutes(ptm.getDurationMinutes())
                .type(ptm.getType())
                .venue(ptm.getVenue())
                .joinLink(ptm.getJoinLink())
                .status(ptm.getStatus())
                .studentIds(studentIds)
                .build();
    }


    @Override
    public List<PTMResponse> getAllPTMs() {
        return ptmRepository.findAll().stream().map(ptm -> {
            List<Long> studentIds = ptm.getStudents()
                    .stream()
                    .map(Student::getStudentId)
                    .collect(Collectors.toList());

            return PTMResponse.builder()
                    .ptmId(ptm.getPtmId())
                    .title(ptm.getTitle())
                    .description(ptm.getDescription())
                    .meetingDateTime(ptm.getMeetingDateTime())
                    // .durationMinutes(ptm.getDurationMinutes())
                    .type(ptm.getType())
                    .venue(ptm.getVenue())
                    .status(ptm.getStatus())
                    .joinLink(ptm.getJoinLink())
                    .studentIds(studentIds)
                    .build();
        }).collect(Collectors.toList());
    }
}

