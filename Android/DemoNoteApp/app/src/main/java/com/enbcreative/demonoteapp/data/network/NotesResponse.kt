package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.data.db.model.note.Note
import org.intellij.lang.annotations.Language

@Language("JSON")
private const val JSON_RESPONSE_SUCCESS =
    "{\n  \"isSuccessful\": true,\n  \"message\": \"Execution Successful\",\n  \"notes\": [\n    {\n      \"id\": 1,\n      \"content\": \"This is a sample note from web api - 1\",\n      \"date\": \"2020.05.26 10:23:30\",\n      \"userId\": 1\n    },\n    {\n      \"id\": 2,\n      \"content\": \"This is a sample note from web api - 2\",\n      \"date\": \"2020.05.26 10:23:30\",\n      \"userId\": 1\n    }\n  ]\n}"

@Language("JSON")
private const val JSON_RESPONSE_FAILED =
    "{\n  \"isSuccessful\": false,\n  \"message\": \"Execution Failed. User do not have any notes!\"\n}"

data class NotesResponse(
    val isSuccessful: Boolean,
    val message: String,
    val notes: List<Note>?
)