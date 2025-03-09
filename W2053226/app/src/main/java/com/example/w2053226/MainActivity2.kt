package com.example.w2053226

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.w2053226.ui.theme.W2053226Theme
import kotlin.random.Random

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var score by remember { mutableStateOf(0) }
            var displayedDice by remember { mutableStateOf(listOf<Int>()) }

            var scoreButton by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row {
                    displayedDice.forEach { image ->
                        Image(
                            painterResource(image),
                            contentDescription = "Dice Image",
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }


                Text("Score : $score")

                Spacer(Modifier.height(20.dp))

                Row{
                    Button(onClick = {
                        val diceImages = listOf(R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
                        displayedDice = List(5) {diceImages[Random.nextInt(6)]}
                        scoreButton = false
                    }) {
                        Text("Throw")
                    }

                    Spacer(Modifier.width(20.dp))

                    Button(onClick = {
                        if(!scoreButton) {
                            score += calculateScore(displayedDice)
                            scoreButton = true
                        }
                    }) {
                        Text("Score")
                    }
                }
            }
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


