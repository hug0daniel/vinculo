package com.vinculo.unit.api.donation.dto;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationRequest.DonationItemDto;
import com.vinculo.api.donation.dto.DonationRequest.DonorDto;
import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Donation DTO Validation")
class DonationDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("DonationRequest")
    class DonationRequestTests {

        @Test
        @DisplayName("should pass with valid data")
        void shouldPassWithValidData() {
            var request = new DonationRequest(
                new DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL),
                List.of(new DonationItemDto("Rice", new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6)))
            );

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when donor is null")
        void shouldFailWhenDonorNull() {
            var request = new DonationRequest(
                null,
                List.of(new DonationItemDto("Rice", new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6)))
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("donor")));
        }

        @Test
        @DisplayName("should fail when items list is empty")
        void shouldFailWhenItemsEmpty() {
            var request = new DonationRequest(
                new DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL),
                List.of()
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("items")));
        }

        @Test
        @DisplayName("should fail when items is null")
        void shouldFailWhenItemsNull() {
            var request = new DonationRequest(
                new DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL),
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
        }
    }

    @Nested
    @DisplayName("DonorDto")
    class DonorDtoTests {

        @Test
        @DisplayName("should pass with valid data")
        void shouldPassWithValidData() {
            var donor = new DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL);

            var violations = validator.validate(donor);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when type is null")
        void shouldFailWhenTypeNull() {
            var donor = new DonorDto("John Doe", "john@example.com", null);

            var violations = validator.validate(donor);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
        }
    }

    @Nested
    @DisplayName("DonationItemDto")
    class DonationItemDtoTests {

        @Test
        @DisplayName("should pass with valid data")
        void shouldPassWithValidData() {
            var item = new DonationItemDto("Rice", new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

            var violations = validator.validate(item);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when productName is null")
        void shouldFailWhenProductNameNull() {
            var item = new DonationItemDto(null, new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

            var violations = validator.validate(item);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("productName")));
        }

        @Test
        @DisplayName("should fail when quantity is null")
        void shouldFailWhenQuantityNull() {
            var item = new DonationItemDto("Rice", null, QuantityUnit.KG, LocalDate.now().plusMonths(6));

            var violations = validator.validate(item);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
        }

        @Test
        @DisplayName("should fail when quantity is not positive")
        void shouldFailWhenQuantityNotPositive() {
            var item = new DonationItemDto("Rice", new BigDecimal("-5"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

            var violations = validator.validate(item);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
        }

        @Test
        @DisplayName("should fail when unit is null")
        void shouldFailWhenUnitNull() {
            var item = new DonationItemDto("Rice", new BigDecimal("50"), null, LocalDate.now().plusMonths(6));

            var violations = validator.validate(item);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("unit")));
        }
    }
}