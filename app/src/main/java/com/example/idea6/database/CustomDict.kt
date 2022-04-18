package com.example.idea6.customdict

import androidx.room.*

@Entity(tableName = "custom_table")

data class CustomDict(
    @PrimaryKey @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "definition")
    val definition: String)