package com.example.GestionDesEntretient.exception;


public class ResourceNotFoundException extends RuntimeException {
    private String resource;
    private Long resourceId;

    public ResourceNotFoundException(String resource, Long resourceId) {
        super("Resource not found: " + resource + " with ID " + resourceId);
        this.resource = resource;
        this.resourceId = resourceId;
    }

    public String getResource() {
        return resource;
    }

    public Long getResourceId() {
        return resourceId;
    }
}
