package com.example.w2053226

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat.Style
import com.example.w2053226.ui.theme.W2053226Theme
import androidx.compose.material3.Text as Text

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GUI()
        }
    }

}

@Composable
fun GUI(){
    var openDialog by remember {mutableStateOf(false)}
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Dice Game",
            modifier = Modifier.padding(bottom = 60.dp),
            style = TextStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        )
        Button(onClick = {
            var i = Intent(context, MainActivity2::class.java)
            i.putExtra("humanWins",0)
            i.putExtra("computerWins", 0)
            (context as Activity).startActivityForResult(i, 1)
        }) {
            Text(text = "New Game")
        }
        Button(onClick = { openDialog=true }) {
            Text(text = "About")
        }

    }
    if(openDialog){
        AlertDialog(
            title = {
                Text(text = "About")
            },
            text = {
                Text(
                    text = "Student Id : w2053226\nName : Sadaru Hansaka\n\n" +
                            "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students.\n" +
                            "The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.\n"
                )
            },

            onDismissRequest = {
                openDialog = false
            },
            confirmButton = {
                Button(onClick = {
                    openDialog = false
                }) {
                    Text(text = "Ok")
                }
            },

        )
    }
}




