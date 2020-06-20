package org.tensorflow.lite.codedirect.dermiscan.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DocItems(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("__v")
	val V: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("label")
	val label: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("predicted")
	val predicted: String? = null,

	@field:SerializedName("points")
	val points: List<Any?>? = null
)