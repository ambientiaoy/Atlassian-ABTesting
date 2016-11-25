package ut.fi.ambientia.abtesting.action.experiments.feature_battles;

import fi.ambientia.abtesting.action.experiments.feature_battles.DisplayFeatureBattleExperiment;
import fi.ambientia.abtesting.model.experiments.Experiment;
import fi.ambientia.abtesting.model.experiments.GoodOldWay;
import org.apache.http.annotation.Experimental;
import org.junit.Before;
import org.junit.Test;
import fi.ambientia.abtesting.model.experiments.FeatureBattleService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DisplayFeatureBattleExperimentShould {

    private FeatureBattleService featureBattleService;
    private DisplayFeatureBattleExperiment displayFeatureBattleExperiment;

    @Before
    public void setUp() throws Exception {
        featureBattleService = mock(FeatureBattleService.class);
        displayFeatureBattleExperiment = new DisplayFeatureBattleExperiment( featureBattleService );
    }

    @Test
    public void create_a_feature_battle_experiment_for_a_user() throws Exception {
        Experiment experiment1 = mock(Experiment.class);
        when(featureBattleService.selectFeatureFor("USER KEY")).thenReturn( experiment1 );

        displayFeatureBattleExperiment.displayContent( "USER KEY" );


    }
}