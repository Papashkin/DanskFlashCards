package com.antsfamily.danskflashcards.ui.about

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsfamily.danskflashcards.R
import com.antsfamily.danskflashcards.core.presentation.TopBar
import com.antsfamily.danskflashcards.ui.theme.FontSize
import com.antsfamily.danskflashcards.ui.theme.Padding

@Composable
fun AboutScreen(
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val appVersion = remember { getAppVersion(context) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding.medium, vertical = Padding.small)
    ) {
        TopBar(onNavigationBack = navigateBack)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_image_bg),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }

            Text(
                text = stringResource(R.string.about_development_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = Padding.large),
            )
            Text(
                text = stringResource(R.string.about_year_2025),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Padding.xSmall),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.about_app_version, appVersion),
                fontSize = FontSize.Caption,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = Padding.large)
            )
        }
    }
}

fun getAppVersion(context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    AboutScreen {}
}
