package com.github.patrickds.androidexperimental.autoadapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.github.patrickds.androidexperimental.application.inflate
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AutoAdapter<T>
private constructor(
        private val resourceId: Int,
        private var items: List<T>,
        private val binder: IItemBinder<T>,
        private val clicks: List<Int>) :
        RecyclerView.Adapter<AutoAdapter<T>.ViewHolder<T>>() {

    private val clicksSubject = clicks.associate { it to PublishSubject.create<T>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = parent.inflate(resourceId)
        return ViewHolder(view, binder, clicks)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {

        clicks.forEach { viewId ->
            if (clicksSubject.containsKey(viewId))
                holder.clicksMap[viewId]
                        ?.map(items::get)
                        ?.subscribe { clicksSubject[viewId]!!.onNext(it) }
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun swapData(dataSet: List<T>) {
        this.items = dataSet
        notifyDataSetChanged()
    }

    fun updateItem(item: T) {
        notifyDataSetChanged()
    }

    fun clicks(viewId: Int): Observable<T> {
        if (clicksSubject.containsKey(viewId))
            return clicksSubject[viewId]!!
        else
            return Observable.empty<T>()
    }

    inner class ViewHolder<T>(
            val view: View,
            val binder: IItemBinder<T>,
            val clicks: List<Int>) :
            RecyclerView.ViewHolder(view) {

        val clicksMap: Map<Int, Observable<Int>> = clicks.associate {
            it.to(RxView.clicks(view.findViewById(it)).map { this.adapterPosition })
        }

        fun bind(item: T) {
            binder.bind(view, item)
        }
    }

    interface IItemBinder<T> {
        fun bind(view: View, item: T)
    }

    class Builder<T> {

        private var resourceId: Int = -1
        private var binder: IItemBinder<T> = object : IItemBinder<T> {
            override fun bind(view: View, item: T) {}
        }

        private var items = emptyList<T>()
        private var clicks = mutableListOf<Int>()

        fun layout(resourceId: Int): Builder<T> {
            this.resourceId = resourceId
            return this
        }

        fun dataSet(items: List<T>): Builder<T> {
            this.items = items
            return this
        }

        fun clicks(viewId: Int): Builder<T> {
            clicks.add(viewId)
            return this
        }

        fun itemBinder(binder: IItemBinder<T>): Builder<T> {
            this.binder = binder
            return this
        }

        fun build(): AutoAdapter<T> {
            return AutoAdapter(
                    resourceId,
                    items,
                    binder,
                    clicks)
        }
    }
}