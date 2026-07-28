package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectsRepository extends JpaRepository<Subjects, Long> {
}
