package com.example.demo;

import java.util.UUID;

public record Employee(
        UUID id,
        String name,
        String phoneNumber
) {
}
