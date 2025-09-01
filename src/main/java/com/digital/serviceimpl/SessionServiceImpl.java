package com.digital.serviceimpl;

import com.digital.dto.SessionRequest;
import com.digital.entity.SchoolClass;
import com.digital.entity.Section;
import com.digital.entity.Session;
import com.digital.entity.Teacher;
import com.digital.exception.ResourceNotFoundException;
import com.digital.repository.ClassRepository;
import com.digital.repository.SectionRepository;
import com.digital.repository.SessionRepository;
import com.digital.repository.TeacherRepository;
import com.digital.servicei.SessionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final ClassRepository classRepository;
    private final SectionRepository sectionRepository;
    private final TeacherRepository teacherRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, ClassRepository classRepository, SectionRepository sectionRepository, TeacherRepository teacherRepository) {
        this.sessionRepository = sessionRepository;
        this.classRepository = classRepository;
        this.sectionRepository = sectionRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public String createSession(SessionRequest sessionRequest) {

        SchoolClass schoolClass = classRepository.findById(sessionRequest.getSchoolClass().getClassId()).orElseThrow(() ->
                new ResourceNotFoundException("School class with ID: " + sessionRequest.getSchoolClass().getClassId() + " not found in database."));

        Section section = sectionRepository.findById(sessionRequest.getSection().getSectionId()).orElseThrow(() ->
                new ResourceNotFoundException("Section with ID: " + sessionRequest.getSection().getSectionId() + " not found in database."));

        Teacher teacher = teacherRepository.findById(sessionRequest.getTeacher().getId()).orElseThrow(() ->
                new ResourceNotFoundException("Teacher with ID: " + sessionRequest.getTeacher().getId() + " not found in database."));

        Session session = Session.builder()
                .schoolClass(schoolClass)
                .section(section)
                .teacher(teacher)
                .date(sessionRequest.getDate())
                .startTime(sessionRequest.getStartTime())
                .endTime(sessionRequest.getEndTime())
                .topic(sessionRequest.getTopic())
                .description(sessionRequest.getDescription())
                .build();

        sessionRepository.save(session);

        return "Session saved successfully";
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
}
