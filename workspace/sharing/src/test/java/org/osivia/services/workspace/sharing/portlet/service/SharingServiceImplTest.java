package org.osivia.services.workspace.sharing.portlet.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;


public class SharingServiceImplTest {

    /** Class under test. */
    private final SharingServiceImpl service;


    /**
     * Constructor.
     */
    public SharingServiceImplTest() {
        super();
        this.service = new SharingServiceImpl();
    }


    @Test
    public void testGenerateLinkId() {
        for (int i = 0; i < 100; i++) {
            String linkId = this.service.generateLinkId();
            assertFalse(StringUtils.isEmpty(linkId));

            // RegEx
            Matcher matcher = Pattern.compile("\\w+-\\w+").matcher(linkId);
            try {
                assertTrue(matcher.matches());
            } catch (AssertionError e) {
                System.err.println(linkId);
                throw e;
            }
        }
    }


    @Test
    public void testConvertLongToBase62() {
        long value;
        String base64;

        value = 21740;
        base64 = this.service.convertLongToBase62(value);
        assertFalse(StringUtils.isEmpty(base64));
        assertEquals("Foo", base64);
    }

}
