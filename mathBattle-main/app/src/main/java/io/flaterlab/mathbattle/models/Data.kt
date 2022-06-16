package io.flaterlab.mathbattle.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

class Data(var context: Context){
    private val gson = Gson()
    private val prefsNode = "prefs"

    private val node = "user"
    fun getUser(): User?{
        val myPrefs = context.getSharedPreferences(prefsNode, Context.MODE_PRIVATE)
        val listType: Type = object : TypeToken<User>() {}.type
        val listJson = myPrefs.getString(node, "")
        return gson.fromJson(listJson, listType)
    }

    fun saveUser(list: User){
        val myPrefs = context.getSharedPreferences(prefsNode, Context.MODE_PRIVATE).edit()
        myPrefs.putString(node, gson.toJson(list))
        myPrefs.apply()
    }

    private val nodeGroup = "group"
    fun getGroups(): ArrayList<Group>{
        val myPrefs = context.getSharedPreferences(prefsNode, Context.MODE_PRIVATE)
        val listType: Type = object : TypeToken<ArrayList<Group>>() {}.type
        val listJson = myPrefs.getString(nodeGroup, "")

        if (listJson == ""){
            return arrayListOf()
        }

        return gson.fromJson(listJson, listType)
    }

    fun saveGroups(list: ArrayList<Group>){
        val myPrefs = context.getSharedPreferences(prefsNode, Context.MODE_PRIVATE).edit()
        myPrefs.putString(nodeGroup, gson.toJson(list))
        myPrefs.apply()
    }

    fun removeGroup(book: Group){
        val a = getGroups()
        val new = arrayListOf<Group>()
        a.forEach {
            if(it.id != book.id){
                new.add(it)
            }
        }
        saveGroups(new)
    }

    fun saveGroup(book: Group){
        val a = getGroups()
        val new = arrayListOf<Group>()
        a.forEach {
            if(it.id != book.id){
                new.add(it)
            }else{
                new.add(book)
            }
        }
        saveGroups(new)
    }

    fun addGroup(file: Group){
        val a = getGroups()
        a.forEach {
            if (it.id == file.id){
                return
            }
        }
        a.add(file)
        saveGroups(a)
    }

    fun findGroup(id: String): Group? {
        val a = getGroups()
        a.forEach {
            if (it.id == id){
                return it
            }
        }
        return null
    }
}