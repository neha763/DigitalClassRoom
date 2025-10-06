package com.digital.serviceimpl;

import com.digital.dto.SubmissionResponse;
import com.digital.entity.Assignment;
import com.digital.entity.AssignmentSubmission;
import com.digital.enums.SubmissionStatus;
import com.digital.exception.ResourceNotFoundException;
import com.digital.exception.SubmissionAlreadyExistsException;
import com.digital.exception.FileUploadException;
import com.digital.repository.AssignmentRepository;
import com.digital.repository.AssignmentSubmissionRepository;
import com.digital.servicei.AssignmentSubmissionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentSubmissionRepository submissionRepository;

    // Convert MultipartFile to Blob
    private Blob convertMultipartToBlob(MultipartFile file) throws FileUploadException {
        try {
            byte[] bytes = file.getBytes();
            return new SerialBlob(bytes);
        } catch (Exception e) {
            throw new FileUploadException("Could not convert file to blob", e);
        }
    }

    @Override
    @Transactional
    public SubmissionResponse submitAssignment(Long studentId, Long assignmentId, MultipartFile file)
            throws ResourceNotFoundException, SubmissionAlreadyExistsException, FileUploadException, SQLException {

        // Check if assignment exists
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        // Check if student has already submitted
        boolean exists = submissionRepository.existsByAssignment_AssignmentIdAndStudentId(assignmentId, studentId);
        if (exists) {
            throw new SubmissionAlreadyExistsException("Submission already exists for assignment " + assignmentId + " and student " + studentId);
        }

        // Convert file
        Blob fileBlob = convertMultipartToBlob(file);

        // Build submission with default feedback & marks
        AssignmentSubmission submission = AssignmentSubmission.builder()
                .assignment(assignment)
                .studentId(studentId)
                .fileUrl(fileBlob)
                .submittedAt(LocalDateTime.now())
                .status(SubmissionStatus.SUBMITTED)
                .marks(0.0)  // Default mark
                .feedback("Pending review")  // Default feedback
                .build();

        AssignmentSubmission saved = submissionRepository.save(submission);

        // Map to response
        SubmissionResponse resp = SubmissionResponse.builder()
                .submissionId(saved.getSubmissionId())
                .assignmentId(saved.getAssignment().getAssignmentId())
                .studentId(saved.getStudentId())
                .fileUrl(saved.getFileUrl().getBytes(1, (int) saved.getFileUrl().length()))
                .submittedAt(saved.getSubmittedAt())
                .status(saved.getStatus())
                .marks(saved.getMarks())
                .feedback(saved.getFeedback())
                .build();

        return resp;
    }

    @Override
    public SubmissionResponse getSubmission(Long submissionId) throws SQLException {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with ID: " + submissionId));

        SubmissionResponse resp = SubmissionResponse.builder()
                .submissionId(submission.getSubmissionId())
                .assignmentId(submission.getAssignment().getAssignmentId())
                .studentId(submission.getStudentId())
                .fileUrl(submission.getFileUrl().getBytes(1, (int) submission.getFileUrl().length()))
                .submittedAt(submission.getSubmittedAt())
                .status((submission.getStatus()))
                .marks(submission.getMarks())
                .feedback(submission.getFeedback())
                .build();

        return resp;
    }

    @Override
    public SubmissionResponse updateSubmissionFile(Long submissionId, MultipartFile file) throws ResourceNotFoundException, FileUploadException, Exception {
        return null;
    }

    @Override
    @Transactional
    public SubmissionResponse updateFeedbackAndMarks(Long submissionId, String feedback, Double marks) throws ResourceNotFoundException, Exception {
        // 1. Load the existing submission (managed entity)
        AssignmentSubmission existing = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with ID: " + submissionId));

        // 2. Update only the fields you want (feedback, marks, status)
        existing.setFeedback(feedback);
        existing.setMarks(marks);
        // Optionally change status
        existing.setStatus(SubmissionStatus. SUBMITTED);

        // 3. Save — since `existing` is managed, Hibernate will do an UPDATE, not INSERT
        AssignmentSubmission updated = submissionRepository.save(existing);

        // 4. Build the response DTO
        return SubmissionResponse.builder()
                .submissionId(updated.getSubmissionId())
                .assignmentId(updated.getAssignment().getAssignmentId())
                .studentId(updated.getStudentId())
                .fileUrl(updated.getFileUrl().getBytes(1, (int) updated.getFileUrl().length()))
                .submittedAt(updated.getSubmittedAt())
                .status(updated.getStatus())
                .marks(updated.getMarks())
                .feedback(updated.getFeedback())
                .build();
    }

   @Transactional
   @Override
   public SubmissionResponse updateSubmission(Long submissionId, MultipartFile file) throws SQLException {

        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with ID: " + submissionId));

        // Update file
        if (file != null && !file.isEmpty()) {
            Blob fileBlob = convertMultipartToBlob(file);
            submission.setFileUrl(fileBlob);
            submission.setSubmittedAt(LocalDateTime.now());
        }


        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setMarks(0.0);
        submission.setFeedback("Pending review");

        AssignmentSubmission updated = submissionRepository.save(submission);

        SubmissionResponse resp = SubmissionResponse.builder()
                .submissionId(updated.getSubmissionId())
                .assignmentId(updated.getAssignment().getAssignmentId())
                .studentId(updated.getStudentId())
                .fileUrl(updated.getFileUrl().getBytes(1, (int) updated.getFileUrl().length()))
                .submittedAt(updated.getSubmittedAt())
                .status(updated.getStatus())
                .marks(updated.getMarks())
                .feedback(updated.getFeedback())
                .build();

        return resp;
    }

    @Override
    @Transactional
    public void deleteSubmission(Long submissionId) throws ResourceNotFoundException {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with ID: " + submissionId));
        submissionRepository.delete(submission);
    }

}
