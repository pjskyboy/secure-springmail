package com.freesundance.mail;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration(locations="classpath:TestApplication-context.xml")
@Category(IntegrationTest.class)
public class TestApplication {

    @Autowired Application unit;

    @Test public void testSendSecureMail() {
        // if it throws it fails
        unit.sendMail();
    }
}
