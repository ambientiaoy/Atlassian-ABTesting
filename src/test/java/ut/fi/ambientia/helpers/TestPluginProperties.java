package ut.fi.ambientia.helpers;

import fi.ambientia.abtesting.infrastructure.repositories.ExperimentRepository;
import fi.ambientia.atlassian.properties.PluginProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestPluginProperties extends PluginProperties {
    private final Map<String, Object> properties;
    private int threshold = ExperimentRepository.DEFAULT_THRESHOLD;

    public TestPluginProperties() {
        properties = new HashMap<>();
    }

    @Override
    protected String getApplicationHome() {
        return this.getClass().getResource("/").getPath();
    }


    public void setProperty(String property, int threshold){
        this.properties.put(property, threshold);
    }

    @Override
    public Integer propertyOrDefault(String key, int i) {
        Optional<Integer> optional = Optional.ofNullable((Integer) properties.get(key));
        return optional.orElse( i );
    }
}
