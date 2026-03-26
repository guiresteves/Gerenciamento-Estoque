package com.br.stockpro.model;

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

    @Column(nullable = false, length = 250)
    private String legalName;

    @Column(nullable = false, length = 250)
    private String tradeName;

    @NotBlank
    @Column(nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(length = 50)
    private String stateRegistration;

    @Column(length = 50)
    private String municipalRegistration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompanyType companyType;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @OneToOne(mappedBy = "companny")
    private Company company;
}
