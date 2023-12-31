package com.sergey.texttobandultra.dialogs

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.sergey.texttobandultra.MainActivity
import com.sergey.texttobandultra.R
import com.sergey.texttobandultra.saveApp


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AskFilePermissionDialog(
    dialogState: MutableState<Boolean>,
    context: Context,
    permissionState: PermissionState,
) {
    if (permissionState.status.isGranted) {
        dialogState.value = false
        saveApp(
            context,
            stringResource(id = R.string.app_created_successfully),
            stringResource(id = R.string.error_message)
        )
    }

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
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.you_need_allow),
                    fontSize = 20.sp
                )  // TODO
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
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    Button(shape = RoundedCornerShape(5.dp), onClick = {
                        permissionState.launchPermissionRequest()
                    }) {
                        Text(text = stringResource(id = R.string.allow))
                    }
                }
            }
        }
    }
}