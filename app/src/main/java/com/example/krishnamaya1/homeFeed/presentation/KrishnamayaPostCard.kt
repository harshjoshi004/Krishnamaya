package com.example.krishnamaya1.homeFeed.presentation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.krishnamaya1.addPost.data.KrishnamayaPost
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.example.krishnamaya1.ui.theme.ElevatedMustard2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun KrishnamayaPostCard(post:KrishnamayaPost, userName:String?,  imageUrl:String?){
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        val (profileRow, postDetails) = createRefs()

        //profile details row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(profileRow) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
        ) {
            Image(
                painter = if (imageUrl == null) painterResource(id = R.drawable.defimg)
                else rememberAsyncImagePainter(model = imageUrl),
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(8.dp))

            Column {
                BoldSubheading(text = userName ?: "User Name", color = ElevatedMustard2)
                Text( fontWeight = FontWeight.Normal, fontSize = 12.sp,
                    text = formatTimestamp(post.timeStamp), color = ElevatedMustard2)
            }
        }

        //post details column
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(postDetails) {
                    top.linkTo(profileRow.bottom);
                    start.linkTo(parent.start)
                }
        ){
            Spacer(modifier = Modifier.size(8.dp))

            Subheading(text = post.postText, Modifier.fillMaxWidth(), textAlign = TextAlign.Start)

            Spacer(modifier = Modifier.size(8.dp))

            post.postImage?.let {
                Box(modifier = Modifier.fillMaxWidth()){
                    Image(
                        painter = rememberAsyncImagePainter(model = it),
                        modifier = Modifier
                            .height(250.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(15.dp)),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}

fun formatTimestamp(timestamp: String): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val date = Date(timestamp.toLong())  // Convert the timestamp to a Date object
    return sdf.format(date)  // Return the formatted date string
}