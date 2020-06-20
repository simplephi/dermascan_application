package org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseJSON(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("doc")
	val doc: List<DocItems?>? = null,

	@field:SerializedName("docs")
	val docs: List<DocItems?>? = null
)