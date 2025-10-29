package com.news.smartnewsaggregator.ui.features.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.news.smartnewsaggregator.domain.model.Category
import com.news.smartnewsaggregator.domain.model.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBar(
    selectedSortOrder: SortOrder,
    onSortOrderChange: (SortOrder) -> Unit,
    selectedCategory: Category,
    onCategoryChange: (Category) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var sortExpanded by remember { mutableStateOf(false) }
        var categoryExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = sortExpanded,
            onExpandedChange = { sortExpanded = !sortExpanded }
        ) {
            TextField(
                value = selectedSortOrder.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = sortExpanded,
                onDismissRequest = { sortExpanded = false }
            ) {
                SortOrder.values().forEach { sortOrder ->
                    DropdownMenuItem(
                        text = { Text(sortOrder.name) },
                        onClick = {
                            onSortOrderChange(sortOrder)
                            sortExpanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            TextField(
                value = selectedCategory.value,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                Category.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.value) },
                        onClick = {
                            onCategoryChange(category)
                            categoryExpanded = false
                        }
                    )
                }
            }
        }
    }
}
