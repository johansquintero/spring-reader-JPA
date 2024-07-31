package com.reader.SpringReader.persistence.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_generator")
    @SequenceGenerator(name = "my_sequence_generator",sequenceName = "my_sequence",allocationSize = 1)
    private Long id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    private String email;


}
