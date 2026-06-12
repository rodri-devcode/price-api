package ar.com.rodrifernandez.priceapi.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Test
    void validRequest_hasNoViolations() {
        ProductRequest req = new ProductRequest("Azucar", "Domino", "1 Kg", new BigDecimal("1100"), "Dia", "Almacen");

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);

        assertTrue(violations.isEmpty(), () -> "Expected no validation violations but got: " + violations);
    }

    @Test
    void missingRequiredFields_producesViolations() {
        // brand is optional; others are mandatory
        ProductRequest req = new ProductRequest("", null, "", null, " ", null);

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);

        // Expect violations for: type, quantity, price, store, category => 5
        assertEquals(5, violations.size(), "Expected 5 violations for missing/blank required fields");

        // ensure price is reported and other fields too
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")), "price violation expected");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")), "type violation expected");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantity")), "quantity violation expected");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("store")), "store violation expected");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("category")), "category violation expected");
    }

    @Test
    void negativePrice_isInvalid() {
        ProductRequest req = new ProductRequest("Azucar", "Domino", "1 Kg", new BigDecimal("-1"), "Dia", "Almacen");

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price") && v.getMessage().toLowerCase().contains("positive")), "Expected positive-or-zero violation on price");
    }

    @Test
    void tooLongFields_triggerSizeViolation() {
        String long101 = "a".repeat(101);
        ProductRequest req = new ProductRequest(long101, "Domino", "1 Kg", new BigDecimal("10"), "Dia", "Almacen");

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(req);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type") && v.getMessage().toLowerCase().contains("must not exceed")), "Expected size violation on type");
    }
}
