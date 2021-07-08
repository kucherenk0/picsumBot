package com.kucherenko.picsum.entity

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "photo")
data class Photo(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(name = "user_id", length = 200)
    var userId: Long? = null,

    @Column(name = "author", length = 200)
    var author: String = "",

    @Column(name = "size", length = 200)
    var size: String = "",

    @Column(name = "height")
    var height: Int,

    @Column(name = "width")
    var width: Int,

    @Column(name = "url", length = 200)
    var url: String = "",

    @JsonProperty("download_url")
    @Column(name = "download_url", length = 200)
    var downloadUrl: String = "",

    @Column(name = "deleted")
    var deleted: Boolean = false,
)
