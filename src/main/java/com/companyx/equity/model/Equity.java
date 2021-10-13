package com.companyx.equity.model;

import javax.persistence.*;

@Entity
public class Equity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    public Equity() {  }

    public Equity(String name) {
        this.setName(name);
    }

    public Equity(int id, String title, String content) {
        this.setId(id);
        this.setName(title);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}