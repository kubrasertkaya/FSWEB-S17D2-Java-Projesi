package com.workintech.dependencyprojection.mapping;

import com.workintech.dependencyprojection.model.Developer;

public class DeveloperResponse {
    private Developer developer;
    private String message;
    private int status;

    public DeveloperResponse(Developer developer, String message, int status) {
        this.developer = developer;
        this.message = message;
        this.status = status;
    }
}
