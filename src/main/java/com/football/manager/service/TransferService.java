package com.football.manager.service;

import com.football.manager.exception.InsufficientFundsException;
import com.football.manager.model.entity.Player;
import com.football.manager.model.entity.Team;
import com.football.manager.repository.PlayerRepository;
import com.football.manager.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private static final Logger logger = LogManager.getLogger(TransferService.class);

    private final PlayerRepository playerRepository;

    private final TeamRepository teamRepository;

    /**
     * Transfers a player from one team to another, updating their respective budgets accordingly.
     * <p>
     * This method ensures that:
     * <ul>
     *     <li>The seller and buyer teams exist.</li>
     *     <li>The player exists and belongs to the selling team.</li>
     *     <li>The buying team has sufficient funds for the transfer.</li>
     *     <li>The player's team is updated, and the budgets of both teams are adjusted.</li>
     * </ul>
     * </p>
     *
     * @param playerId       The identifier of the player being transferred.
     * @param sellerTeamId   The identifier of the team selling the player.
     * @param buyerTeamId    The identifier of the team buying the player.
     * @param transferAmount The transfer fee to be deducted from the buyer's budget and added to the seller's budget.
     */
    @Transactional
    public void transfer(Long playerId, Long sellerTeamId, Long buyerTeamId, double transferAmount) {
        logger.info("Starting transfer process for playerId: {} from teamId: {} to teamId: {} for amount: {}",
                playerId, sellerTeamId, buyerTeamId, transferAmount);

        Team sellerTeam = teamRepository.findById(sellerTeamId)
                .orElseThrow(() -> new EntityNotFoundException("Seller team not found with id: " + sellerTeamId));

        Team buyerTeam = teamRepository.findById(buyerTeamId)
                .orElseThrow(() -> new EntityNotFoundException("Buyer team not found with id: " + buyerTeamId));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        Long playerCurrentTeamId = player.getTeam().getId();
        if (!sellerTeamId.equals(playerCurrentTeamId)) {
            throw new IllegalArgumentException("Player " + player.getName() + " does not belong to selling team " + sellerTeam.getName());
        }

        if (buyerTeam.getBudget() < transferAmount) {
            throw new InsufficientFundsException("Transfer failed due to insufficient funds. " + buyerTeam.getName()
                    + " current funds : " + buyerTeam.getBudget());
        }

        buyerTeam.setBudget(buyerTeam.getBudget() - transferAmount);
        sellerTeam.setBudget(sellerTeam.getBudget() + transferAmount);
        player.setTeam(buyerTeam);

        playerRepository.save(player);
        teamRepository.save(sellerTeam);
        teamRepository.save(buyerTeam);

        logger.info("Transfer successful! Player {} (id: {}) is now in team {}.",
                player.getName(), playerId, buyerTeam.getAcronym());
    }
}

