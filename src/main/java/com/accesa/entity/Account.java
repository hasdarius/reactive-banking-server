package com.accesa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table("account")
public class Account {
    @Id
    private Long id;
    @Column("account_number")
    private String accountNumber;
    @Column("total_balance")
    private double totalBalance;
    @Transient
    @JsonIgnore
    @MappedCollection(idColumn = "account_number")
    private List<Transaction> transactions;

    // Constructors, getters, and setters
}


