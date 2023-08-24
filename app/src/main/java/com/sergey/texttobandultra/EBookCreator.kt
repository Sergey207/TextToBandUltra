package com.sergey.texttobandultra

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.sergey.texttobandultra.dialogs.AskFilePermissionDialog
import com.sergey.texttobandultra.ui.theme.TextToBandUltraTheme
import com.sergey.texttobandultra.widgets.FilesCard

class EBookCreator : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextToBandUltraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreatorScreen()
                }
            }
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "this.startActivity(Intent(this, MainActivity::class.java))",
            "android.content.Intent"
        )
    )
    override fun onBackPressed() {
        this.startActivity(Intent(this, MainActivity::class.java))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreatorScreen() {
    val context = LocalContext.current
    val successText = stringResource(id = R.string.app_created_successfully)
    val errorText = stringResource(id = R.string.error_message)
    val writeFilesPermission = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val isDialog = remember { mutableStateOf(false) }
    val hasPermission = remember { mutableStateOf(false) }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        hasPermission.value = true
    else if (writeFilesPermission.status.isGranted)
        hasPermission.value = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(10f, fill = true),
        )
        {
            items(tabs.size) { index -> FilesCard(tab = tabs[index]) }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    if (hasPermission.value)
                        saveApp(context, successText, errorText)
                    else
                        isDialog.value = true
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = stringResource(id = R.string.create_app), fontSize = 20.sp)
            }
        }
    }
    if (isDialog.value) {
        AskFilePermissionDialog(
            dialogState = isDialog,
            context = context,
            permissionState = writeFilesPermission
        )
    }
}
