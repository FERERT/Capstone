package com.course.translateapp.translator

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MainIconButton(icon: Int, onClick: () -> Unit){
    IconButton(onClick = onClick) {
        Icon(painter = painterResource(id = icon), contentDescription = "", modifier = Modifier.size(24.dp))
        
    }

}