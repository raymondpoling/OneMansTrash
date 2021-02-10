package org.mousehole.americanairline.onemanstrash.validation

import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import java.util.regex.Pattern

interface PassFail {
    fun chain(passFail:PassFail) : PassFail
}
class Fail(val failMessages: List<String>) : PassFail {
    override fun chain(passFail: PassFail): PassFail {
        return when(passFail) {
            is Fail -> Fail(passFail.failMessages + failMessages)
            else -> this
        }
    }

}
class Pass : PassFail {
    override fun chain(passFail: PassFail): PassFail {
        return when (passFail) {
            is Fail -> passFail
            else -> this
        }
    }
}
class Validation(val doesPass:() -> Boolean,
                 val failMessage:String,
                 private val previousValidation: Validation? = null) {
companion object {
    fun validateIsEmail(isEmail:String) : Boolean {
        val gcc = """[a-zA-Z0-9_.-]"""
        val pattern = Pattern.compile("""${gcc}*@${gcc}*""")
        return pattern.matcher(isEmail).matches()
    }
}

    fun chain(doesPass: () -> Boolean, failMessage: String): Validation =
            Validation(doesPass, failMessage, this)

    private fun execThis() : PassFail = if(doesPass()) Pass() else Fail(listOf(failMessage))

    fun exec(): PassFail {
        val ret = execThis()
        return previousValidation?.exec()?.chain(ret) ?:ret
    }
}
