package com.digital.servicei;

import com.digital.dto.FeeStructureDTO;
import java.util.List;

public interface FeeService {

    FeeStructureDTO createFeeStructure(FeeStructureDTO dto);

    FeeStructureDTO getFeeStructureByClass(Long classId, String academicYear);

    List<FeeStructureDTO> getAllFeeStructures();

    FeeStructureDTO updateFeeStructure(Long id, FeeStructureDTO dto);

    void deleteFeeStructure(Long id);
}
