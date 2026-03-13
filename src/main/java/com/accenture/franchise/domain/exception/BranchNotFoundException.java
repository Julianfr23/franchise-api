package com.accenture.franchise.domain.exception;
public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String id) {
        super("Branch not found with id: " + id);
    }
}
