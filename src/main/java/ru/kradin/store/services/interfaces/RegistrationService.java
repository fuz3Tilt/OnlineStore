package ru.kradin.store.services.interfaces;

import ru.kradin.store.DTOs.RegistrationDTO;
import ru.kradin.store.exceptions.VerificationTokenNotFoundException;

public interface RegistrationService {
    public void register(RegistrationDTO registrationDTO) throws VerificationTokenNotFoundException;
}
