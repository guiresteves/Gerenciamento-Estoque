package com.br.stockpro.validation.validator;

import com.br.stockpro.validation.annotation.ValidCNPJ;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCNPJ, String> {

    private static final int[] WEIGHTS_FIRST  = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHTS_SECOND = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;

        String cnpj = value.replaceAll("\\D", "");

        if (cnpj.length() != 14) return false;
        if (hasAllSameDigits(cnpj))  return false;

        int first  = calcDigit(cnpj, WEIGHTS_FIRST);
        int second = calcDigit(cnpj, WEIGHTS_SECOND);

        return cnpj.charAt(12) - '0' == first
                && cnpj.charAt(13) - '0' == second;
    }

    private int calcDigit(String cnpj, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += (cnpj.charAt(i) - '0') * weights[i];
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private boolean hasAllSameDigits(String cnpj) {
        return cnpj.chars().distinct().count() == 1;
    }
}
