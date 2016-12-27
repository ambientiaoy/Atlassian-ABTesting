package fi.ambientia.atlassian.routes;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Routes {
    public static String getParameter(Map<String, String> parameter, String parameter_name, Supplier<String> defaultParameterSupplier) {
        return Optional.ofNullable(parameter.get(parameter_name)).orElseGet(defaultParameterSupplier);
    }
}
