package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.Subjects;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectsService {
    private final SubjectsRepository repo;

    public SubjectsService(SubjectsRepository repo) {
        this.repo = repo;
    }

    public List<String> getSubjects() {
        return repo.findById(1L).orElse(new Subjects()).getSubjects();
    }

    @Transactional
    public void updateSubjects(List<String> subjects) {
        final Subjects existingSubjects = repo.findById(1L).orElse(new Subjects());
        existingSubjects.setSubjects(subjects);
        repo.save(existingSubjects);
    }
}
