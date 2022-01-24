package com.jordiej.suitgame.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import com.jordiej.suitgame.R
import com.jordiej.suitgame.databinding.ActivityGameBinding
import com.jordiej.suitgame.enum.GetWinner
import com.jordiej.suitgame.enum.HandInput
import com.jordiej.suitgame.utils.WinningCondition
import com.shashank.sony.fancytoastlib.FancyToast
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private val TAG = ActivityGameBinding::class.simpleName
    private lateinit var binding: ActivityGameBinding

    private lateinit var playerHand: HandInput
    private lateinit var computerHand: HandInput
    private var playerScore = 0
    private var computerScore = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding()
        showDialogFragment()
        setOnClickListener()
    }

    private fun viewBinding() {
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    private fun showDialogFragment() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_fragment, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        val dismiss = mAlertDialog.findViewById<TextView>(R.id.tv_got_it)
        dismiss.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun setOnClickListener() {
        binding.flRock.setOnClickListener {
            if (playerScore != 3 && computerScore != 3) {
                //player select hand input
                playerHand = HandInput.ROCK
                showSelectedPlayerHand(HandInput.ROCK)
                getPlayerHand()

                //computer get random hand
                getComputerHand()

                //logging
                Log.d(TAG, "Player choose $playerHand")
                Log.d(TAG, "Computer choose $computerHand")

                //get winner
                getWinner()
                gameFinished()
            }
        }

        binding.flPaper.setOnClickListener {
            if (playerScore != 3 && computerScore != 3) {
                playerHand = HandInput.PAPER
                showSelectedPlayerHand(HandInput.PAPER)
                getPlayerHand()
                getComputerHand()
                Log.d(TAG, "Player choose $playerHand")
                Log.d(TAG, "Computer choose $computerHand")
                getWinner()
                gameFinished()
            }
        }

        binding.flScissors.setOnClickListener {
            if (playerScore != 3 && computerScore != 3) {
                playerHand = HandInput.SCISSORS
                showSelectedPlayerHand(HandInput.SCISSORS)
                getPlayerHand()
                getComputerHand()
                Log.d(TAG, "Player choose $playerHand")
                Log.d(TAG, "Computer choose $computerHand")
                getWinner()
                gameFinished()
            }
        }

        binding.tvInstructions.setOnClickListener {
            showDialogFragment()
        }

        binding.ivReset.setOnClickListener {
            playerScore = 0
            computerScore = 0
            viewBinding()
            setOnClickListener()
            FancyToast.makeText(
                this,
                "Game Reset Successfully!",
                FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS,
                false
            ).show()
            Log.d(TAG, "Game Reset Successfully!")
        }
    }

    private fun showSelectedPlayerHand(selectedHand: HandInput) {
        val handRock: FrameLayout?
        val handPaper: FrameLayout?
        val handScissors: FrameLayout?
        val selected = AnimationUtils.loadAnimation(this, R.anim.anim_selected)

        handRock = binding.flRock
        handPaper = binding.flPaper
        handScissors = binding.flScissors

        when (selectedHand) {
            HandInput.ROCK -> {
                handRock.startAnimation(selected)
                handRock.setBackgroundResource(R.drawable.bg_selected)
                handPaper.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
                handScissors.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
            }
            HandInput.PAPER -> {
                handRock.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
                handPaper.startAnimation(selected)
                handPaper.setBackgroundResource(R.drawable.bg_selected)
                handScissors.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
            }
            HandInput.SCISSORS -> {
                handRock.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
                handPaper.setBackgroundResource(R.drawable.bg_bottom_right_shadow)
                handScissors.startAnimation(selected)
                handScissors.setBackgroundResource(R.drawable.bg_selected)
            }
        }

    }

    private fun getPlayerHand() {
        val ltr = AnimationUtils.loadAnimation(this, R.anim.anim_left_to_right)
        val playerHandImage = binding.ivPlayerHand

        when (playerHand) {
            HandInput.ROCK -> {
                playerHandImage.setImageResource(R.drawable.batu)
                playerHandImage.startAnimation(ltr)
                playerHandImage.rotation = 90F
            }
            HandInput.PAPER -> {
                playerHandImage.setImageResource(R.drawable.kertas)
                playerHandImage.startAnimation(ltr)
                playerHandImage.rotation = 90F
            }
            HandInput.SCISSORS -> {
                playerHandImage.setImageResource(R.drawable.gunting)
                playerHandImage.startAnimation(ltr)
                playerHandImage.rotation = 110F
            }
        }
    }

    private fun getComputerHand() {
        val comHandRes = binding.ivComHand
        val getRandomHand = Random.nextInt(0, 3)
        computerHand = HandInput.values()[getRandomHand]
        val rtl = AnimationUtils.loadAnimation(this, R.anim.anim_right_to_left)

        when (computerHand) {
            HandInput.ROCK -> {
                comHandRes.setImageResource(R.drawable.batu)
                comHandRes.startAnimation(rtl)
                comHandRes.rotation = 90F
                comHandRes.rotationY = 180F
            }
            HandInput.PAPER -> {
                comHandRes.setImageResource(R.drawable.kertas)
                comHandRes.startAnimation(rtl)
                comHandRes.rotation = 90F
                comHandRes.rotationY = 180F
            }
            HandInput.SCISSORS -> {
                comHandRes.setImageResource(R.drawable.gunting)
                comHandRes.startAnimation(rtl)
                comHandRes.rotation = 110F
                comHandRes.rotationY = 180F
            }

        }
    }

    private fun getWinner() {
        val result = WinningCondition().getWinningPlayer(playerHand, computerHand)

        when (result) {
            WinningCondition.DRAW -> {
                binding.tvVs.text = getString(R.string.text_result_draw)
                Log.d(TAG, "DRAW")
            }
            WinningCondition.PLAYER_WINS -> {
                binding.tvVs.text = getString(R.string.text_result_player_win)
                playerScore++
                binding.tvPlayerScore.text = "$playerScore"
                Log.d(TAG, "PLAYER WIN THIS ROUND")
            }
            WinningCondition.COMPUTER_WINS -> {
                binding.tvVs.text = getString(R.string.text_result_com_win)
                computerScore++
                binding.tvComScore.text = "$computerScore"
                Log.d(TAG, "COMPUTER WIN THIS ROUND")
            }
        }
        Log.d(TAG, "Score = (Player : $playerScore), (Computer : $computerScore)")

    }

    private fun showWinnerDialog(suitWinner: GetWinner) {
        val mDialogView: View?

        when (suitWinner) {
            GetWinner.PLAYER -> {
                mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_player_wins, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                val playAgain = mAlertDialog.findViewById<TextView>(R.id.tv_play_again)

                playAgain.setOnClickListener {
                    playerScore = 0
                    computerScore = 0
                    viewBinding()
                    setOnClickListener()
                    mAlertDialog.dismiss()
                    FancyToast.makeText(
                        this,
                        "Game Reset Successfully!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                    Log.d(TAG, "Game Reset Successfully!")
                }

                val dismiss = mAlertDialog.findViewById<TextView>(R.id.tv_dismiss)
                dismiss.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }

            GetWinner.COMPUTER -> {
                mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_computer_wins, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                val playAgain = mAlertDialog.findViewById<TextView>(R.id.tv_play_again)

                playAgain.setOnClickListener {
                    playerScore = 0
                    computerScore = 0
                    viewBinding()
                    setOnClickListener()
                    mAlertDialog.dismiss()
                    FancyToast.makeText(
                        this,
                        "Game Reset Successfully!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                    Log.d(TAG, "Game Reset Successfully!")
                }

                val dismiss = mAlertDialog.findViewById<TextView>(R.id.tv_dismiss)
                dismiss.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
        }

    }

    private fun gameFinished() {
        if (playerScore == 3) {
            binding.tvScore.text = getString(R.string.text_you_win)
            binding.tvVs.textSize = 16F
            binding.tvVs.text = getString(R.string.text_game_over)
            showWinnerDialog(GetWinner.PLAYER)

            Log.d(TAG, "gameFinished: ===PLAYER WINS===")
        } else if (computerScore == 3) {
            binding.tvScore.text = getString(R.string.text_com_wins)
            binding.tvVs.textSize = 16F
            binding.tvVs.text = getString(R.string.text_game_over)
            showWinnerDialog(GetWinner.COMPUTER)

            Log.d(TAG, "gameFinished: ===COM WINS===")
        }
    }
}