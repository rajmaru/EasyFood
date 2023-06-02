package com.one.easyfood.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class IngredientsItemMargin: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.left = 22
        }

        if(parent.getChildAdapterPosition(view) == (parent.adapter?.getItemCount()?.minus(1))){
            outRect.right = 20
        }
    }
}