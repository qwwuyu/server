package com.qwwuyu.gs.entity

class FileResultEntity {
    private var successFile: MutableList<String>? = null
    private var failureFile: MutableList<String>? = null
    private var existFile: MutableList<String>? = null

    fun setSuccessFile(name: String) {
        if (successFile == null) successFile = mutableListOf()
        successFile?.add(name)
    }

    fun setFailureFile(name: String) {
        if (failureFile == null) failureFile = mutableListOf()
        failureFile?.add(name)
    }

    fun setExistFile(name: String) {
        if (existFile == null) existFile = mutableListOf()
        existFile?.add(name)
    }
}