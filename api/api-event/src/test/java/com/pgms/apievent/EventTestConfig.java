package com.pgms.apievent;

import com.pgms.coreinfraes.repository.EventSearchRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class EventTestConfig {

    @MockBean
    private EventSearchRepository eventSearchRepository;

}

