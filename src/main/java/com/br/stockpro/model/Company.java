package com.br.stockpro.model;

import com.br.stockpro.enums.CompanyStatus;
import com.br.stockpro.enums.CompanyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Table(
        name = "companies",
        indexes = {
                @Index(name = "idx_company_cnpj", columnList = "cnpj")
        }

)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String legalName;

    @NotBlank
    @Column(nullable = false)
    private String tradeName;

    @NotBlank
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    private String stateRegistration;
    private String municipalRegistration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    @NotBlank
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;
}
