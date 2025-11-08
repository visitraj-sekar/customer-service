package com.vc.customer.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseAudit {
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	@Column(name = "created_by", nullable = false)
	private String createdBy = "system";
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	@Column(name = "updated_by", nullable = false)
	private String updatedBy = "system";
	@Version
	@Column(name = "version", nullable = false)
	private Long version = 0L;

	@PrePersist
	public void onCreate() {
		Instant now = Instant.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {
		this.updatedAt = Instant.now();
	}
}
