package com.example.w2053226

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w2053226.ui.theme.W2053226Theme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.random.Random

var updatedHumanWins by mutableStateOf(0)
var updatedComputerWins by mutableStateOf(0)

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isHardMode by rememberSaveable { mutableStateOf(false) }

            var playerScore by rememberSaveable { mutableStateOf(0) }
            var computerScore by rememberSaveable { mutableStateOf(0) }

            var playerDice by rememberSaveable{ mutableStateOf(listOf<Int>()) }
            var computerDice by rememberSaveable { mutableStateOf(listOf<Int>()) }

            var scoreButton by rememberSaveable { mutableStateOf(false) }

            var result by rememberSaveable { mutableStateOf("") }

            var showDialog by rememberSaveable { mutableStateOf(false) }
            var dialogMessage by rememberSaveable { mutableStateOf("") }
            var dialogTitle by rememberSaveable { mutableStateOf("") }
            var gameOver by rememberSaveable { mutableStateOf(false) }

            var showTargetInputDialog by rememberSaveable { mutableStateOf(true) }
            var targetScore by remember { mutableStateOf(101)}
            val diceImages = listOf(
                R.drawable.dice1,
                R.drawable.dice2,
                R.drawable.dice3,
                R.drawable.dice4,
                R.drawable.dice5,
                R.drawable.dice6,
            )

            var selectedDice by rememberSaveable { mutableStateOf(List(5) { false }) }
            var throwCount by rememberSaveable { mutableStateOf(0) }
            var computerThrowCount by rememberSaveable { mutableStateOf(0) }
            var tieOption by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp,50.dp,0.dp,0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(start = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "H: $updatedHumanWins / C: $updatedComputerWins",
                            fontSize = 18.sp
                        )
                    }
                    Column(
                        modifier = Modifier.padding(end = 20.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(text = "Target Score: $targetScore", fontSize = 18.sp)
                        Text(text = "Computer Score: $computerScore", fontSize = 18.sp)
                        Text(text = "Human Score: $playerScore", fontSize = 18.sp)
                    }
                }

                Box (
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ){
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ){
                        Text("- Computer -", modifier = Modifier.align(Alignment.CenterHorizontally))
                        displayDices(computerDice)

                        Spacer(Modifier.height(20.dp))

                        Text("- Human -", modifier = Modifier.align(Alignment.CenterHorizontally))
//                        displayDices(playerDice)
                        displayDices(
                            diceImages = playerDice,
                            selectedDice = selectedDice,
                            onDiceClick = { index ->
                                selectedDice = selectedDice.toMutableList().also {
                                    it[index] = !it[index]
                                }
                            }
                        )
                    }
                }


                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding( 0.dp,0.dp,0.dp,60.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ){
                    Button(onClick = {
                        if(tieOption){
                            tieOption = false
                            playerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                            computerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)
                            scoreButton = true
                            if(playerScore >= targetScore || computerScore >= targetScore){
                                if(playerScore > computerScore) {
                                    dialogMessage = "You Win"
                                    dialogTitle = "Game Over"
                                    updatedHumanWins ++
                                    showDialog = true
                                    gameOver = true

                                }else if(playerScore == computerScore){
                                    dialogTitle = "Draw"
                                    dialogMessage = "You have a another chance"
                                    showDialog = true
                                    tieOption = true
                                }else{
                                    dialogTitle = "Game Over"
                                    dialogMessage = "You Lose"
                                    updatedComputerWins ++
                                    showDialog = true
                                    gameOver = true

                                }
                            }
                            throwCount == 3

                        }else {

                            if (throwCount == 0) {
                                println("First throw: Initializing player and computer dice")
                                playerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                                computerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                                scoreButton = false
                                computerThrowCount = 0
                            } else {
                                println("Player rerolls selected dice")
                                playerDice = playerDice.mapIndexed { index, dice ->
                                    if (!selectedDice[index]) diceImages[Random.nextInt(0, 6)] else dice
                                }

                                selectedDice = List(5) { false }

                            }
                            throwCount++
                            if(!isHardMode) {
                                println("Random Stratergy ----------------------------------------------")
                                // Computer's turn to reroll (if it hasn't used all rolls)
                                if (computerThrowCount < 3 && throwCount < 3) {
                                    println("Computer performing reroll ${computerThrowCount}/2")
                                    computerDice = computerReroll(diceImages, computerDice)
                                    computerThrowCount++
                                }
                            }
                        }

                        if(throwCount == 3 ){
                            if(!isHardMode) {
                                println("Random Stratergy ----------------------------------------------")
                                while (computerThrowCount < 3) {
                                    println("Computer using reroll ${computerThrowCount}/2") // Log reroll count
                                    computerDice = computerReroll(
                                        diceImages,
                                        computerDice
                                    ) // Call computerReroll
                                    computerThrowCount++
                                }
                            }else{
                                //                                hard mode
                                println("Computer performing computer strategy-------------------------------------")
                                computerDice = ComputerStratergy(diceImages, computerDice, playerScore, computerScore, targetScore)
                                computerThrowCount += 2
                            }

                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)
                            scoreButton = true
                            throwCount = 0
                            computerThrowCount = 0

                            if(playerScore >= targetScore || computerScore >= targetScore){
                                if(playerScore > computerScore) {
                                    dialogMessage = "You Win"
                                    dialogTitle = "Game Over"
                                    updatedHumanWins ++
                                    showDialog = true
                                    gameOver = true

                                }else if(playerScore == computerScore){
                                    dialogTitle = "Draw"
                                    dialogMessage = "You have a another chance"
                                    showDialog = true
                                    tieOption = true
                                }else{
                                    dialogTitle = "Game Over"
                                    dialogMessage = "You Lose"
                                    updatedComputerWins ++
                                    showDialog = true
                                    gameOver = true

                                }

                            }
                        }
                    },
                    enabled = throwCount < 3 && !gameOver
                    ) {
                        Text(if(throwCount ==  0) "Throw" else "Re Roll ($throwCount/2)")
                    }

                    Spacer(Modifier.width(20.dp))
