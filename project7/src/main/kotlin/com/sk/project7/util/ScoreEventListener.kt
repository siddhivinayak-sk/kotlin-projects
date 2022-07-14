package com.sk.project7.util

import com.sk.project7.model.Score

interface ScoreEventListener {

    fun scoreChange(score: Score)

}