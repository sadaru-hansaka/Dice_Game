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
import androidx.compose.material3.Button
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
                        if(throwCount == 0) {
                            playerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                            computerDice = List(5) { diceImages[Random.nextInt(0, 6)] }
                            scoreButton = false
                        }else{
                            playerDice = playerDice.mapIndexed { index, dice ->
                                if (!selectedDice[index]) diceImages[Random.nextInt(0, 6)] else dice
                            }
                        }
                        throwCount++

                        if(throwCount == 3 ){
                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)
                            scoreButton = true
                            throwCount = 0
                        }
                    },
                    enabled = throwCount < 3
                    ) {
                        Text(if(throwCount ==  0) "Throw" else "Re Roll ($throwCount/2)")
                    }

                    Spacer(Modifier.width(20.dp))

                    Button(onClick = {
                        if(!scoreButton) {
                            playerScore += calculateScore(playerDice)
                            computerScore += calculateScore(computerDice)
                            scoreButton = true
                            throwCount = 0
                        }
                        if(playerScore >= 101 || computerScore >= 101){
                            result = if(playerScore>computerScore) "You Win" else "You Lose"
                        }
                        selectedDice = List(5) { false }
                    }
                    ) {
                        Text("Score")
                    }
                }

                Spacer(Modifier.height(50.dp))

                Text("$result", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 20.sp)
            }
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


