package com.example.gitapinext.adapter.First;

import java.util.Objects;

public class UserModel {

    private Long id;
    private String name;
    private int counter;

    public UserModel(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel model = (UserModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(name, model.name) &&
                Objects.equals(counter, model.counter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, counter);
    }
}
