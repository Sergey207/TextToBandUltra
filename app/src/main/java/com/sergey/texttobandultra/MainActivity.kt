package com.sergey.texttobandultra

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergey.texttobandultra.dialogs.AddTabDialog
import com.sergey.texttobandultra.ui.theme.TextToBandUltraTheme
import com.sergey.texttobandultra.widgets.TabsRow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextToBandUltraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

var DEBUG = false
const val DEBUG_TABS_NUM = 5

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Screen() {
    val context = LocalContext.current
    if (tabs.isEmpty())
        tabs.add(TextTab(stringResource(id = R.string.main)))

    if (DEBUG) {
        for (i in 1..DEBUG_TABS_NUM) {
            tabs.add(TextTab("Test tab $i", remember {
                mutableStateOf("Text for testing $i")
            }))
        }
        DEBUG = false
    }

//    val currentIndex = remember { mutableStateOf(0) }
    val dialogState = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabsRow(modifier = Modifier.weight(1f, fill = true), currentIndex)
            IconButton(
                onClick = { dialogState.value = true },
                modifier = Modifier
                    .padding(5.dp)
                    .background(colorScheme.primary, shape = RoundedCornerShape(10.dp))
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = "add button",
                    tint = colorScheme.onPrimary
                )
            }

        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = tabs[currentIndex.value].text.value,
            onValueChange = {
                tabs[currentIndex.value].text.value = it
            },
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true),
            label = { Text(text = stringResource(id = R.string.write_text_here)) },
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    context.startActivity(Intent(context, EBookCreator::class.java))
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(id = R.string.create_app), fontSize = 20.sp)
            }
        }
    }

    if (dialogState.value)
        AddTabDialog(dialogState, LocalContext.current)
}
