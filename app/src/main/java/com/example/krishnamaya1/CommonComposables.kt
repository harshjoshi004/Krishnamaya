package com.example.krishnamaya1

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.krishnamaya1.ui.theme.BackgroundMustard
import com.example.krishnamaya1.ui.theme.ElevatedMustard1
import com.example.krishnamaya1.ui.theme.ElevatedMustard2

@Composable
fun CommonButtonElevated(text:String, modifier: Modifier = Modifier, onClick:()->Unit) {
    ElevatedButton(onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.elevatedButtonColors(
            contentColor = ElevatedMustard2,
            disabledContentColor = ElevatedMustard1
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun CommonButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = ElevatedMustard2,
            contentColor = Color.White,
            disabledContainerColor = ElevatedMustard1,
            disabledContentColor = Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

fun myToast(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun myLoaderDialogue(context: Context): ProgressDialog {
    return ProgressDialog(context).apply {
        setMessage("Loading, please wait...")
        setTitle("Krishnamaya")
        setIcon(R.drawable.logo)
        setCancelable(false) // Disable cancelling
    }
}

fun myBasicDialogue(context: Context, text: String): ProgressDialog {
    return ProgressDialog(context).apply {
        setMessage(text)
        setTitle("Krishnamaya")
        setIcon(R.drawable.logo)
    }
}

@Composable
fun Heading(modifier: Modifier = Modifier, text:String, color: Color = Color.Black, textAlign:TextAlign = TextAlign.Center){
    Text(
        text = text,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    )
}

@Composable
fun Subheading(text: String, modifier: Modifier = Modifier, color: Color = Color.Black, textAlign:TextAlign = TextAlign.Center){
    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        textAlign = textAlign,
        modifier = modifier
    )
}

@Composable
fun BoldSubheading(modifier: Modifier = Modifier, text:String, color: Color = Color.Black, textAlign:TextAlign = TextAlign.Center){
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        fontSize = 16.sp
    )
}

@Composable
fun Paragraph(modifier: Modifier = Modifier, text:String, color: Color = Color.Black, textAlign:TextAlign = TextAlign.Center){
    Text(
        text = text,
        color = color,
        modifier = modifier,
        fontWeight = FontWeight.Light,
        textAlign = textAlign,
        fontSize = 12.sp
    )
}

@Composable
fun LoadingScreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundMustard)
    ){
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}