//--------------------------------------------------------------------------------------------------------------------------------------score button
                    Button(onClick = {
                        if(!scoreButton) {

                            if(!isHardMode) {
                                // Player scores: Computer uses all remaining rolls
                                while (computerThrowCount < 3) {
                                    println("Computer using reroll ${computerThrowCount}/2")
                                    computerDice = computerReroll(diceImages, computerDice)
                                    computerThrowCount++
                                }
                            }else{
                                println("Computer performing computer strategy-------------------------------------")
                                computerDice = ComputerStratergy(diceImages, computerDice, playerScore, computerScore, targetScore)
                                computerThrowCount += 2
                            }

                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)

                            scoreButton = true
                            throwCount = 0
                            computerThrowCount = 0
                        }
//                        find winner
                        if(playerScore >= targetScore || computerScore >= targetScore){
                            if(playerScore > computerScore) {
                                dialogMessage = "You Win"
                                dialogTitle = "Game Over"
                                updatedHumanWins ++
                                showDialog = true
                                gameOver = true

                            }else if(playerScore == computerScore){
                                dialogTitle = "Draw"
                                dialogMessage = "You have a another chance"
                                showDialog = true
                                tieOption = true
                            }else{
                                dialogMessage = "You Lose"
                                dialogTitle = "Game Over"
                                updatedComputerWins ++
                                showDialog = true
                                gameOver = true

                            }

                        }
//                        reset selected dices
                        selectedDice = List(5) { false }
                    },
                        enabled = !gameOver
                    ) {
                        Text("Score")
                    }
                }

                Spacer(Modifier.height(50.dp))

                Text("$result", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 20.sp)
            }
            DialogBox(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                title = dialogTitle ,
                message = dialogMessage,
                passedColor = if(dialogMessage == "You Win") Color.Green else if(dialogMessage == "You Lose") Color.Red else Color.White
            )

            TargetInputDialog(
                showDialog = showTargetInputDialog,
                onDismiss = { showTargetInputDialog = false },
                onConfirm = { target, isHard ->
                    targetScore = target
                    isHardMode = isHard
                }
            )
        }
    }
}

