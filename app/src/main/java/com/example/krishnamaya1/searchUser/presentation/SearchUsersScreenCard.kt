package com.example.krishnamaya1.searchUser.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.krishnamaya1.BoldSubheading
import com.example.krishnamaya1.Paragraph
import com.example.krishnamaya1.Subheading
import com.example.krishnamaya1.authentication.data.KrishnamayaUser
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun SearchUsersCard(user: KrishnamayaUser, onClick: (KrishnamayaUser)->Unit){
    val consistentPadding = 8.dp

    // user click logic
    val userClick = {
        //onClick(user)
    }

    // rest of the ui
    Column {
        Spacer(modifier = Modifier.size(consistentPadding))

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ){
            val (image, name, email, bio, button) = createRefs()

            Image(
                painter = rememberAsyncImagePainter(model = user.imageLink),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, ElevatedMustard1, CircleShape)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            BoldSubheading(
                text = user.userName, textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(name) { start.linkTo(image.end); top.linkTo(image.top) }
            )

            Subheading(text = user.email,
                color = ElevatedMustard2, textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(email) { start.linkTo(image.end); top.linkTo(name.bottom) }
            )

            Paragraph(text = user.bio, textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(bio) { start.linkTo(image.end); top.linkTo(email.bottom) }
            )

            IconButton(onClick = { userClick() },
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(button) {
                        end.linkTo(parent.end); top.linkTo(parent.top)
                    }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null,
                    modifier = Modifier.size(28.dp), tint = ElevatedMustard2)
            }
        }

        Spacer(modifier = Modifier.size(consistentPadding))

        Divider(color = ElevatedMustard2)
    }
}