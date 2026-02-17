package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for StudentController
 * 
 * Testing Strategy:
 * - Uses @SpringBootTest for full application context
 * - Uses @AutoConfigureMockMvc for MockMvc support
 * - Tests HTTP endpoints with Spring Security
 * - Validates request/response handling
 * - Tests authorization rules (ROLE_USER, ROLE_ADMIN)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("StudentController Integration Tests")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    private Student testStudent1;
    private Student testStudent2;
    private List<Student> testStudents;

    @BeforeEach
    void setUp() {
        // Arrange: Setup test data
        testStudent1 = new Student();
        testStudent1.setId(1L);
        testStudent1.setName("John Doe");
        testStudent1.setRoll("CSE001");

        testStudent2 = new Student();
        testStudent2.setId(2L);
        testStudent2.setName("Jane Smith");
        testStudent2.setRoll("CSE002");

        testStudents = Arrays.asList(testStudent1, testStudent2);
    }

    // ==================== GET /students Tests ====================

    @Test
    @DisplayName("Should redirect to login when accessing /students without authentication")
    void shouldRedirectToLogin_WhenNotAuthenticated() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/students"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));

        verify(studentService, never()).getAllStudents();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return students list when authenticated as USER")
    void shouldReturnStudentsList_WhenAuthenticatedAsUser() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(testStudents);

        // Act & Assert
        mockMvc.perform(get("/students"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("students"))
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attribute("students", hasSize(2)))
            .andExpect(model().attribute("students", hasItem(
                allOf(
                    hasProperty("id", is(1L)),
                    hasProperty("name", is("John Doe")),
                    hasProperty("roll", is("CSE001"))
                )
            )));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return students list when authenticated as ADMIN")
    void shouldReturnStudentsList_WhenAuthenticatedAsAdmin() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(testStudents);

        // Act & Assert
        mockMvc.perform(get("/students"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("students"))
            .andExpect(model().attributeExists("students"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should return empty model when no students exist")
    void shouldReturnEmptyModel_WhenNoStudentsExist() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/students"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("students"))
            .andExpect(model().attribute("students", hasSize(0)));

        verify(studentService, times(1)).getAllStudents();
    }

    // ==================== GET /students/add Tests ====================

    @Test
    @DisplayName("Should redirect to login when accessing /students/add without authentication")
    void shouldRedirectToLogin_WhenAddingWithoutAuth() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/students/add"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should deny access to /students/add for USER role")
    void shouldDenyAccess_WhenUserTriesToAdd() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/students/add"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should show add student form for ADMIN role")
    void shouldShowAddForm_WhenAdminAccesses() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/students/add"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("student-form"))
            .andExpect(model().attributeExists("student"))
            .andExpect(model().attribute("student", instanceOf(StudentDTO.class)));
    }

    // ==================== POST /students/store Tests ====================

    @Test
    @DisplayName("Should redirect to login when posting to /students/store without authentication")
    void shouldRedirectToLogin_WhenStoringWithoutAuth() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .param("name", "Test Student")
                .param("roll", "CSE999"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));

        verify(studentService, never()).saveStudent(any());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should deny access to store student for USER role")
    void shouldDenyAccess_WhenUserTriesToStore() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .param("name", "Test Student")
                .param("roll", "CSE999"))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(studentService, never()).saveStudent(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should store student successfully for ADMIN role")
    void shouldStoreStudent_WhenAdminSubmits() throws Exception {
        // Arrange
        Student savedStudent = new Student();
        savedStudent.setId(3L);
        savedStudent.setName("New Student");
        savedStudent.setRoll("CSE999");
        
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(savedStudent);

        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .param("name", "New Student")
                .param("roll", "CSE999"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should reject request without CSRF token")
    void shouldRejectRequest_WithoutCsrfToken() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/store")
                .param("name", "Test Student")
                .param("roll", "CSE999"))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(studentService, never()).saveStudent(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle empty form submission")
    void shouldHandleEmptyFormSubmission() throws Exception {
        // Arrange
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(new Student());

        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .param("name", "")
                .param("roll", ""))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }

    // ==================== POST /students/delete/{id} Tests ====================

    @Test
    @DisplayName("Should redirect to login when deleting without authentication")
    void shouldRedirectToLogin_WhenDeletingWithoutAuth() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/delete/1")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));

        verify(studentService, never()).deleteStudent(anyLong());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Should deny access to delete for USER role")
    void shouldDenyAccess_WhenUserTriesToDelete() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/students/delete/1")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().isForbidden());

        verify(studentService, never()).deleteStudent(anyLong());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete student successfully for ADMIN role")
    void shouldDeleteStudent_WhenAdminRequests() throws Exception {
        // Arrange
        doNothing().when(studentService).deleteStudent(1L);

        // Act & Assert
        mockMvc.perform(post("/students/delete/1")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle deletion of non-existent student")
    void shouldHandleDeletion_OfNonExistentStudent() throws Exception {
        // Arrange
        doNothing().when(studentService).deleteStudent(999L);

        // Act & Assert
        mockMvc.perform(post("/students/delete/999")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/students"));

        verify(studentService, times(1)).deleteStudent(999L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle service exception during deletion")
    void shouldHandleException_DuringDeletion() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Database error"))
            .when(studentService).deleteStudent(1L);

        // Act & Assert
        mockMvc.perform(post("/students/delete/1")
                .with(csrf()))
            .andDo(print())
            .andExpect(status().is5xxServerError());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    // ==================== Content Type and Request Validation Tests ====================

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should accept form data with proper content type")
    void shouldAcceptFormData_WithProperContentType() throws Exception {
        // Arrange
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(new Student());

        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Test Student")
                .param("roll", "CSE999"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should handle special characters in student data")
    void shouldHandleSpecialCharacters_InStudentData() throws Exception {
        // Arrange
        when(studentService.saveStudent(any(StudentDTO.class))).thenReturn(new Student());

        // Act & Assert
        mockMvc.perform(post("/students/store")
                .with(csrf())
                .param("name", "O'Connor & Sons")
                .param("roll", "CSE-001/2024"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }
}
