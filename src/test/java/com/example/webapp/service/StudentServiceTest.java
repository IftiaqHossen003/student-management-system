package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentService
 * 
 * Testing Strategy:
 * - Uses Mockito to mock dependencies (StudentRepository, ModelMapper)
 * - Tests business logic in isolation
 * - Follows AAA pattern (Arrange-Act-Assert)
 * - Covers success, failure, and edge cases
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        // Arrange: Setup test data
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("John Doe");
        testStudent.setRoll("CSE001");

        testStudentDTO = new StudentDTO();
        testStudentDTO.setName("John Doe");
        testStudentDTO.setRoll("CSE001");
    }

    // ==================== getAllStudents() Tests ====================

    @Test
    @DisplayName("Should return all students when students exist")
    void shouldReturnAllStudents_WhenStudentsExist() {
        // Arrange
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Jane Smith");
        student2.setRoll("CSE002");

        List<Student> expectedStudents = Arrays.asList(testStudent, student2);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> actualStudents = studentService.getAllStudents();

        // Assert
        assertThat(actualStudents)
            .isNotNull()
            .hasSize(2)
            .containsExactlyElementsOf(expectedStudents);
        
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void shouldReturnEmptyList_WhenNoStudentsExist() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Student> actualStudents = studentService.getAllStudents();

        // Assert
        assertThat(actualStudents)
            .isNotNull()
            .isEmpty();
        
        verify(studentRepository, times(1)).findAll();
    }

    // ==================== getStudentById() Tests ====================

    @Test
    @DisplayName("Should return student when student exists")
    void shouldReturnStudent_WhenStudentExists() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertThat(result)
            .isPresent()
            .contains(testStudent);
        
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when student does not exist")
    void shouldReturnEmpty_WhenStudentDoesNotExist() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(999L);

        // Assert
        assertThat(result).isEmpty();
        
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void shouldThrowException_WhenIdIsNull() {
        // Arrange - mimic real Spring Data behavior for null IDs
        when(studentRepository.findById(null))
            .thenThrow(new IllegalArgumentException("ID must not be null"));

        // Act & Assert
        assertThatThrownBy(() -> studentService.getStudentById(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("ID must not be null");
    }

    // ==================== saveStudent() Tests ====================

    @Test
    @DisplayName("Should save student successfully with valid DTO")
    void shouldSaveStudent_WithValidDTO() {
        // Arrange
        when(modelMapper.map(testStudentDTO, Student.class)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student savedStudent = studentService.saveStudent(testStudentDTO);

        // Assert
        assertThat(savedStudent)
            .isNotNull()
            .isEqualTo(testStudent);
        
        assertThat(savedStudent.getName()).isEqualTo("John Doe");
        assertThat(savedStudent.getRoll()).isEqualTo("CSE001");
        
        verify(modelMapper, times(1)).map(testStudentDTO, Student.class);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should save student with ID when updating")
    void shouldSaveStudent_WhenUpdating() {
        // Arrange
        testStudentDTO.setId(1L); // Simulate updating existing student
        testStudent.setId(1L);

        when(modelMapper.map(testStudentDTO, Student.class)).thenReturn(testStudent);
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student updatedStudent = studentService.saveStudent(testStudentDTO);

        // Assert
        assertThat(updatedStudent)
            .isNotNull()
            .isEqualTo(testStudent);
        
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should handle mapping exception")
    void shouldHandleMappingException() {
        // Arrange
        when(modelMapper.map(testStudentDTO, Student.class))
            .thenThrow(new RuntimeException("Mapping failed"));

        // Act & Assert
        assertThatThrownBy(() -> studentService.saveStudent(testStudentDTO))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Mapping failed");
        
        verify(modelMapper, times(1)).map(testStudentDTO, Student.class);
        verify(studentRepository, never()).save(any(Student.class));
    }

    // ==================== deleteStudent() Tests ====================

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudent_Successfully() {
        // Arrange
        doNothing().when(studentRepository).deleteById(1L);

        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle delete for non-existent student")
    void shouldHandleDelete_ForNonExistentStudent() {
        // Arrange
        doNothing().when(studentRepository).deleteById(999L);

        // Act
        studentService.deleteStudent(999L);

        // Assert
        verify(studentRepository, times(1)).deleteById(999L);
    }

    @Test
    @DisplayName("Should propagate exception when delete fails")
    void shouldPropagateException_WhenDeleteFails() {
        // Arrange
        doThrow(new RuntimeException("Database error"))
            .when(studentRepository).deleteById(anyLong());

        // Act & Assert
        assertThatThrownBy(() -> studentService.deleteStudent(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Database error");
        
        verify(studentRepository, times(1)).deleteById(1L);
    }

    // ==================== Edge Cases and Boundary Tests ====================

    @Test
    @DisplayName("Should handle repository returning null")
    void shouldHandleRepositoryReturningNull() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(null);

        // Act - service returns null when repository returns null
        List<StudentDTO> result = studentService.getAllStudents();

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should verify no interactions when methods not called")
    void shouldVerifyNoInteractions_WhenMethodsNotCalled() {
        // Assert
        verifyNoInteractions(studentRepository);
        verifyNoInteractions(modelMapper);
    }
}
