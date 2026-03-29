package com.br.stockpro.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(
        name = "suppliers",
        indexes = {
                @Index(name = "idx_supplier_company", columnList = "company_id"),
                @Index(name = "idx_supplier_cnpj", columnList = "cnpj")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_supplier_company_cnpj",
                        columnNames = {"company_id", "cnpj"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 250)
    private String legalName;

    @Column(nullable = false, length = 250)
    private String tradeName;

    @Column(length = 50)
    private String stateRegistration;

    @Column(length = 50)
    private String municipalRegistration;

    @Column(nullable = false, length = 18)
    private String cnpj;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
