package de.dps.springbatch.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDateTime birthdate;

}