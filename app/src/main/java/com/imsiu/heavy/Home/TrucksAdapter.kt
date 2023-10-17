package com.imsiu.heavy.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imsiu.heavy.JSON.TruckModel
import com.imsiu.heavy.databinding.TruckRowBinding

import androidx.recyclerview.widget.ListAdapter
class TrucksAdapter(var clickListner: ClickListner): ListAdapter<TruckModel, TrucksAdapter.ViewHolder>(ShowsDiffutil()) {
    class ViewHolder(var binding: TruckRowBinding): RecyclerView.ViewHolder(binding.root){
    } //end item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(TruckRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var truck = getItem(position)

        holder.binding.apply {
            itemCard.setOnClickListener {
                clickListner.truckDetail(truck)
            }
            carTitle.text = truck.model
            cityTxt.text = truck.city
            priceTxt.text = truck.rentPrice
            distanceTxt.text = clickListner.distanceIs(truck).toString()
        }

    }
    interface ClickListner{

        fun truckDetail(truck: TruckModel)
        fun distanceIs(truck: TruckModel):Double

    }// interface
}



class ShowsDiffutil (): DiffUtil.ItemCallback<TruckModel>() {
    override fun areItemsTheSame(oldItem: TruckModel, newItem: TruckModel): Boolean {

        return false
    }
    override fun areContentsTheSame(oldItem: TruckModel, newItem: TruckModel): Boolean {

        return false

    } //end content


}