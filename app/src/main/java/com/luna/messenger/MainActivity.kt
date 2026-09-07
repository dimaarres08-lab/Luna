package com.luna.messenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*

private val Bg = Color(0xFF0B0B10)
private val Surface = Color(0xFF15151C)
private val Bubble = Color(0xFF252535)
private val Accent = Color(0xFF8B5CF6)

data class Chat(
    val name: String,
    val message: String,
    val time: String,
    val unread: Int,
    val avatar: String
)

private val chats = listOf(
    Chat("Алексей", "Привет! Как дела?", "10:42", 2, "А"),
    Chat("Мария", "Отправила фотографию", "09:31", 0, "М"),
    Chat("Дмитрий", "Созвонимся вечером?", "Вчера", 0, "Д"),
    Chat("Команда проекта", "Анна: Файл готов", "Вчера", 5, "К")
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LunaApp()
        }
    }
}

@Composable
fun LunaApp() {

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg,
            surface = Surface,
            primary = Accent
        )
    ) {

        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "chats"
        ) {

            composable("chats") {

                ChatsScreen(
                    onChatClick = {
                        navController.navigate("chat/${it.name}")
                    }
                )
            }

            composable("chat/{name}") {

                val name =
                    it.arguments?.getString("name") ?: "Чат"

                ChatScreen(
                    name = name,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun ChatsScreen(
    onChatClick: (Chat) -> Unit
) {

    var search by remember {
        mutableStateOf("")
    }

    val filteredChats =
        chats.filter {
            it.name.contains(search, ignoreCase = true)
        }

    Scaffold(
        containerColor = Bg,

        floatingActionButton = {

            FloatingActionButton(
                onClick = {},
                containerColor = Accent
            ) {

                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Новый чат"
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(padding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Accent),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "L",
                        color = Color.White,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "Luna",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),

                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {}) {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Поиск"
                    )
                }

                IconButton(onClick = {}) {

                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Меню"
                    )
                }
            }

            OutlinedTextField(

                value = search,

                onValueChange = {
                    search = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

                placeholder = {
                    Text("Поиск")
                },

                leadingIcon = {

                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },

                singleLine = true,

                shape = RoundedCornerShape(18.dp)
            )

            Text(
                "Чаты",
                modifier = Modifier.padding(
                    start = 18.dp,
                    top = 20.dp,
                    bottom = 8.dp
                ),

                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn {

                items(filteredChats) { chat ->

                    ChatRow(
                        chat = chat,
                        onClick = {
                            onChatClick(chat)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatRow(
    chat: Chat,
    onClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(

            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Accent),

            contentAlignment = Alignment.Center
        ) {

            Text(
                chat.avatar,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        ) {

            Text(
                chat.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                chat.message,
                color = Color.Gray,
                maxLines = 1,
                fontSize = 14.sp
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                chat.time,
                color = Color.Gray,
                fontSize = 12.sp
            )

            if (chat.unread > 0) {

                Box(

                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(23.dp)
                        .clip(CircleShape)
                        .background(Accent),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        chat.unread.toString(),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChatScreen(
    name: String,
    onBack: () -> Unit
) {

    var message by remember {
        mutableStateOf("")
    }

    var messages by remember {

        mutableStateOf(
            listOf(
                "Привет! 👋",
                "Привет! Рад тебя видеть."
            )
        )
    }

    Scaffold(

        containerColor = Bg,

        topBar = {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(8.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onBack
                ) {

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад"
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "онлайн",
                        color = Color(0xFF22C55E),
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = {}) {

                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Позвонить"
                    )
                }

                IconButton(onClick = {}) {

                    Icon(
                        Icons.Default.Videocam,
                        contentDescription = "Видео"
                    )
                }
            }
        },

        bottomBar = {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(8.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {}) {

                    Icon(
                        Icons.Default.AttachFile,
                        contentDescription = "Файл"
                    )
                }

                OutlinedTextField(

                    value = message,

                    onValueChange = {
                        message = it
                    },

                    modifier = Modifier.weight(1f),

                    placeholder = {
                        Text("Сообщение")
                    },

                    singleLine = true,

                    shape = RoundedCornerShape(22.dp)
                )

                IconButton(

                    onClick = {

                        if (message.isNotBlank()) {

                            messages =
                                messages + message

                            message = ""
                        }
                    }
                ) {

                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Отправить",
                        tint = Accent
                    )
                }
            }
        }

    ) { padding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 14.dp),

            verticalArrangement = Arrangement.Bottom
        ) {

            items(messages) { msg ->

                Box(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),

                    contentAlignment = Alignment.CenterEnd
                ) {

                    Text(

                        msg,

                        modifier = Modifier
                            .background(
                                Bubble,
                                RoundedCornerShape(18.dp)
                            )
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}
