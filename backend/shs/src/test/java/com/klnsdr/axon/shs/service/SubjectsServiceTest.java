package com.klnsdr.axon.shs.service;


import com.klnsdr.axon.shs.entity.Subjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubjectsServiceTest {
    private SubjectsService subjectsService;
    private SubjectsRepository mockSubjectsRepository;

    @BeforeEach
    public void setUp() {
        mockSubjectsRepository = mock(SubjectsRepository.class);
        subjectsService = new SubjectsService(mockSubjectsRepository);
    }

    @Test
    public void getSubjectsReturnsListWhenSubjectsExist() {
        final List<String> expectedSubjects = List.of("Math", "Physics", "Chemistry");
        final Subjects subjects = new Subjects();
        subjects.setId(1L);
        subjects.setSubjects(expectedSubjects);

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.of(subjects));

        final List<String> result = subjectsService.getSubjects();

        assertEquals(expectedSubjects, result);
        verify(mockSubjectsRepository, times(1)).findById(1L);
    }

    @Test
    public void getSubjectsReturnsEmptyListWhenSubjectsNotFound() {
        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.empty());

        final List<String> result = subjectsService.getSubjects();

        assertNull(result);
        verify(mockSubjectsRepository, times(1)).findById(1L);
    }

    @Test
    public void getSubjectsReturnsEmptyListWhenSubjectsPropertyIsNull() {
        final Subjects subjects = new Subjects();
        subjects.setId(1L);
        subjects.setSubjects(null);

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.of(subjects));

        final List<String> result = subjectsService.getSubjects();

        assertNull(result);
        verify(mockSubjectsRepository, times(1)).findById(1L);
    }

    @Test
    public void updateSubjectsCreatesAndSavesNewEntityWhenNotFound() {
        final List<String> newSubjects = List.of("Biology", "History");

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.empty());
        when(mockSubjectsRepository.save(any(Subjects.class))).thenAnswer(invocation -> invocation.getArgument(0));

        subjectsService.updateSubjects(newSubjects);

        verify(mockSubjectsRepository, times(1)).findById(1L);
        verify(mockSubjectsRepository, times(1)).save(argThat(subjects ->
                subjects.getSubjects().equals(newSubjects)
        ));
    }

    @Test
    public void updateSubjectsModifiesAndSavesExistingEntity() {
        final List<String> existingSubjects = List.of("Math", "Physics");
        final List<String> updatedSubjects = List.of("English", "Geography");

        final Subjects subjects = new Subjects();
        subjects.setId(1L);
        subjects.setSubjects(existingSubjects);

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.of(subjects));
        when(mockSubjectsRepository.save(any(Subjects.class))).thenReturn(subjects);

        subjectsService.updateSubjects(updatedSubjects);

        verify(mockSubjectsRepository, times(1)).findById(1L);
        verify(mockSubjectsRepository, times(1)).save(argThat(s ->
                s.getSubjects().equals(updatedSubjects)
        ));
    }

    @Test
    public void updateSubjectsWithEmptyList() {
        final List<String> emptySubjects = List.of();

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.empty());
        when(mockSubjectsRepository.save(any(Subjects.class))).thenAnswer(invocation -> invocation.getArgument(0));

        subjectsService.updateSubjects(emptySubjects);

        verify(mockSubjectsRepository, times(1)).findById(1L);
        verify(mockSubjectsRepository, times(1)).save(argThat(subjects ->
                subjects.getSubjects().equals(emptySubjects)
        ));
    }

    @Test
    public void updateSubjectsWithNullReplacesPreviousSubjects() {
        final List<String> previousSubjects = List.of("Math", "Physics");
        final Subjects subjects = new Subjects();
        subjects.setId(1L);
        subjects.setSubjects(previousSubjects);

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.of(subjects));
        when(mockSubjectsRepository.save(any(Subjects.class))).thenReturn(subjects);

        subjectsService.updateSubjects(null);

        verify(mockSubjectsRepository, times(1)).findById(1L);
        verify(mockSubjectsRepository, times(1)).save(argThat(s ->
                s.getSubjects() == null
        ));
    }

    @Test
    public void updateSubjectsWithSingleSubject() {
        final List<String> singleSubject = List.of("Art");

        when(mockSubjectsRepository.findById(1L)).thenReturn(Optional.empty());
        when(mockSubjectsRepository.save(any(Subjects.class))).thenAnswer(invocation -> invocation.getArgument(0));

        subjectsService.updateSubjects(singleSubject);

        verify(mockSubjectsRepository, times(1)).findById(1L);
        verify(mockSubjectsRepository, times(1)).save(argThat(subjects ->
                subjects.getSubjects().size() == 1 && subjects.getSubjects().getFirst().equals("Art")
        ));
    }
}


