package com.codewithfk.newsapp.presentation.news_details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.codewithfk.newsapp.data.model.News
import com.codewithfk.newsapp.presentation.State
import com.codewithfk.newsapp.presentation.news_details.BookmarkAction

@Composable
fun NewsDetailsScreen(
    navController: NavController,
    news: News,
    isLocal: Boolean = false
) {
    NewsDetails(
        navController = navController,
        news = news,
        isLocal = isLocal
    )
}

@Composable
fun NewsDetails(
    navController: NavController,
    news: News,
    isLocal: Boolean = false
) {
    val viewModel: NewsDetailsViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.1f))
    ) {

        // IMAGE
        AsyncImage(
            model = news.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Crop
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            val (backBtn, topSpace, summary, contentBox) = createRefs()

            Spacer(
                modifier = Modifier
                    .height(350.dp)
                    .constrainAs(topSpace) {
                        top.linkTo(parent.top)
                    }
            )

            // BACK BUTTON
            Image(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top, margin = 16.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                    .clickable { navController.popBackStack() }
            )

            // CONTENT BOX
            Box(
                modifier = Modifier
                    .constrainAs(contentBox) {
                        top.linkTo(topSpace.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                    }
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .padding(vertical = 50.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = news.text ?: "",
                    fontSize = 14.sp
                )
            }

            // SUMMARY CARD
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .padding(16.dp)
                    .constrainAs(summary) {
                        top.linkTo(topSpace.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = news.publish_date ?: "", fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.title ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.authors?.joinToString(", ") ?: "",
                    fontSize = 10.sp
                )
            }
        }

        // BOOKMARK BUTTON
        Image(
            imageVector = if (isLocal) Icons.Filled.Delete else Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .background(Color.Red)
                .size(48.dp)
                .padding(8.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    if (isLocal)
                        viewModel.removeNews(news)
                    else
                        viewModel.addNews(news)
                }
        )
    }

    // TOAST STATE
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value) {
        when (state.value) {
            is State.Success -> {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

                if ((state.value as State.Success<BookmarkAction>).data == BookmarkAction.REMOVE) {
                    navController.popBackStack()
                }
            }

            is State.Error -> {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}
