package com.br.stockpro.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "categories",
        indexes = {
                @Index(name = "idx_category_company", columnList = "company_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_category_company_name",
                        columnNames = {"company_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
