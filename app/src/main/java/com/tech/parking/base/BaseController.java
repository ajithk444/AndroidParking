package com.tech.parking.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseController {
    private final DatabaseReference reference;

    protected BaseController(String path) {
        this.reference = FirebaseDatabase.getInstance().getReference().child(path);
    }

    public DatabaseReference getReference() {
        return reference;
    }
}
