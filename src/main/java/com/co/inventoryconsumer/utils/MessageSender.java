package com.co.inventoryconsumer.utils;

public interface MessageSender<T> {
    void execute(T message, Long idMessage);
}