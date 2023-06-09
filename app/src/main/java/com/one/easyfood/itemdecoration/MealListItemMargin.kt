package com.one.easyfood.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MealListItemMargin : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
       if(parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1 ){
            outRect.top = 18
        }

        if(parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount?.minus(1)) ||
            parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount?.minus(2))){
            outRect.bottom = 16
        }

        if(parent.getChildAdapterPosition(view) % 2 == 0 ){
            outRect.left = 18
        }else{
            outRect.right = 18
        }


    }
}