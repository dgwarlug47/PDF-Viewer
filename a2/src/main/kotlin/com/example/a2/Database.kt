package com.example.a2

import com.google.gson.Gson

class Database {
    val drawingNames : MutableList<String> = mutableListOf()
    private val nameToModel = HashMap<String, Model>()
    private val nameToView1 = HashMap<String, View1>()
    private val nameToView2 = HashMap<String, View2>()
    fun storeViewModel(drawingName: String, model: Model, view1: View1, view2: View2){
        drawingNames.add(drawingName)
        nameToModel[drawingName] = model
        nameToView1[drawingName] = view1
        nameToView2[drawingName] = view2
    }
    fun getModel(drawingName: String): Model{
        return nameToModel[drawingName]!!
    }
    fun getView1(drawingName: String): View1{
        return nameToView1[drawingName]!!
    }
    fun getView2(drawingName: String): View2{
        return nameToView2[drawingName]!!
    }
}