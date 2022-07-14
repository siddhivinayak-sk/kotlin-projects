package com.sk.ktl.interop;

public class User {
    private String name;

    public User() {}

    public User(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void create(Created created) {
        created.onCreate(this);
    }
}
