package com.one.easyfood.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CategoriesListItemMargin : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view) == 0
            || parent.getChildAdapterPosition(view) == 1
            || parent.getChildAdapterPosition(view) == 2){
            outRect.top = 16
        }
    }
}