package com.example.webapp.repository;

import com.example.webapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for StudentRepository
 * 
 * Testing Strategy:
 * - Uses @DataJpaTest for JPA-focused testing
 * - Uses H2 in-memory database
 * - Tests database interactions and JPA operations
 * - Validates CRUD operations
 * - Tests transactional behavior
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository Database Tests")
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent1;
    private Student testStudent2;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        studentRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Arrange: Setup test data
        testStudent1 = new Student();
        testStudent1.setName("John Doe");
        testStudent1.setRoll("CSE001");

        testStudent2 = new Student();
        testStudent2.setName("Jane Smith");
        testStudent2.setRoll("CSE002");
    }

    // ==================== save() Tests ====================

    @Test
    @DisplayName("Should save student successfully and generate ID")
    void shouldSaveStudent_AndGenerateId() {
        // Act
        Student savedStudent = studentRepository.save(testStudent1);

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getId()).isGreaterThan(0L);
        assertThat(savedStudent.getName()).isEqualTo("John Doe");
        assertThat(savedStudent.getRoll()).isEqualTo("CSE001");
    }

    @Test
    @DisplayName("Should persist student to database")
    void shouldPersistStudent_ToDatabase() {
        // Act
        Student savedStudent = studentRepository.save(testStudent1);
        entityManager.flush();
        entityManager.clear();

        // Verify by fetching from database
        Student foundStudent = entityManager.find(Student.class, savedStudent.getId());

        // Assert
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getId()).isEqualTo(savedStudent.getId());
        assertThat(foundStudent.getName()).isEqualTo("John Doe");
        assertThat(foundStudent.getRoll()).isEqualTo("CSE001");
    }

    @Test
    @DisplayName("Should update existing student")
    void shouldUpdateExistingStudent() {
        // Arrange
        Student savedStudent = studentRepository.save(testStudent1);
        entityManager.flush();
        entityManager.clear();

        // Act
        savedStudent.setName("John Updated");
        savedStudent.setRoll("CSE999");
        Student updatedStudent = studentRepository.save(savedStudent);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Student foundStudent = studentRepository.findById(updatedStudent.getId()).get();
        assertThat(foundStudent.getName()).isEqualTo("John Updated");
        assertThat(foundStudent.getRoll()).isEqualTo("CSE999");
    }

    @Test
    @DisplayName("Should save multiple students")
    void shouldSaveMultipleStudents() {
        // Act
        Student saved1 = studentRepository.save(testStudent1);
        Student saved2 = studentRepository.save(testStudent2);
        entityManager.flush();

        // Assert
        assertThat(saved1.getId()).isNotEqualTo(saved2.getId());
        assertThat(studentRepository.count()).isEqualTo(2);
    }

    // ==================== findById() Tests ====================

    @Test
    @DisplayName("Should find student by ID")
    void shouldFindStudent_ById() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent1);
        entityManager.clear();

        // Act
        Optional<Student> foundStudent = studentRepository.findById(savedStudent.getId());

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getId()).isEqualTo(savedStudent.getId());
        assertThat(foundStudent.get().getName()).isEqualTo("John Doe");
        assertThat(foundStudent.get().getRoll()).isEqualTo("CSE001");
    }

    @Test
    @DisplayName("Should return empty when student not found by ID")
    void shouldReturnEmpty_WhenStudentNotFound() {
        // Act
        Optional<Student> foundStudent = studentRepository.findById(999L);

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when finding with null ID")
    void shouldReturnEmpty_WhenFindingWithNullId() {
        // Act
        Optional<Student> foundStudent = studentRepository.findById(null);

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    // ==================== findAll() Tests ====================

    @Test
    @DisplayName("Should find all students")
    void shouldFindAllStudents() {
        // Arrange
        entityManager.persistAndFlush(testStudent1);
        entityManager.persistAndFlush(testStudent2);
        entityManager.clear();

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertThat(students)
            .isNotNull()
            .hasSize(2)
            .extracting(Student::getName)
            .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void shouldReturnEmptyList_WhenNoStudentsExist() {
        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertThat(students)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("Should return all students in insertion order")
    void shouldReturnStudents_InInsertionOrder() {
        // Arrange
        Student saved1 = entityManager.persistAndFlush(testStudent1);
        Student saved2 = entityManager.persistAndFlush(testStudent2);
        entityManager.clear();

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertThat(students).hasSize(2);
        assertThat(students.get(0).getId()).isEqualTo(saved1.getId());
        assertThat(students.get(1).getId()).isEqualTo(saved2.getId());
    }

    // ==================== deleteById() Tests ====================

    @Test
    @DisplayName("Should delete student by ID")
    void shouldDeleteStudent_ById() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent1);
        Long studentId = savedStudent.getId();
        entityManager.clear();

        // Act
        studentRepository.deleteById(studentId);
        entityManager.flush();

        // Assert
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertThat(deletedStudent).isEmpty();
    }

    @Test
    @DisplayName("Should not throw exception when deleting non-existent student")
    void shouldNotThrowException_WhenDeletingNonExistentStudent() {
        // Act & Assert
        // Should not throw exception
        studentRepository.deleteById(999L);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should delete only specified student")
    void shouldDeleteOnlySpecifiedStudent() {
        // Arrange
        Student saved1 = entityManager.persistAndFlush(testStudent1);
        Student saved2 = entityManager.persistAndFlush(testStudent2);
        entityManager.clear();

        // Act
        studentRepository.deleteById(saved1.getId());
        entityManager.flush();

        // Assert
        assertThat(studentRepository.findById(saved1.getId())).isEmpty();
        assertThat(studentRepository.findById(saved2.getId())).isPresent();
        assertThat(studentRepository.count()).isEqualTo(1);
    }

    // ==================== count() Tests ====================

    @Test
    @DisplayName("Should count all students")
    void shouldCountAllStudents() {
        // Arrange
        entityManager.persistAndFlush(testStudent1);
        entityManager.persistAndFlush(testStudent2);

        // Act
        long count = studentRepository.count();

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return zero when no students exist")
    void shouldReturnZero_WhenNoStudentsExist() {
        // Act
        long count = studentRepository.count();

        // Assert
        assertThat(count).isEqualTo(0);
    }

    // ==================== existsById() Tests ====================

    @Test
    @DisplayName("Should return true when student exists")
    void shouldReturnTrue_WhenStudentExists() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent1);

        // Act
        boolean exists = studentRepository.existsById(savedStudent.getId());

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when student does not exist")
    void shouldReturnFalse_WhenStudentDoesNotExist() {
        // Act
        boolean exists = studentRepository.existsById(999L);

        // Assert
        assertThat(exists).isFalse();
    }

    // ==================== Transactional Behavior Tests ====================

    @Test
    @DisplayName("Should rollback transaction on error")
    void shouldRollbackTransaction_OnError() {
        // Arrange
        entityManager.persistAndFlush(testStudent1);
        long initialCount = studentRepository.count();

        // Act
        try {
            Student invalidStudent = new Student();
            // Intentionally create invalid state if business logic requires it
            studentRepository.save(invalidStudent);
            entityManager.flush();
        } catch (Exception e) {
            // Expected exception
        }

        // Assert
        // Count should remain the same due to rollback
        assertThat(studentRepository.count()).isEqualTo(initialCount);
    }

    @Test
    @DisplayName("Should maintain data integrity across operations")
    void shouldMaintainDataIntegrity_AcrossOperations() {
        // Arrange
        Student saved1 = studentRepository.save(testStudent1);
        Student saved2 = studentRepository.save(testStudent2);
        entityManager.flush();

        // Act
        studentRepository.deleteById(saved1.getId());
        Student saved3 = studentRepository.save(new Student(null, "Alice Johnson", "CSE003"));
        entityManager.flush();
        entityManager.clear();

        // Assert
        List<Student> remainingStudents = studentRepository.findAll();
        assertThat(remainingStudents).hasSize(2);
        assertThat(remainingStudents)
            .extracting(Student::getRoll)
            .containsExactlyInAnyOrder("CSE002", "CSE003");
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Should handle student with null values")
    void shouldHandleStudent_WithNullValues() {
        // Arrange
        Student studentWithNulls = new Student();
        studentWithNulls.setName(null);
        studentWithNulls.setRoll(null);

        // Act
        Student savedStudent = studentRepository.save(studentWithNulls);

        // Assert
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getName()).isNull();
        assertThat(savedStudent.getRoll()).isNull();
    }

    @Test
    @DisplayName("Should handle student with empty strings")
    void shouldHandleStudent_WithEmptyStrings() {
        // Arrange
        Student studentWithEmptyStrings = new Student();
        studentWithEmptyStrings.setName("");
        studentWithEmptyStrings.setRoll("");

        // Act
        Student savedStudent = studentRepository.save(studentWithEmptyStrings);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Student foundStudent = studentRepository.findById(savedStudent.getId()).get();
        assertThat(foundStudent.getName()).isEmpty();
        assertThat(foundStudent.getRoll()).isEmpty();
    }

    @Test
    @DisplayName("Should handle long student names")
    void shouldHandleLongStudentNames() {
        // Arrange
        String longName = "A".repeat(255);
        testStudent1.setName(longName);

        // Act
        Student savedStudent = studentRepository.save(testStudent1);
        entityManager.flush();

        // Assert
        assertThat(savedStudent.getName()).hasSize(255);
    }
}
