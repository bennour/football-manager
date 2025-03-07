package com.football.manager.service;

import com.football.manager.exception.InsufficientFundsException;
import com.football.manager.model.entity.Player;
import com.football.manager.model.entity.Team;
import com.football.manager.repository.PlayerRepository;
import com.football.manager.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
class TransferServiceTest {

    @Autowired
    private TransferService service;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void shouldTransferPlayer() {
        Long playerId = 1L;
        Long sellerTeamId = 1L;
        Long buyerTeamId = 2L;
        double transferAmount = 5;

        Team sellerBefore = teamRepository.findById(sellerTeamId).orElseThrow();
        Team buyerBefore = teamRepository.findById(buyerTeamId).orElseThrow();
        Player playerBefore = playerRepository.findById(playerId).orElseThrow();

        double sellerInitialBudget = sellerBefore.getBudget();
        double buyerInitialBudget = buyerBefore.getBudget();

        service.transfer(playerId, sellerTeamId, buyerTeamId, transferAmount);

        Team sellerAfter = teamRepository.findById(sellerTeamId).orElseThrow();
        Team buyerAfter = teamRepository.findById(buyerTeamId).orElseThrow();
        Player playerAfter = playerRepository.findById(playerId).orElseThrow();

        assertThat(playerAfter.getTeam().getId()).isEqualTo(buyerTeamId);
        assertThat(sellerAfter.getBudget()).isEqualTo(sellerInitialBudget + transferAmount);
        assertThat(buyerAfter.getBudget()).isEqualTo(buyerInitialBudget - transferAmount);
    }


    @Test
    void shouldThrowExceptionWhenSellerTeamNotFound() {
        assertThatThrownBy(() -> service.transfer(1L, 99L, 2L, 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Seller team not found with id: 99");
    }

    @Test
    void shouldThrowExceptionWhenBuyerTeamNotFound() {
        assertThatThrownBy(() -> service.transfer(1L, 1L, 99L, 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Buyer team not found with id: 99");
    }

    @Test
    void shouldThrowExceptionWhenPlayerNotFound() {
        assertThatThrownBy(() -> service.transfer(99L, 1L, 1L, 5))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Player not found with id: 99");
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        assertThatThrownBy(() -> service.transfer(1L, 1L, 1L, 99999))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Transfer failed due to insufficient funds");
    }

    @Test
    void shouldThrowExceptionWhenPlayerDoesNotBelongToSeller() {
        assertThatThrownBy(() -> service.transfer(1L, 3L, 1L, 99999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not belong to selling team");
    }
}