package org.mousehole.americanairline.onemanstrash.model

import java.io.Serializable

data class Offer(var id:String = "",
                 var seller: String ="",
                 var email: String = "",
                 var name: String = "",
                 var description: String = "",
                 var expiration: String = "",
                 var bidOn: MutableMap<String,String> = mutableMapOf(),
                 var watchers: MutableMap<String,Boolean> = mutableMapOf(),
                 var url: String? = null) : Serializable
