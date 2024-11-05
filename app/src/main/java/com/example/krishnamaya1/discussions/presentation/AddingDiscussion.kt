package com.example.krishnamaya1.discussions.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Upload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.discussions.data.Reply
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddingDiscussion(discussionViewModel: DiscussionViewModel) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val onClickButton:(String)->Unit = { str->
        discussionViewModel.addDiscussion(
            text = str,
            onSuccess = {
                Toast.makeText(context, "Discussion Added!", Toast.LENGTH_SHORT).show()
                text = ""
                discussionViewModel.getDiscussions()
            },
            onFailure = {
                Toast.makeText(context, "Error Occurred: $it", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Row (
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Start Discussion") },
            maxLines = 1,
            modifier = Modifier.fillMaxSize().weight(1f)
        )
        IconButton(
            onClick = {
                if(text.isNotBlank())
                    onClickButton(text)
                else
                    Toast.makeText(context, "Discussion cannot be empty", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
        ) {
            Icon(Icons.Default.AddCircle, null,
                modifier = Modifier.size(35.dp),
                tint = ElevatedMustard2)
        }
    }
}