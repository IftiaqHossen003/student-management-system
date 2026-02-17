package com.example.webapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entity tests for Student
 * 
 * Testing Strategy:
 * - Tests POJO behavior (getters, setters)
 * - Tests object equality and hash code
 * - Tests constructors
 * - Validates Lombok-generated methods
 * - Tests entity state management
 */
@DisplayName("Student Entity Tests")
class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Should create student with no-args constructor")
    void shouldCreateStudent_WithNoArgsConstructor() {
        // Act
        Student newStudent = new Student();

        // Assert
        assertThat(newStudent).isNotNull();
        assertThat(newStudent.getId()).isNull();
        assertThat(newStudent.getName()).isNull();
        assertThat(newStudent.getRoll()).isNull();
    }

    @Test
    @DisplayName("Should create student with all-args constructor")
    void shouldCreateStudent_WithAllArgsConstructor() {
        // Act
        Student newStudent = new Student(1L, "John Doe", "CSE001");

        // Assert
        assertThat(newStudent).isNotNull();
        assertThat(newStudent.getId()).isEqualTo(1L);
        assertThat(newStudent.getName()).isEqualTo("John Doe");
        assertThat(newStudent.getRoll()).isEqualTo("CSE001");
    }

    // ==================== Getter Tests ====================

    @Test
    @DisplayName("Should return null for uninitialized ID")
    void shouldReturnNull_ForUninitializedId() {
        // Assert
        assertThat(student.getId()).isNull();
    }

    @Test
    @DisplayName("Should return correct ID after setting")
    void shouldReturnCorrectId_AfterSetting() {
        // Act
        student.setId(1L);

        // Assert
        assertThat(student.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should return null for uninitialized name")
    void shouldReturnNull_ForUninitializedName() {
        // Assert
        assertThat(student.getName()).isNull();
    }

    @Test
    @DisplayName("Should return correct name after setting")
    void shouldReturnCorrectName_AfterSetting() {
        // Act
        student.setName("Jane Smith");

        // Assert
        assertThat(student.getName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should return null for uninitialized roll")
    void shouldReturnNull_ForUninitializedRoll() {
        // Assert
        assertThat(student.getRoll()).isNull();
    }

    @Test
    @DisplayName("Should return correct roll after setting")
    void shouldReturnCorrectRoll_AfterSetting() {
        // Act
        student.setRoll("CSE002");

        // Assert
        assertThat(student.getRoll()).isEqualTo("CSE002");
    }

    // ==================== Setter Tests ====================

    @Test
    @DisplayName("Should set ID correctly")
    void shouldSetId_Correctly() {
        // Act
        student.setId(100L);

        // Assert
        assertThat(student.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should update ID when set multiple times")
    void shouldUpdateId_WhenSetMultipleTimes() {
        // Act
        student.setId(1L);
        student.setId(2L);

        // Assert
        assertThat(student.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should set name correctly")
    void shouldSetName_Correctly() {
        // Act
        student.setName("Alice Johnson");

        // Assert
        assertThat(student.getName()).isEqualTo("Alice Johnson");
    }

    @Test
    @DisplayName("Should handle empty name")
    void shouldHandleEmptyName() {
        // Act
        student.setName("");

        // Assert
        assertThat(student.getName()).isEmpty();
    }

    @Test
    @DisplayName("Should handle null name")
    void shouldHandleNullName() {
        // Act
        student.setName(null);

        // Assert
        assertThat(student.getName()).isNull();
    }

    @Test
    @DisplayName("Should set roll correctly")
    void shouldSetRoll_Correctly() {
        // Act
        student.setRoll("EEE101");

        // Assert
        assertThat(student.getRoll()).isEqualTo("EEE101");
    }

    @Test
    @DisplayName("Should handle special characters in roll")
    void shouldHandleSpecialCharacters_InRoll() {
        // Act
        student.setRoll("CSE-001/2024");

        // Assert
        assertThat(student.getRoll()).isEqualTo("CSE-001/2024");
    }

    // ==================== Equals and HashCode Tests ====================

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqual_ToItself() {
        // Arrange
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Act & Assert
        assertThat(student).isEqualTo(student);
        assertThat(student.hashCode()).isEqualTo(student.hashCode());
    }

    @Test
    @DisplayName("Should be equal when all fields are same")
    void shouldBeEqual_WhenAllFieldsAreSame() {
        // Arrange
        Student student1 = new Student(1L, "John Doe", "CSE001");
        Student student2 = new Student(1L, "John Doe", "CSE001");

        // Act & Assert
        assertThat(student1).isEqualTo(student2);
        assertThat(student1.hashCode()).isEqualTo(student2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when IDs are different")
    void shouldNotBeEqual_WhenIdsAreDifferent() {
        // Arrange
        Student student1 = new Student(1L, "John Doe", "CSE001");
        Student student2 = new Student(2L, "John Doe", "CSE001");

        // Act & Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @DisplayName("Should not be equal when names are different")
    void shouldNotBeEqual_WhenNamesAreDifferent() {
        // Arrange
        Student student1 = new Student(1L, "John Doe", "CSE001");
        Student student2 = new Student(1L, "Jane Smith", "CSE001");

        // Act & Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @DisplayName("Should not be equal when rolls are different")
    void shouldNotBeEqual_WhenRollsAreDifferent() {
        // Arrange
        Student student1 = new Student(1L, "John Doe", "CSE001");
        Student student2 = new Student(1L, "John Doe", "CSE002");

        // Act & Assert
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqual_ToNull() {
        // Arrange
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Act & Assert
        assertThat(student).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqual_ToDifferentClass() {
        // Arrange
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Act & Assert
        assertThat(student).isNotEqualTo("Not a Student");
    }

    @Test
    @DisplayName("Should handle equals with null fields")
    void shouldHandleEquals_WithNullFields() {
        // Arrange
        Student student1 = new Student(null, null, null);
        Student student2 = new Student(null, null, null);

        // Act & Assert
        assertThat(student1).isEqualTo(student2);
        assertThat(student1.hashCode()).isEqualTo(student2.hashCode());
    }

    @Test
    @DisplayName("Should maintain consistent hashCode")
    void shouldMaintainConsistent_HashCode() {
        // Arrange
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Act
        int hashCode1 = student.hashCode();
        int hashCode2 = student.hashCode();

        // Assert
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    // ==================== ToString Tests ====================

    @Test
    @DisplayName("Should generate string representation")
    void shouldGenerateStringRepresentation() {
        // Arrange
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Act
        String toString = student.toString();

        // Assert
        assertThat(toString)
            .isNotNull()
            .contains("Student")
            .contains("id=1")
            .contains("name=John Doe")
            .contains("roll=CSE001");
    }

    @Test
    @DisplayName("Should handle toString with null fields")
    void shouldHandleToString_WithNullFields() {
        // Act
        String toString = student.toString();

        // Assert
        assertThat(toString)
            .isNotNull()
            .contains("Student");
    }

    // ==================== Property Chaining Tests ====================

    @Test
    @DisplayName("Should support property chaining with setters")
    void shouldSupportPropertyChaining_WithSetters() {
        // Act
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Assert
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getName()).isEqualTo("John Doe");
        assertThat(student.getRoll()).isEqualTo("CSE001");
    }

    // ==================== Validation Tests ====================

    @Test
    @DisplayName("Should accept valid student data")
    void shouldAcceptValidStudentData() {
        // Act
        student.setId(1L);
        student.setName("John Doe");
        student.setRoll("CSE001");

        // Assert
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isNotBlank();
        assertThat(student.getRoll()).isNotBlank();
    }

    @Test
    @DisplayName("Should handle unicode characters in name")
    void shouldHandleUnicodeCharacters_InName() {
        // Act
        student.setName("José García");

        // Assert
        assertThat(student.getName()).isEqualTo("José García");
    }

    @Test
    @DisplayName("Should handle very long name")
    void shouldHandleVeryLongName() {
        // Arrange
        String longName = "A".repeat(255);

        // Act
        student.setName(longName);

        // Assert
        assertThat(student.getName()).hasSize(255);
    }

    @Test
    @DisplayName("Should handle numeric roll numbers")
    void shouldHandleNumericRollNumbers() {
        // Act
        student.setRoll("123456");

        // Assert
        assertThat(student.getRoll()).isEqualTo("123456");
    }

    @Test
    @DisplayName("Should handle alphanumeric roll with special chars")
    void shouldHandleAlphanumericRoll_WithSpecialChars() {
        // Act
        student.setRoll("CSE-2024-001");

        // Assert
        assertThat(student.getRoll()).isEqualTo("CSE-2024-001");
    }

    // ==================== State Mutation Tests ====================

    @Test
    @DisplayName("Should allow state mutation after creation")
    void shouldAllowStateMutation_AfterCreation() {
        // Arrange
        student.setId(1L);
        student.setName("Original Name");
        student.setRoll("ROLL001");

        // Act
        student.setName("Updated Name");
        student.setRoll("ROLL002");

        // Assert
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getName()).isEqualTo("Updated Name");
        assertThat(student.getRoll()).isEqualTo("ROLL002");
    }

    @Test
    @DisplayName("Should maintain independent instances")
    void shouldMaintainIndependent_Instances() {
        // Arrange
        Student student1 = new Student(1L, "John", "001");
        Student student2 = new Student(2L, "Jane", "002");

        // Act
        student1.setName("John Updated");

        // Assert
        assertThat(student1.getName()).isEqualTo("John Updated");
        assertThat(student2.getName()).isEqualTo("Jane");
    }
}
