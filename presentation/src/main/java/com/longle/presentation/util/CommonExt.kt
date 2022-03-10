package com.longle.presentation.util

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import com.longle.domain.model.Result

fun <ResultStatus, Data, ViewData> Result<ResultStatus, Data>.toViewData(
    mapper: (Data?) -> ViewData?
): Result<ResultStatus, ViewData> {
    return Result(state, mapper(data))
}

fun dismissKeyboard(context: Context?, windowToken: IBinder) {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}
