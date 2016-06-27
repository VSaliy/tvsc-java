package com.tvsc.persistence.repository;

import com.tvsc.core.AppProfiles;
import com.tvsc.persistence.config.PersistenceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Taras Zubrei
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersistenceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
public class EpisodeRepositoryTest {
    @Autowired
    EpisodeRepository episodeRepository;
    private final Long USER_ID = 1L;
    private final List<Long> PRESENT_IDS = Arrays.asList(5378093L, 300428L, 312249L, 405398L, 3039311L);
    private final List<Long> ABSENT_IDS = Arrays.asList(4426795L, 5487129L, 362532L, 427739L);

    @Test
    public void getEpisodes() {
        List<Long> episodes = episodeRepository.getEpisodes(USER_ID, 78901L);
        assertThat(episodes, is(notNullValue()));
        assertThat(episodes.size(), is(equalTo(19)));
    }

    @Test
    public void setWatched() {
        episodeRepository.setWatched(USER_ID, 78901L, ABSENT_IDS);
        episodeRepository.setUnwatched(USER_ID, 78901L, ABSENT_IDS);
    }

    @Test
    public void setWatchedAlreadyWatchedEpisodeExpectedDuplicatedKey() {
        Assertions.expectThrows(DuplicateKeyException.class, () -> episodeRepository.setWatched(USER_ID, 78901L, PRESENT_IDS));
    }

    @Test
    public void setUnwatchedYetNotWatchedEpisodeExpectedDuplicatedKey() {
        RuntimeException exception = Assertions.expectThrows(RuntimeException.class, () -> episodeRepository.setUnwatched(USER_ID, 78901L, ABSENT_IDS));
        assertThat(exception.getCause(), is(instanceOf(BatchUpdateException.class)));
    }
}