@Composable
fun displayDices(diceImages: List<Int>, selectedDice:List<Boolean> = List(5) {false}, onDiceClick: (Int) -> Unit = {}){
    Row{
        diceImages.forEachIndexed() { index, image ->
            Image(
                painterResource(image),
                contentDescription = "Dice Image",
                modifier = Modifier.size(40.dp)
                    .clickable { onDiceClick(index) }
                    .border(if(selectedDice[index]) 5.dp else 0.dp, color = Color.Black)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

//calculates the score according to the image[use immutable map]
fun calculateScore(diceImages: List<Int>): Int {
    val diceValues = mapOf(
        R.drawable.dice1 to 1,
        R.drawable.dice2 to 2,
        R.drawable.dice3 to 3,
        R.drawable.dice4 to 4,
        R.drawable.dice5 to 5,
        R.drawable.dice6 to 6
    )
    return diceImages.sumOf { diceValues[it] ?: 0 }
}

//Alert Dialog Box(can pass text, bg color)
@Composable
fun DialogBox(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    message: String,
    title: String,
    passedColor : Color = Color.White,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            containerColor = passedColor,
            title = { Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){Text(title) }},
            text = { Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {Text(message )} },
            confirmButton = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )

                    ) {
                        Text("OK")
                    }
                }
            }
        )
    }
}


// Function to simulate computer's random reroll strategy
fun computerReroll(diceImages: List<Int>, currentDice: List<Int>): List<Int> {
    println("\n=== COMPUTER RANDOM STRATEGY START ===")

    println("Decides to reroll or stay with avalable score")
    val shouldReroll = Random.nextBoolean()
    println("Computer Desides : $shouldReroll")

    if (!shouldReroll) {
        println("Computer keeps the current dice: $currentDice")
        return currentDice // Keep the current dice
    }

    // Randomly decide which dice to keep or reroll
    val newDice =  currentDice.map { dice ->
        if (Random.nextBoolean()){
            println("Computer keeps dice: $dice")
            dice
        }else{
            val newDiceValues = diceImages[Random.nextInt(0, 6)]
            println("Computer rerolls dice: $dice -> $newDiceValues") // Log rerolled dice
            newDiceValues
        }
    }
    println("Computer's updated dice: $newDice") // Log updated dice
    return newDice
}


@Composable
fun TargetInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int, Boolean) -> Unit  // (targetScore, isHardMode)
) {
    var targetInput by remember { mutableStateOf("101") }  // Default target score
    var selectedMode by remember { mutableStateOf("easy") }  // "easy" or "hard"

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Game Setup") },
            text = {
                Column {
                    // Target score input
                    Text("Enter target score:")
                    TextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // Mode selection (radio buttons)
                    Text("Select game mode:")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == "easy",
                            onClick = { selectedMode = "easy" }
                        )
                        Text("Easy Mode", modifier = Modifier.clickable { selectedMode = "easy" })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedMode == "hard",
                            onClick = { selectedMode = "hard" }
                        )
                        Text("Hard Mode", modifier = Modifier.clickable { selectedMode = "hard" })
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = targetInput.toIntOrNull() ?: 101  // Fallback to 101 if invalid
                        onConfirm(target, selectedMode == "hard")
                        onDismiss()  // Close the dialog permanently
                    }
                ) {
                    Text("Start Game")
                }
            },
            dismissButton = null  // Remove "Cancel" to force a selection
        )
    }
}

