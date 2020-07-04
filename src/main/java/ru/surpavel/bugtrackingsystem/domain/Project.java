package ru.surpavel.bugtrackingsystem.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Size(max = 50)
    @Column(unique = true)
    private String title;
    
    
    @Size(max = 100)
    @Column(unique = true)
    private String file;

    
}
