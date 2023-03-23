package io.github.gabriel.quarkussocial.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateUserRequest {

    @NotBlank
    private String name;
    @NotNull
    private Integer age;

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
}
