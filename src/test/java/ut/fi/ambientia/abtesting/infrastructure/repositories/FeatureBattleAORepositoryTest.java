package ut.fi.ambientia.abtesting.infrastructure.repositories;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.activeobjects.test.TestActiveObjects;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.FeatureBattleAORepository;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.ExperimentAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.FeatureBattleAO;
import fi.ambientia.abtesting.infrastructure.repositories.persistence.UserExperimentAO;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.experiments.ExperimentRandomizer;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import net.java.ao.EntityManager;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import ut.fi.ambientia.helpers.TestPluginProperties;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ut.fi.ambientia.abtesting.model.TestData.FEATURE_BATTLE_IDENTIFIER;
import static ut.fi.ambientia.abtesting.model.TestData.USERIDENTIFIER;
import static ut.fi.ambientia.matchers.ExperimentMatcher.thatIsEqualTo;

@RunWith(ActiveObjectsJUnitRunner.class)
public class FeatureBattleAORepositoryTest {

    public static final int CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD = 0;
    public static final int CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW = 200;
    private EntityManager entityManager;

    private ActiveObjects ao; // (1)
    private FeatureBattleAORepository featureBattleRepository;
    private ExperimentAORepository experimentRepository;
    private Experiment experiment;

    TestPluginProperties properties;

    @Before
    public void setUp() throws Exception
    {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);
        ao.migrate(ExperimentAO.class);
        ao.migrate(FeatureBattleAO.class);
        ao.migrate(UserExperimentAO.class);
        properties = new TestPluginProperties();
        featureBattleRepository = new FeatureBattleAORepository(ao, properties);
        experimentRepository = new ExperimentAORepository(ao, properties);
    }

    @Test
    public void should_get_default_random_value_if_random_percentage_not_defined() throws Exception {
        ExperimentRandomizer experimentRandomizer = featureBattleRepository.experimentRandomizer(FEATURE_BATTLE_IDENTIFIER);

        properties.setProperty("feature.battle.default.win", CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD);
        assertThat(experimentRandomizer.randomize().type(), equalTo(Experiment.Type.GOOD_OLD));

        properties.setProperty("feature.battle.default.win", CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW);
        assertThat(experimentRandomizer.randomize().type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void should_get_experiment_treshhold_from_db() throws Exception {

        properties.setProperty("feature.battle.default.win", CONSTANT_SMALL_ENOUGH_SO_THAT_ALWAYS_TAKES_THE_OLD);
        featureBattleRepository.createFeatureBattle(FEATURE_BATTLE_IDENTIFIER);
        featureBattleRepository.setThreshold(FEATURE_BATTLE_IDENTIFIER, CONSTANT_BIG_ENOUGH_TO_ALWAYS_TRY_THE_NEW);

        ExperimentRandomizer experimentRandomizer = featureBattleRepository.experimentRandomizer(FEATURE_BATTLE_IDENTIFIER);
        Experiment experiment = experimentRandomizer.randomize();

        assertThat(experiment.type(), equalTo(Experiment.Type.NEW_AND_SHINY));
    }

    @Test
    public void should_store_randomized_experiment_for_user() throws Exception {
        UserExperimentAO[] abTestAos = ao.find(UserExperimentAO.class);
        assertThat(abTestAos.length, equalTo(0));

        featureBattleRepository.newFeatureBattleFor(FEATURE_BATTLE_IDENTIFIER).forUser(  USERIDENTIFIER ).resultBeing( newAndShiny(FEATURE_BATTLE_IDENTIFIER) );

        abTestAos = ao.find(UserExperimentAO.class);
        assertThat(abTestAos.length, equalTo(1));
    }

    @Ignore
    @Test
    public void should_not_save_more_than_one_experiments() throws Exception {
        fail("NOT TESTED");
    }

    @Test
    public void should_get_experiment_for_user() throws Exception {
        Experiment experiment = newAndShiny(FEATURE_BATTLE_IDENTIFIER);
        featureBattleRepository.newFeatureBattleFor(FEATURE_BATTLE_IDENTIFIER).forUser(  USERIDENTIFIER ).resultBeing(experiment);

        List<Experiment> experiments = experimentRepository.experimentsForUser(USERIDENTIFIER);

        assertThat(experiments, hasItem( thatIsEqualTo(experiment, FEATURE_BATTLE_IDENTIFIER)));
    }

    @Test
    public void should_get_feature_battle_from_repository() throws Exception {
//        featureBattleRepository.createFeatureBattle(FEATURE_BATTLE_IDENTIFIER, newAndShiny())

        FeatureBattleAO featureBattleAO = ao.create(FeatureBattleAO.class);
        featureBattleAO.setFeatureBattleId( FEATURE_BATTLE_IDENTIFIER.getIdentifier());
        featureBattleAO.setThreshold(10);
        featureBattleAO.save();
//        featureBattleAO.setGoodOld ( newGoodOld() );
//        featureBattleAO.setNewAndShiny

        Optional<FeatureBattle> featureBattle = featureBattleRepository.getFeatureBattle(FEATURE_BATTLE_IDENTIFIER);

        assertThat( featureBattle.get().getIdentifier(), equalTo(FEATURE_BATTLE_IDENTIFIER) );
    }

    private Experiment newAndShiny(FeatureBattleIdentifier featureBattleIdentifier) {
        return new NewAndShiny(featureBattleIdentifier);
    }

}