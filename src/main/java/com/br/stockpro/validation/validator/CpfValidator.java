package com.br.stockpro.validation.validator;

import com.br.stockpro.validation.annotation.ValidCPF;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;
        if (cpf.chars().distinct().count() == 1) return false;

        int firstDigit = calcDigit(cpf, 10);
        int secondDigit = calcDigit(cpf, 11);

        return cpf.charAt(9) - '0' == firstDigit
                && cpf.charAt(10) - '0' == secondDigit;
    }

    private int calcDigit(String cpf, int weightStart) {
        int sum = 0;
        for (int i = 0; i < weightStart - 1; i++) {
            sum += (cpf.charAt(i) - '0') * (weightStart - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}