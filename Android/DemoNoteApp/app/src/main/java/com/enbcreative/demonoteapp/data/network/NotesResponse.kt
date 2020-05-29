package com.enbcreative.demonoteapp.data.network

import com.enbcreative.demonoteapp.data.db.model.note.Note
import org.intellij.lang.annotations.Language

@Language("JSON")
private const val JSON_RESPONSE_SUCCESS =
    "{\n  \"error\": false,\n  \"message\": \"Execution successful.\",\n  \"notes\": [\n    {\n      \"id\": 1,\n      \"userId\": 1,\n      \"content\": \"Test note for user id 1\",\n      \"created_at\": \"2020-05-29 15:08:34\",\n      \"updated_at\": \"2020-05-29 00:00:00\"\n    },\n    {\n      \"id\": 2,\n      \"userId\": 1,\n      \"content\": \"Test note content for user id 1\",\n      \"created_at\": \"2020-05-29 15:08:34\",\n      \"updated_at\": \"2020-05-29 00:00:00\"\n    }\n  ]\n}"

@Language("JSON")
private const val JSON_RESPONSE_FAILED =
    "{\n  \"error\": true,\n  \"message\": \"User has no any notes yet.\"\n}"

data class NotesResponse(
    val error: Boolean,
    val message: String,
    val notes: List<Note>?
)