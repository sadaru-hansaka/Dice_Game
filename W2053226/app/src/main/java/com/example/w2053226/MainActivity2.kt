package com.example.w2053226

import android.os.Bundle
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w2053226.ui.theme.W2053226Theme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import kotlin.random.Random

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var playerScore by rememberSaveable { mutableStateOf(0) }
            var computerScore by rememberSaveable { mutableStateOf(0) }

            var playerDice by rememberSaveable{ mutableStateOf(listOf<Int>()) }
            var computerDice by rememberSaveable { mutableStateOf(listOf<Int>()) }

            var scoreButton by remember { mutableStateOf(false) }

            var result by remember { mutableStateOf("") }

            var showDialog by remember { mutableStateOf(false) }
            var dialogMessage by remember { mutableStateOf("") }
            var dialogTitle by remember { mutableStateOf("") }
            var gameOver by remember { mutableStateOf(false) }

            val diceImages = listOf(
                R.drawable.dice1,
                R.drawable.dice2,
                R.drawable.dice3,
                R.drawable.dice4,
                R.drawable.dice5,
                R.drawable.dice6,
            )

            var selectedDice by remember { mutableStateOf(List(5) { false }) }
            var throwCount by remember { mutableStateOf(0) }
            var computerThrowCount by remember { mutableStateOf(0) }
            var tieOption by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
            ){
                Column (
                    modifier = Modifier.fillMaxWidth().padding(0.dp,50.dp,20.dp,0.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = "Computer Score: $computerScore", fontSize = 18.sp )
                    Text(text = "Your Score: $playerScore", fontSize = 18.sp)
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

                        Text("- You -", modifier = Modifier.align(Alignment.CenterHorizontally))
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
                            if(playerScore >= 101 || computerScore >= 101){
                                if(playerScore > computerScore) {
                                    dialogMessage = "You Win"
                                    dialogTitle = "Game Over"
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
                                computerThrowCount = 1
                            } else {
                                println("Player rerolls selected dice")
                                playerDice = playerDice.mapIndexed { index, dice ->
                                    if (!selectedDice[index]) diceImages[Random.nextInt(0, 6)] else dice
                                }

                                selectedDice = List(5) { false }

                            }
                            throwCount++
                            // Computer's turn to reroll (if it hasn't used all rolls)
                            if (computerThrowCount < 3 && throwCount < 3) {
                                println("Computer performing reroll ${computerThrowCount}/2")
                                computerDice = computerReroll(diceImages, computerDice)
                                computerThrowCount++
                            }
                        }

                        if(throwCount == 3 ){
                            while (computerThrowCount < 3) {
                                println("Computer using reroll ${computerThrowCount}/2") // Log reroll count
                                computerDice = computerReroll(diceImages, computerDice) // Call computerReroll
                                computerThrowCount++
                            }

                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)
                            scoreButton = true
                            throwCount = 0
                            computerThrowCount = 0

                            if(playerScore >= 101 || computerScore >= 101){
                                if(playerScore > computerScore) {
                                    dialogMessage = "You Win"
                                    dialogTitle = "Game Over"
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

                    Button(onClick = {
                        if(!scoreButton) {

                            // Player scores: Computer uses all remaining rolls
                            while (computerThrowCount < 3) {
                                println("Computer using reroll ${computerThrowCount}/2")
                                computerDice = computerReroll(diceImages, computerDice)
                                computerThrowCount++
                            }

                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)

                            scoreButton = true
                            throwCount = 0
                            computerThrowCount = 0
                        }
//                        find winner
                        if(playerScore >= 101 || computerScore >= 101){
                            if(playerScore > computerScore) {
                                dialogMessage = "You Win"
                                dialogTitle = "Game Over"
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
                modifier = Modifier.size(50.dp)
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
    // Randomly decide whether to reroll (50% chance)
    val shouldReroll = Random.nextBoolean()
    println("Computer decides to reroll: $shouldReroll")

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
