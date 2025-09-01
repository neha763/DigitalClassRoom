package com.digital.servicei;

import com.digital.dto.SessionRequest;
import com.digital.entity.Session;

import java.util.List;

public interface SessionService {
    String createSession(SessionRequest sessionRequest);

    List<Session> getAllSessions();

    // create session

    // view sessions by class and section

    // view session by sessionId

    // view sessions

}
