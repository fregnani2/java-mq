package com.example.mq.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class responsible for the structure of the transaction entity, mapped to the table "transactions".
 *  * Annotations:
 *  * @AllArgsConstructor: Generates a constructor with 1 parameter for each field in your class. Fields are initialized in the order they are declared.
 *  * @NoArgsConstructor: Generates a no-argument constructor.
 *  * @Getter: Generates getters.
 *  * @Setter: Generates setters.
 *  * @EqualsAndHashCode: Generates hashCode and equals implementations from the id field.
 *  * @Entity: Specifies that the class is an entity and is mapped to a database table.
 *  * @Table: Specifies the name of the database table to be used for mapping.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer toAccountNumber;
    private Integer fromAccountNumber;
    @Column(name = "transaction_amount")
    private Double amount;
    @Column(name = "transaction_date")
    private LocalDateTime date;
    private String status;


}
