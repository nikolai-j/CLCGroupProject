package nj.clcproject.service;

import nj.clcproject.model.Game;
import nj.clcproject.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *  Business operations for game inventory.
 */
@Service
public class GameService {

    private static final Logger logger =
            LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Gets all games from the database.
     */
    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        logger.info("Entering getAllGames");

        List<Game> games = gameRepository.findAll();

        logger.info("Exiting getAllGames");
        return games;
    }

    /**
     * Gets a game using its ID.
     */
    @Transactional(readOnly = true)
    public Game getGameById(Long id) {
        logger.info("Entering getGameById: {}", id);

        Game game = gameRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Game not found with ID: " + id));

        logger.info("Exiting getGameById");
        return game;
    }

    /**
     * Creates or updates a game record.
     */
    @Transactional
    public Game saveGame(Game game) {
        logger.info("Entering saveGame");

        Game savedGame = gameRepository.save(game);

        logger.info("Exiting saveGame");
        return savedGame;
    }

    /**
     * Deletes a game from the database.
     */
    @Transactional
    public void deleteGame(Long id) {
        logger.info("Entering deleteGame: {}", id);

        if (!gameRepository.existsById(id)) {
            throw new NoSuchElementException(
                    "Game not found with ID: " + id);
        }

        gameRepository.deleteById(id);

        logger.info("Exiting deleteGame");
    }
}
