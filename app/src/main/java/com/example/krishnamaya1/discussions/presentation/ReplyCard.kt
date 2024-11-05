package com.example.krishnamaya1.discussions.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.R
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.discussions.data.Reply
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun ReplyCard(
    user: KrishnamayaUser,
    reply: Reply
) {
    val name = user.userName
    val imageUrl = user.imageLink
    val text = reply.text
    val time = formatTimestamp(reply.timeStamp)

    // Reply Card UI
    ConstraintLayout(modifier = Modifier.padding(8.dp)) {
        val (userName, timeStamp, image, textString) = createRefs()
        // Image
        Image(
            painter =
            if(imageUrl == null || imageUrl.isBlank())
                painterResource(R.drawable.defimg)
            else
                rememberAsyncImagePainter(imageUrl),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(ElevatedMustard2)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        // User Name
        Text( fontWeight = FontWeight.Bold, fontSize = 18.sp, text = name, color = ElevatedMustard2,
            modifier = Modifier.constrainAs(userName) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(parent.top)
            }
        )
        // Time Stamp
        Text( fontWeight = FontWeight.Normal, fontSize = 10.sp, text = time, color = ElevatedMustard2,
            modifier = Modifier.constrainAs(timeStamp) {
                start.linkTo(userName.start)
                top.linkTo(userName.bottom)
            }
        )
        // Discussion Text
        Text( fontWeight = FontWeight.Normal, fontSize = 16.sp, text = text, color = Color.Black,
            modifier = Modifier.constrainAs(textString) {
                start.linkTo(parent.start)
                top.linkTo(image.bottom, margin = 8.dp)
            }
        )
    }
}