package com.sergey.texttobandultra.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sergey.texttobandultra.checkName
import com.sergey.texttobandultra.tabs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTabDialog(
    indexOfTitle: Int,
    dialogState: MutableState<Boolean>,
    context: Context
) {
    val newTabTitle = remember { mutableStateOf(tabs[indexOfTitle].title) }
    val oldName = tabs[indexOfTitle].title
    Dialog(onDismissRequest = { dialogState.value = false }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    value = newTabTitle.value,
                    label = { Text(text = "New tab name") },
                    onValueChange = { newTabTitle.value = it.replace("\n", "") })

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(shape = RoundedCornerShape(5.dp), onClick = {
                        dialogState.value = false
                    }) {
                        Text(text = "Cancel")
                    }

                    Button(shape = RoundedCornerShape(5.dp), onClick = {
                        val error = checkName(newTabTitle.value, oldName)
                        if (error.isNotEmpty())
                            Toast.makeText(
                                context,
                                error,
                                Toast.LENGTH_SHORT
                            ).show()
                        else {
                            tabs[indexOfTitle].title = newTabTitle.value
                            dialogState.value = false
                        }
                    }
                    ) {
                        Text(text = "Rename")

                    }
                }
            }
        }
    }
}