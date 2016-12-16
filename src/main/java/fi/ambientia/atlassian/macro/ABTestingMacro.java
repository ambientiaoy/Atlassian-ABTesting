package fi.ambientia.atlassian.macro;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import fi.ambientia.abtesting.routes.Something;

import java.util.Map;

public class ABTestingMacro implements Macro {
    public ABTestingMacro(Something something) {

    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        return "viewToBeRendered";
    }

    public BodyType getBodyType() {
        return BodyType.NONE;
    }

    public OutputType getOutputType() {
        return OutputType.BLOCK;
    }
}
