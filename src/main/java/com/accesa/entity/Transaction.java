package com.accesa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    Long id;
    @Column("account_number")
    String accountNumber;
    @Column("date")
    LocalDate transactionDate;
    @Column("transaction_details")
    String transactionDetails;
    @Column("value_date")
    LocalDate valueDate;
    @Column("withdrawal_amount")
    Double withdrawalAmount;
    @Column("deposit_amount")
    Double depositAmount;
}


