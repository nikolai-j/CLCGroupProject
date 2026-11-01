package nj.clcproject.repository;

import nj.clcproject.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides db access for games.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
