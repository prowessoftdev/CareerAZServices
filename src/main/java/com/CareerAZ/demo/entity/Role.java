package com.CareerAZ.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "roles",
        uniqueConstraints = {@UniqueConstraint(name = "uq_roles_name", columnNames = {"name"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g., ROLE_USER, ROLE_ADMIN
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;
}

