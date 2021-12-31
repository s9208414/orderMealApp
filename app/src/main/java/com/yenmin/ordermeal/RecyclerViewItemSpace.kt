package com.yenmin.ordermeal

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemSpace: RecyclerView.ItemDecoration() {
    private var space = 30

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space
        Log.e("space","已設定")
    }
}