/**
 * Computer Hard Strategy
 *
 * When user starts a new game alertdialog box will display asking for target score and game mode. If user choose Hard mode this function calls.
 *
 * Strategy :-
 *  This function tacked list of dice images, and computer's current dice list, player score and computer score.
 *  Then it checks score difference between playerScore and computerScore.And also it calculates the sum of that list.
 *  All the bellow process works on this data.
 *
 *  In here if the score difference is lower that 5 or equal to five. That means computer has less chance to win.Because the score difference can be
 *  a minus value too.
 *  And in last round computer may be short with a very less value in that case computer decides to re roll.
 *
 *  Computer side also have two re roll chances as the player.
 *
 *  When choosing what dices to re roll computer follows this steps,
 *      first it gets indexes of dices which is lesser than 5. Then only that indexes roll again using random function.
 *
 *  One re roll chance is done. And again system checks the sum of new list.
 *
 *  Then system checks sum of the new list, then if the sum is lower than or equal to 15 or the sum different is reduced.
 *  System takes the second re roll chance.
 *
 *  Then again system choose the indexes of values lower than 5 and roll again.
 *  That's the last chance
 *
 *  Then it display on the screen
 */

fun ComputerStratergy(diceImages: List<Int>, currentDice: List<Int>, playerScore: Int, computerScore: Int, targetScore: Int): List<Int> {
    println("\n=== COMPUTER STRATEGY START ===")
    println("Initial Dice: ${currentDice.map { it.toString().substringAfterLast(".") }}")

    // Mapping of dice images to their numeric values
    val diceValues = mapOf(
        R.drawable.dice1 to 1,
        R.drawable.dice2 to 2,
        R.drawable.dice3 to 3,
        R.drawable.dice4 to 4,
        R.drawable.dice5 to 5,
        R.drawable.dice6 to 6
    )

    // Convert current dice to their numeric values (1-6)
    var currentDiceValues = currentDice.map { diceValues[it] ?: 0 }
    var currentSum = currentDiceValues.sum() // Simple sum of numbers
    println("Numeric Values: $currentDiceValues (Sum: $currentSum)")

    val scoreDifference = computerScore - playerScore
    val remain = targetScore - computerScore
    println("Score Difference: $scoreDifference, Remaining to Target: $remain")

    var rerollFlag = false
    var rerollsUsed = 0

    if (scoreDifference <= 5 || remain < 10 && currentSum < 10) {
        rerollFlag = true
        println("Reroll condition met (scoreDifference <= 5 || remain < 10 && currentSum < 10)")
    }

    if (rerollFlag && rerollsUsed < 2) {
        println("\n--- REROLL ATTEMPT ${rerollsUsed + 1} ---")

        // Select dice to reroll: prioritize rerolling dice with values less than 5
        val diceToReroll = currentDiceValues.mapIndexed { index, value ->
            if (value < 5) index else null
        }.filterNotNull()
        println("Dice to reroll (values < 5): $diceToReroll")

        // Perform the reroll by generating new numbers (1-6)
        currentDiceValues = currentDiceValues.mapIndexed { index, value ->
            if (index in diceToReroll) Random.nextInt(1, 7) else value
        }

        rerollsUsed++
        val newSum = currentDiceValues.sum()
        println("After reroll: $currentDiceValues (Sum: $newSum)")

        var sumDifference = newSum - currentSum

        if (newSum < 15 || sumDifference > 0) {
            println("\n--- REROLL ATTEMPT ${rerollsUsed + 1} ---")

            val diceToReroll = currentDiceValues.mapIndexed { index, value ->
                if (value < 5) index else null
            }.filterNotNull()
            println("Dice to reroll (values < 5): $diceToReroll")

            println("Reroll didn't improve or exceeded target, trying again")
            currentDiceValues = currentDiceValues.mapIndexed { index, value ->
                if (index in diceToReroll) Random.nextInt(1, 7) else value
            }
            currentSum = currentDiceValues.sum()
            println("After second attempt: $currentDiceValues (Sum: $currentSum)")
        }
    }

    // Convert back to drawable IDs
    val finalDice = currentDiceValues.map { value ->
        when (value) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            6 -> R.drawable.dice6
            else -> R.drawable.dice1
        }
    }

    println("Final Dice: ${finalDice.map { it.toString().substringAfterLast(".") }}")
    println("=== COMPUTER STRATEGY END ===\n")

    return finalDice
}