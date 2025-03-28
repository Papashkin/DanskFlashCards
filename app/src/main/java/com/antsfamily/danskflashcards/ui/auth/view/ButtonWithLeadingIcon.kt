package com.antsfamily.danskflashcards.ui.auth.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.ui.theme.Padding
import com.antsfamily.danskflashcards.ui.theme.alert
import com.antsfamily.danskflashcards.ui.theme.wistful_1000

@Composable
fun ButtonWithLeadingIcon(
    modifier: Modifier = Modifier,
    stringId: Int,
    iconId: Int,
    isLoading: Boolean,
    errorText: String? = null,
    onClick: () -> Unit
) {
    Box {
        Column {
            Button(
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = wistful_1000
                ),
                border = BorderStroke(0.5.dp, wistful_1000),
                onClick = { onClick() },
                shape = RoundedCornerShape(Padding.xLarge),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = wistful_1000,
                    )
                } else {
                    ButtonContent(modifier, stringId, iconId)
                }
            }
            Text(
                text = errorText.orEmpty(),
                color = alert,
                minLines = 2,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun ButtonContent(modifier: Modifier, stringId: Int, iconId: Int) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
        ) {
            Image(
                modifier = Modifier.padding(start = Padding.large),
                painter = painterResource(iconId),
                contentDescription = null
            )
        }
        Text(
            text = stringResource(id = stringId),
            color = wistful_1000,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonWithLeadingIconPreview() {
    Column(Modifier.fillMaxSize()) {
        ButtonWithLeadingIcon(
            Modifier,
            R.string.sign_up_google,
            R.drawable.ic_google,
            false,
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithLeadingIcon(
            Modifier,
            R.string.sign_up_google,
            R.drawable.ic_google,
            true,
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithLeadingIcon(
            Modifier,
            R.string.sign_up_google,
            R.drawable.ic_google,
            false,
            "Something went wrong.\nPlease try it later."
        ) {}
        Spacer(modifier = Modifier.height(20.dp))
        ButtonWithLeadingIcon(
            Modifier,
            R.string.sign_up_google,
            R.drawable.ic_google,
            false,
            "Something went wrong. Please try it later. Or test the really long error text explanation"
        ) {}
    }
}