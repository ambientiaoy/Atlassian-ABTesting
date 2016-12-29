package fi.ambientia.atlassian.routes.experiments;

import fi.ambientia.abtesting.action.experiments.CreateExperiment;
import fi.ambientia.abtesting.infrastructure.repositories.ExperimentAORepository;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattle;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleIdentifier;
import fi.ambientia.abtesting.model.feature_battles.FeatureBattleRepository;
import org.junit.Before;
import org.junit.Test;
import ut.fi.ambientia.abtesting.model.TestData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ut.fi.ambientia.mocks.Dummy.dummy;

public class FeatureBattleRouteShould {

    private FeatureBattleRepository featureBattleRepository;
    private FeatureBattleRoute featureBattle;

    @Before
    public void setUp() throws Exception {
        featureBattleRepository = mock(FeatureBattleRepository.class);
        CreateExperiment createNewHypothesis = mock(CreateExperiment.class);
        featureBattle = new FeatureBattleRoute(createNewHypothesis, featureBattleRepository);

    }

    @Test
    public void return_404_if_not_found() throws Exception {
        when(featureBattleRepository.getFeatureBattle( any(FeatureBattleIdentifier.class))).thenReturn(Optional.empty());

        Response head = featureBattle.head(dummy(HttpServletRequest.class), TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier());

        assertThat( head.getStatus(), equalTo(404));
    }



    @Test
    public void return_200_if_found() throws Exception {
        FeatureBattle fb = mock(FeatureBattle.class);
        when(featureBattleRepository.getFeatureBattle( any(FeatureBattleIdentifier.class))).thenReturn(Optional.of( fb ));

        Response head = featureBattle.head(dummy(HttpServletRequest.class), TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier());

        assertThat( head.getStatus(), equalTo(200));
    }

    @Test
    public void return_FeatureBattleDescriptor_if_found() throws Exception {
        FeatureBattle fb = new FeatureBattle(TestData.FEATURE_BATTLE_IDENTIFIER, new ArrayList<>());
        when(featureBattleRepository.getFeatureBattle( any(FeatureBattleIdentifier.class))).thenReturn(Optional.of( fb ));

        Response head = featureBattle.head(dummy(HttpServletRequest.class), TestData.FEATURE_BATTLE_IDENTIFIER.getIdentifier());

        assertThat( head.getEntity(), is(equalTo(null)));
    }

}