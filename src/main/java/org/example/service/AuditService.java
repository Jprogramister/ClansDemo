package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Clan;
import org.example.repository.AuditRepository;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class AuditService implements BiConsumer<Clan, Clan> {
    private final AuditRepository auditRepository;

    @Override
    public void accept(@Nullable Clan before, @Nullable Clan after) {
        var record = String.format("Clan has been updated. A state before update is %s, after %s %n", before, after);
        System.out.println(record);
        auditRepository.save(record);
    }
}
