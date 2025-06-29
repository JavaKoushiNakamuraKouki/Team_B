package com.example.demo.Model; 

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 
    private String name;
    private Integer age; 
    private Date start; 
    private Date end;  
    private String pass;

    // コンストラクタ
    public User() {}

    public User(Long id, String name, Integer age, Date start, Date end, String pass) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.start = start;
        this.end = end;
        this.pass = pass;
    }

    // GetterとSetter (IDがLong、ageがInteger、start/endがDateに対応させる)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}