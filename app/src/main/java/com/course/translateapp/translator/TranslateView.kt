package com.course.translateapp.translator

import android.graphics.drawable.Icon
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
//import com.course.translateapp.Manifest
import com.course.translateapp.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun TranslateView(viewModel: TranslateViewModel){

    val state = viewModel.state
    val context = LocalContext.current //toast
    val keyboardController = LocalSoftwareKeyboardController.current
    val languageOptions = viewModel.languageOptions
    val itemsSelection = viewModel.itemSelection
    val itemsVoice = viewModel.itemsVoice
    var indexSource by remember { mutableStateOf(0) }
    var indexTarget by remember { mutableStateOf(1) }
    var expandedSource by remember { mutableStateOf(false) }
    var expandedTarget by remember { mutableStateOf(false) }
    var selectedSourceLang by remember { mutableStateOf(languageOptions[0]) }
    var selectedTargetLang by remember { mutableStateOf(languageOptions[1]) }
    var selectedTargetVoice by remember { mutableStateOf(itemsVoice[1]) }

    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)

    SideEffect {
        permissionState.launchPermissionRequest()
    }

    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {
            viewModel.onValue(it.toString().replace("[", "").replace("]","").trimStart())

        })


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Row (verticalAlignment = Alignment.CenterVertically){
            DropdownLang(
                itemSelection = itemsSelection,
                selectedIndex = indexSource,
                expand = expandedSource,
                onClickExpanded = { expandedSource = true },
                onClickDismiss = { expandedSource = false },
                onClickItem = {index ->
                    indexSource = index
                    selectedSourceLang = languageOptions[index]
                    expandedSource = false
                }
            )

            Icon(Icons.Default.ArrowForward, contentDescription = "",
                modifier = Modifier.padding(start = 15.dp, end = 15.dp))

            DropdownLang(
                itemSelection = itemsSelection,
                selectedIndex = indexTarget,
                expand = expandedTarget,
                onClickExpanded = { expandedTarget = true },
                onClickDismiss = { expandedTarget = false },
                onClickItem = {index ->
                    indexTarget = index
                    selectedTargetLang = languageOptions[index]
                    selectedTargetVoice = itemsVoice[index]
                    expandedTarget = false
                }
            )
        }
        
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(value = state.textToTranslate,
            onValueChange = {viewModel.onValue(it)},
            label = { Text(text = "Introduce your text")},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.onTranslate(
                        state.textToTranslate,
                        context,
                        selectedSourceLang,
                        selectedTargetLang
                    )
                }
            ),
            colors =  TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
            )

        Row (verticalAlignment = Alignment.CenterVertically){
            MainIconButton(icon = R.drawable.mic) {
                if (permissionState.status.isGranted){
                    speechRecognitionLauncher.launch(Unit)
                }else{
                    permissionState.launchPermissionRequest()
                }

            }
            MainIconButton(icon = R.drawable.translate) {
                viewModel.onTranslate(
                    state.textToTranslate,
                    context,
                    selectedSourceLang,
                    selectedTargetLang
                )

            }
            MainIconButton(icon = R.drawable.speak) {
                viewModel.textToSpeech(context, selectedTargetVoice)

            }
            MainIconButton(icon = R.drawable.delete) {
                viewModel.clean()

            }

        }

        if (state.isDownloading){
            CircularProgressIndicator()
            Text(text = "Downloading, please wait a moment")
        } else {
            OutlinedTextField(value = state.translateText,
                onValueChange = {},
                label = { Text(text = "Text Translated")},
                readOnly = false,
                colors =  TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                )


        }

    }

}