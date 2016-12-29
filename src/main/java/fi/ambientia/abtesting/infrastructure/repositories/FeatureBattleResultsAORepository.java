package fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import fi.ambientia.abtesting.infrastructure.activeobjects.SimpleActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleResultAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResult;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleResults;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import fi.ambientia.atlassian.properties.PluginProperties;
import net.java.ao.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FeatureBattleResultsAORepository implements FeatureBattleResults {

    private final SimpleActiveObjects ao;
    private final PluginProperties properties;

    @Autowired
    public FeatureBattleResultsAORepository(@Qualifier("TransactionalActiveObject") SimpleActiveObjects ao, PluginProperties properties) {
        this.ao = ao;
        this.properties = properties;
    }

    @Override
    public List<FeatureBattleResult> featureBattleResultsFor(FeatureBattleIdentifier featureBattleIdentifier) {
        List<FeatureBattleResult> featureBattleResults = new ArrayList<>();

        FeatureBattleAO[] featureBattleAOs = ao.find(FeatureBattleAO.class, Query.select().where("FEATURE_BATTLE_ID = ? ", featureBattleIdentifier.getIdentifier()));
        Optional<FeatureBattleAO> fb = Arrays.asList(featureBattleAOs).stream().findFirst();
        Optional<Integer> first = fb.map(_fb -> _fb.getID());

        if( fb.isPresent() ) {
            Integer feature_battle_id = first.orElse(-1);
            List<ExperimentAO> experimentAOs = Arrays.asList(fb.get().getExperiments());
            featureBattleResults = experimentAOs.stream().
                    map(
                            experimentAO -> new FeatureBattleResult(new UserIdentifier(""), ExperimentAORepository.buildExperiment(properties, experimentAO))
                    ).collect(Collectors.toList());

        }
        return featureBattleResults;
    }

    public List<FeatureBattleResult> _foobar_featureBattleResultsFor(FeatureBattleIdentifier featureBattleIdentifier) {
        FeatureBattleAO[] featureBattleAOs = ao.find(FeatureBattleAO.class, Query.select().where("ID = ? ", featureBattleIdentifier.getIdentifier()));
        Optional<Integer> first = Arrays.asList(featureBattleAOs).stream().map(fb -> fb.getID()).findFirst();

        Integer feature_battle_id = first.orElse(-1);

        FeatureBattleResultAO[] featureBattleResultAOs = ao.find(FeatureBattleResultAO.class, Query.select().where("FEATURE_BATTLE = ?", feature_battle_id));
        Optional<FeatureBattleAO> first1 = Arrays.asList(featureBattleAOs).stream().findFirst();

        Optional<ExperimentAO[]> experimentAOs = first1.map(featureBattleAO1 -> featureBattleAO1.getExperiments());
        Optional<Stream<ExperimentAO>> experimentAOStream = experimentAOs.map(experimentAOs1 -> Arrays.asList(experimentAOs1).stream());

//        Optional<FeatureBattleResult> featureBattleResult = experimentAOs.map( experimentAOs1 ->
//        {
//            return new FeatureBattleResult( new UserIdentifier( "" ), ExperimentAORepository.buildExperiment( experimentAOs1, properties ));
//        });
//        first1.map( featureBattleAO -> new FeatureBattleResult(
//                        new UserIdentifier( featureBattleAO.getUserID()),
//                        ExperimentAORepository.buildExperiment( featureBattleAO.get)));
        return new ArrayList<>();
    }
}
