package ut.fi.ambientia.atlassian;

import org.junit.Test;
import fi.ambientia.atlassian.api.MyPluginComponent;
import fi.ambientia.atlassian.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}