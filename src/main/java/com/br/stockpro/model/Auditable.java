package com.br.stockpro.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;

@Getter
@MappedSuperclass
public abstract class Auditable {

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    protected Instant updatedAt;

    @PrePersist
    private void prePersistBase() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        prePersist(); // hook
    }

    @PreUpdate
    private void preUpdateBase() {
        this.updatedAt = Instant.now();
        preUpdate(); // hook
    }

    // Hooks — sobrescreva se precisar
    protected void prePersist() {}
    protected void preUpdate() {}
}
