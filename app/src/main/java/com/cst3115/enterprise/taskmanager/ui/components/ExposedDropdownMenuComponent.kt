package com.cst3115.enterprise.taskmanager.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A generic dropdown menu component for use in forms.
 *
 * @param label The label for the dropdown.
 * @param selectedItem The currently selected item.
 * @param onItemSelected Callback when an item is selected.
 * @param items The list of items to display in the dropdown.
 * @param isLoading Indicates if the items are currently loading.
 * @param itemLabel Function to extract the displayable label from each item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ExposedDropdownMenuComponent(
    label: String,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    items: List<T>,
    isLoading: Boolean,
    itemLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor() // Ensures the dropdown is properly anchored
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize() // Ensures the dropdown size matches the text field
        ) {
            when {
                isLoading -> {
                    // Display a loading indicator
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Loading...")
                    }
                }
                items.isEmpty() -> {
                    // Display a message when no items are available
                    Text(
                        text = "No items available",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    // Display the list of items
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(itemLabel(item)) },
                            onClick = {
                                onItemSelected(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
