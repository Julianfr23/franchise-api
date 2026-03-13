package com.accenture.franchise.domain.exception;
public class FranchiseNotFoundException extends RuntimeException {
    public FranchiseNotFoundException(String id) {
        super("Franchise not found with id: " + id);
    }
}
