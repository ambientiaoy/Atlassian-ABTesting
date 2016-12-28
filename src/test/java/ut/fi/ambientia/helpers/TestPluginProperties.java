package ut.fi.ambientia.helpers;

import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.atlassian.properties.PluginProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestPluginProperties extends PluginProperties {
    private final Map<String, Object> properties;

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

    public void setProperty(String property, String value){
        this.properties.put(property, value);
    }

    @Override
    public Integer propertyOrDefault(String key, int i) {
        Optional<Integer> optional = Optional.ofNullable((Integer) properties.get(key));
        return optional.orElse( i );
    }

    @Override
    public String propertyOrDefault(String key, String defaultString) {
        Optional<String> optional = Optional.ofNullable((String) properties.get(key));
        return optional.orElse( defaultString );
    }
}
