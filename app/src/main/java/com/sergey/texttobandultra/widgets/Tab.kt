package com.sergey.texttobandultra.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergey.texttobandultra.R
import com.sergey.texttobandultra.TextTab
import com.sergey.texttobandultra.dialogs.EditTabDialog
import com.sergey.texttobandultra.save
import com.sergey.texttobandultra.tabs
import com.sergey.texttobandultra.tabsPath

@Composable
fun Tab(
    tab: TextTab,
    tabNumber: Int,
    currentIndex: MutableState<Int>
) {
    val height = 57.dp
    val activeButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    )
    val disableButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary
    )
    val isDialogActive = remember { mutableStateOf(false) }

    val isActive = (tabNumber == currentIndex.value)
    Button(
        onClick = { currentIndex.value = tabNumber },
        modifier = Modifier
            .height(height)
            .padding(5.dp)
            .border(
                1.dp,
                if (isActive) Color.Transparent else Color.White,
                RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = if (isActive) activeButtonColors else disableButtonColors
    ) {
        Row {
            Text(text = "${tab.title}${if (tab.toSave.value) "*" else ""}", fontSize = 20.sp)

            IconButton(onClick = {
                isDialogActive.value = true
            }, modifier = Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "edit button",
                    tint = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            }

            if (!isActive) {
                IconButton(
                    onClick = {
                        if (currentIndex.value > tabNumber)
                            currentIndex.value--
                        tabs.remove(tabs[tabNumber])
                        tabsPath.resolve("${tab.title}.txt").delete()
                        tabs.save()
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "close button",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    if (isDialogActive.value)
        EditTabDialog(
            indexOfTitle = tabNumber,
            dialogState = isDialogActive,
            LocalContext.current
        )
}
