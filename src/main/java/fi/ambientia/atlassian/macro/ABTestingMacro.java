package fi.ambientia.atlassian.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import fi.ambientia.abtesting.routes.Something;
import fi.ambientia.atlassian.users.MapCurrentUserToUserkey;

import java.util.Map;

public class ABTestingMacro implements Macro {
    public ABTestingMacro(Something something, MapCurrentUserToUserkey mapCurrentUserToUserkey) {

    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        return null;
    }

    public BodyType getBodyType() {
        return null;
    }

    public OutputType getOutputType() {
        return null;
    }
}
