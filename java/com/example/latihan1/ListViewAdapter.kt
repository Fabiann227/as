package com.example.latihan1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ListViewAdapter(val ctx: Context, val DataList: List<ListViewData>): BaseAdapter() {
    override fun getCount(): Int {
        return DataList.size
    }

    override fun getItem(position: Int): ListViewData {
        return DataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View {
        var myConvertView = convertview
        if (myConvertView == null)
        {
            myConvertView = LayoutInflater.from(ctx).inflate(R.layout.item_list, parent, false)
        }

        val currentItem = getItem(position)
        val imageItem = myConvertView?.findViewById<ImageView>(R.id.imageList)
        val headerItem = myConvertView?.findViewById<TextView>(R.id.headerList)
        val contentItem = myConvertView?.findViewById<TextView>(R.id.contentList)

        imageItem?.setImageBitmap(currentItem.image)
        headerItem?.text = currentItem.header
        contentItem?.text = currentItem.content

        return myConvertView!!
    }
}