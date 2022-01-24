package com.jordiej.suitgame.utils

import com.jordiej.suitgame.enum.HandInput

class WinningCondition {

    companion object {
        const val DRAW = 0
        const val PLAYER_WINS = 1
        const val COMPUTER_WINS = 2
    }

    fun getWinningPlayer(playerHand: HandInput, computerHand: HandInput): Int {
        return if (playerHand == computerHand) {
            DRAW
        } else if (playerHand == HandInput.ROCK && computerHand == HandInput.SCISSORS) {
            PLAYER_WINS
        } else if (playerHand == HandInput.PAPER && computerHand == HandInput.ROCK) {
            PLAYER_WINS
        } else if (playerHand == HandInput.SCISSORS && computerHand == HandInput.PAPER) {
            PLAYER_WINS
        } else {
            COMPUTER_WINS
        }

    }
}