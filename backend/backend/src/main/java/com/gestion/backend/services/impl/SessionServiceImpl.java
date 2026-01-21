package com.gestion.backend.services.impl;

import com.gestion.backend.entities.Session;
import com.gestion.backend.repositories.SessionRepository;
import com.gestion.backend.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    @Override
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    @Override
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public Session toggleActive(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session with ID " + id + " not found"));
        session.setActive(!session.isActive());
        return sessionRepository.save(session);
    }
}
