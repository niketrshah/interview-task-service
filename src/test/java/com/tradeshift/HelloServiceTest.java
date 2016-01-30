package com.tradeshift;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

public class HelloServiceTest {

    private HelloDAO helloDAO;
    private HelloService helloService;

    @Before
    public void tearUp() {
        this.helloDAO = Mockito.mock(HelloDAO.class);
        helloService = new HelloService(helloDAO);
    }

    @Test
    public void does_actually_say_it() {
        assertEquals("Shift Happens!", helloService.sayIt());
    }
}
