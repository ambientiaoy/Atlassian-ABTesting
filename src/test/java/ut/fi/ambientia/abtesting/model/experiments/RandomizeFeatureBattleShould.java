package ut.fi.ambientia.abtesting.model.experiments;

import fi.ambientia.abtesting.action.experiments.feature_battles.RandomizeFeatureBattle;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.ExperimentRepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import fi.ambientia.abtesting.model.experiments.NewAndShiny;
import fi.ambientia.abtesting.model.user.UserIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomizeFeatureBattleShould {

    private static final UserIdentifier USERIENTIFIER = new UserIdentifier("FOOBARBAZ");
    private static final FeatureBattleIdentifier EXPERIENT_IDENIFIER = new FeatureBattleIdentifier("EXP1");
    private RandomizeFeatureBattle randomizeFeatureBattle;
    private FeatureBattleRepository featureBattleRepository;
    private ExperimentRepository experimentRepository;

    @Before
    public void setUp() throws Exception {
        featureBattleRepository = mock(FeatureBattleRepository.class);
        experimentRepository = mock(ExperimentRepository.class);
        randomizeFeatureBattle = new RandomizeFeatureBattle(featureBattleRepository, experimentRepository);
        when(featureBattleRepository.experimentRandomizer(any(FeatureBattleIdentifier.class))).thenReturn( () -> new GoodOldWay(new FeatureBattleIdentifier("NO SUCH EXP")));
    }

    @Test
    public void return_experiment_if_defined_in_repository() throws Exception {
        NewAndShiny newAndShiny = new NewAndShiny(EXPERIENT_IDENIFIER);
        when(experimentRepository.experimentsForUser( USERIENTIFIER )).thenReturn(Arrays.asList(newAndShiny));

        Experiment experiment = randomizeFeatureBattle.getExperiment(EXPERIENT_IDENIFIER).forUser(USERIENTIFIER);

        assertThat(experiment, equalTo(newAndShiny));
    }

    @Test
    public void do_stuff() throws Exception {
        NewAndShiny newAndShiny = new NewAndShiny(EXPERIENT_IDENIFIER);
        when(experimentRepository.experimentsForUser( USERIENTIFIER )).thenReturn(Arrays.asList( new GoodOldWay( new FeatureBattleIdentifier("NOT NOW")) ));
        when( featureBattleRepository.experimentRandomizer( EXPERIENT_IDENIFIER )).thenReturn( () -> newAndShiny);

        Experiment experiment = randomizeFeatureBattle.getExperiment(EXPERIENT_IDENIFIER).forUser(USERIENTIFIER);

        assertThat(experiment, equalTo( newAndShiny ));
    }



}