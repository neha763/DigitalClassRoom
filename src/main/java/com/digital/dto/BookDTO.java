package com.digital.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String category;
    private String edition;
    private Integer totalCopies;
    private Integer availableCopies;
    private String shelfNumber;
    private LocalDate addedDate;
    private String status;
}
