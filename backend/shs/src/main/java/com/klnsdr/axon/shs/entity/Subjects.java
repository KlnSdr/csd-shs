package com.klnsdr.axon.shs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "shs_subjects")
@Entity
public class Subjects {
    @Id
    @Column(name = "id")
    private Long id = 1L;

    @ElementCollection
    @CollectionTable(
            name = "shs_subjects_entries",
            joinColumns = @JoinColumn(name = "subjects_id")
    )
    @Column(name = "subject")
    private List<String> subjects;
}