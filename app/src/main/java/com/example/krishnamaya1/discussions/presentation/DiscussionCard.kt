package com.example.krishnamaya1.discussions.presentation

import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.krishnamaya1.addPost.data.KrishnamayaPost
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.krishnamaya1.Subheading
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.Heading
import com.example.krishnamaya1.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.Paragraph
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.authentication.presentation.AuthSharedPreferences
import com.example.krishnamaya1.discussions.data.Discussion
import com.example.krishnamaya1.homeFeed.presentation.formatTimestamp
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale

@Composable
fun DiscussionCard(
    discussionViewModel: DiscussionViewModel,
    user: KrishnamayaUser,
    discussion: Discussion
) {
    val context = LocalContext.current
    var replyState by remember { mutableStateOf(false) }
    val name = user.userName
    val time = formatTimestamp(discussion.timeStamp.toString())
    val imageUrl = user.imageLink
    val text = discussion.text
    val clickReplyButton = { replyState = !replyState }

    ConstraintLayout(modifier = Modifier.padding(8.dp).animateContentSize()) {
        val (userName, timeStamp, image, textString, repliesButton, replies) = createRefs()

        // Image
        Image(contentScale = ContentScale.Crop,
            contentDescription = null,
            painter =
                if(imageUrl == null || imageUrl.isBlank())
                    painterResource(R.drawable.defimg)
                else
                    rememberAsyncImagePainter(imageUrl),
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ElevatedMustard2)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        // User Name
        Text( fontWeight = FontWeight.Bold, fontSize = 20.sp, text = name, color = ElevatedMustard2,
            modifier = Modifier.constrainAs(userName) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(parent.top)
            }
        )
        // Time Stamp
        Text( fontWeight = FontWeight.Normal, fontSize = 12.sp, text = time, color = ElevatedMustard2,
            modifier = Modifier.constrainAs(timeStamp) {
                start.linkTo(userName.start)
                top.linkTo(userName.bottom)
            }
        )
        // Discussion Text
        Text( fontWeight = FontWeight.Normal, fontSize = 18.sp, text = text, color = Color.Black,
            modifier = Modifier.constrainAs(textString) {
                start.linkTo(parent.start)
                top.linkTo(image.bottom, margin = 8.dp)
            }
        )
        // Replies Button
        TextButton(
            onClick = {clickReplyButton()},
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .constrainAs(repliesButton) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(textString.bottom)
                }
        ) {
            Row {
                Icon(contentDescription = null,
                    tint = ElevatedMustard2,
                    imageVector = if(replyState)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                )
                Text(text = if(replyState) "Hide Replies" else "Show Replies",
                    fontWeight = FontWeight.Bold, color = ElevatedMustard2
                )
            }
        }
        //replies
        if(replyState) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp).constrainAs(replies) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(repliesButton.bottom)
                }
            ) {
                AddingReplies(discussionViewModel, discussion)

                discussion.listOfReplies.forEach { reply ->
                    var userState by remember { mutableStateOf<KrishnamayaUser?>(null) }
                    discussionViewModel.getUserFromId(reply.userId) {
                        Log.d("Harsh", "DiscussionCard: $it")
                        userState = it
                    }
                    userState?.let {
                        ReplyCard(it, reply)
                    }
                }
            }
        }
    }
}

fun formatTimestamp(timestamp: String): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val date = Date(timestamp.toLong())
    return sdf.format(date)
}