package com.gestion.backend.services;

import com.gestion.backend.entities.Session;
import java.util.List;

public interface SessionService {
    List<Session> getAllSessions();

    Session createSession(Session session);

    void deleteSession(Long id);

    Session toggleActive(Long id);
}
