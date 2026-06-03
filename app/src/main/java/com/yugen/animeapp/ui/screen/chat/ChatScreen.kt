package com.yugen.animeapp.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.animeapp.R
import com.yugen.animeapp.ui.component.YugenTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navigateBack: () -> Unit,
    chatViewModel: ChatViewModel = hiltViewModel()
) {

    val messages by chatViewModel.messages.collectAsState()
    val streamingResponse by chatViewModel.streamingResponse.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, streamingResponse) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size + if (isLoading) 1 else 0)
        }
    }

    Scaffold(
        topBar = {
            YugenTopAppBar(
                title = "Yugen AI Assistant",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        bottomBar = {
            ChatInputBar(
                isLoading = isLoading,
                onSendMessage = chatViewModel::sendMessage
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_medium))
        ) {
            items(messages) { message ->
                ChatBubble(
                    text = message.text,
                    isUser = message.isUser,
                    isError = message.isError
                )
            }

            if (isLoading) {
                item {
                    ChatBubble(
                        text = streamingResponse.ifEmpty { "..." },
                        isUser = false,
                        isStreaming = true
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    isError: Boolean = false,
    isStreaming: Boolean = false
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Text(
            text = if (isUser) "You" else "Yugen AI",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_xsmall))
        )

        Surface(
            shape = if (isUser)
                RoundedCornerShape(
                    dimensionResource(R.dimen.rounded_corner_shape_large),
                    dimensionResource(R.dimen.rounded_corner_shape_large),
                    dimensionResource(R.dimen.rounded_corner_shape_xsmall),
                    dimensionResource(R.dimen.rounded_corner_shape_large)
                )
            else
                RoundedCornerShape(
                    dimensionResource(R.dimen.rounded_corner_shape_large),
                    dimensionResource(R.dimen.rounded_corner_shape_large),
                    dimensionResource(R.dimen.rounded_corner_shape_large),
                    dimensionResource(R.dimen.rounded_corner_shape_xsmall)
                ),
            color = if (isError) MaterialTheme.colorScheme.errorContainer
            else if (isUser) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ChatInputBar(
    isLoading: Boolean,
    onSendMessage: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ask about anime...") },
            maxLines = 3,
            shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_2xlarge))
        )

        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))

        IconButton(
            onClick = {
                onSendMessage(text)
                text = ""
            },
            enabled = text.isNotBlank() && !isLoading,
            modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_normal)),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send",
                    tint = if (text.isNotBlank()) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleUserPreview() {
    ChatBubble(text = "Hello", isUser = true)
}

@Preview(showBackground = true)
@Composable
private fun ChatBubbleAIPreview() {
    ChatBubble(text = "Hello", isUser = false)
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ChatInputBar(isLoading = false, onSendMessage = {})
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenLoadingPreview() {
    ChatInputBar(isLoading = true, onSendMessage = {})
}