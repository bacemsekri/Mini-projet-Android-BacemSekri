package com.mbds.bmp.newsletter.model

import java.io.Serializable

class SourceNewsApiResponse(
    val status: String,
    val sources: List<Editor>
) : Serializable