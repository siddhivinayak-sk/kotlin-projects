package com.sk.project7.controller

import com.sk.project7.model.BallStatistic
import com.sk.project7.model.Score
import com.sk.project7.util.ScoreManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreController {

    @Autowired
    lateinit var scoreManager: ScoreManager

    @PostMapping
    fun post(@RequestBody score: Score): String {
        return scoreManager.addScore(score)
    }

    @PutMapping
    fun put(@RequestBody ball: BallStatistic): String {
        return scoreManager.addBallStatistic(ball)
    }
}