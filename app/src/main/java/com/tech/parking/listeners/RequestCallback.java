package com.tech.parking.listeners;

public interface RequestCallback<M> {
    RequestCallback<Void> NOP = new RequestCallback<Void>() {
        @Override
        public void success(Void model) {

        }

        @Override
        public void error(String error) {

        }
    };

    void success(M model);

    void error(String error);
}