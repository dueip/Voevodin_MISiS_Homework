package com.example.misis

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import com.example.misis.util.todoitem.TaskImportance
import com.example.misis.util.todoitem.TodoItem
import com.example.misis.util.taskrepository.TaskRepository
import com.example.misis.ui.common.app.App
import com.example.misis.ui.common.themepicker.ThemePicker
import com.example.misis.util.taskStorage.TaskStorage
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {

    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeTaskStorage()

        val viewModel: TaskRepository = ViewModelProvider(this)[TaskRepository::class.java]

        val selectedItem = createInitialSelectedItemState()
        val isEditorOpen = mutableStateOf(false)

        val openRedactor = createRedactorOpener(selectedItem, isEditorOpen)

        enableEdgeToEdge()

        val isDarkTheme = mutableStateOf(false)

        setContent {
            ThemePicker ( ){
                App(viewModel, isDarkTheme)
            }
        }
    }

    private fun initializeTaskStorage() {
        val preferences: SharedPreferences = getPreferences(MODE_PRIVATE)
        TaskStorage.initialize(preferences)
    }

    private fun createInitialSelectedItemState(): MutableState<TodoItem> {
        return mutableStateOf(
            TodoItem(
                "",
                "",
                TaskImportance.DEFAULT,
                false,
                OffsetDateTime.now(),
                null,
                null
            )
        )
    }

    private fun createRedactorOpener(
        selectedItem: MutableState<TodoItem>,
        isEditorOpen: MutableState<Boolean>
    ): (TodoItem?) -> Unit = { item ->
        if (item == null) {
            selectedItem.value = TodoItem(
                "",
                "",
                TaskImportance.DEFAULT,
                false,
                OffsetDateTime.now(),
                null,
                null
            )
        } else {
            selectedItem.value = item
        }
        isEditorOpen.value = true
    }
}
