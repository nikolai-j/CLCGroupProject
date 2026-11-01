
package nj.clcproject.controller;

import nj.clcproject.model.Game;
import nj.clcproject.service.GameService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.NoSuchElementException;

/**
 * Handles web requests for game inventory.
 */
@Controller
public class GameController {

    private static final Logger logger =
            LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Displays the application home page.
     */
    @GetMapping("/")
    public String home() {
        logger.info("Displaying home page");
        return "index";
    }

    /**
     * Displays all games in inventory.
     */
    @GetMapping("/games")
    public String listGames(Model model) {
        logger.info("Retrieving game inventory");

        model.addAttribute("games", gameService.getAllGames());

        return "games";
    }

    /**
     * Displays the form for adding a game.
     */
    @GetMapping("/games/new")
    public String showAddForm(Model model) {
        logger.info("Displaying add game form");

        model.addAttribute("game", new Game());

        return "game-form";
    }

    /**
     * Displays the form for editing an existing game.
     */
    @GetMapping("/games/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        logger.info("Editing game ID: {}", id);

        try {
            model.addAttribute("game",
                    gameService.getGameById(id));
        } catch (NoSuchElementException ex) {
            return "redirect:/games";
        }

        return "game-form";
    }

    /**
     * Verifies and saves game.
     */
    @PostMapping("/games/save")
    public String saveGame(
            @Valid @ModelAttribute("game") Game game,
            BindingResult result) {

        logger.info("Processing game save request");

        if (result.hasErrors()) {
            logger.warn("Game validation failed");
            return "game-form";
        }

        // Prevent form submissions from modifying an
        // existing record unless it already exists.
        if (game.getId() != null) {
            try {
                gameService.getGameById(game.getId());
            } catch (NoSuchElementException ex) {
                return "redirect:/games";
            }
        }

        gameService.saveGame(game);

        return "redirect:/games";
    }

    /**
     * Deletes a game and returns to the inventory list.
     */
    @PostMapping("/games/delete/{id}")
    public String deleteGame(@PathVariable Long id) {
        logger.info("Deleting game ID: {}", id);

        try {
            gameService.deleteGame(id);
        } catch (NoSuchElementException ex) {
            logger.warn("Game ID {} was not found", id);
        }

        return "redirect:/games";
    }
}
