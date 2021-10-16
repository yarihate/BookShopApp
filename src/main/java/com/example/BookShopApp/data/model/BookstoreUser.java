package com.example.BookShopApp.data.model;

import com.example.BookShopApp.data.model.payments.BalanceTransactionEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class BookstoreUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;

    @Column(columnDefinition = "Decimal(10,2) default '00.00'")
    private Double balance;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<BalanceTransactionEntity> transactions = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<BalanceTransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BalanceTransactionEntity> transactions) {
        this.transactions = transactions;
    }
}
