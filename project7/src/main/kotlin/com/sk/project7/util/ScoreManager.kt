package com.sk.project7.util

import com.sk.project7.model.BallStatistic
import com.sk.project7.model.Score
import org.springframework.stereotype.Component

@Component
class ScoreManager {

    val scores: MutableList<Score> = mutableListOf()
    var scoreEventListener: ScoreEventListener? = null

    fun registerListener(scoreEventListener: ScoreEventListener) {
        this.scoreEventListener = scoreEventListener
    }

    fun addScore(score: Score): String {
        scores.add(score)
        scoreEventListener?.scoreChange(score)
        return "added"
    }

    fun currentScore(): Score {
        return if(scores.size == 0)
            Score(0, 0.0f, 0, 0, 0, 0, 0)
        else
            scores.last()
    }

    fun addBallStatistic(ball: BallStatistic): String {
        val score = currentScore()
        var run: Int = score.run
        var over: Float = score.over
        var wicket: Int = score.wicket
        var four: Int = score.four
        var six: Int = score.six
        var wide: Int = score.wide
        var noball: Int = score.noball

        if(ball.wide) {
            wide = wide.plus(1)
            run = run.plus(1)
        }
        else if(ball.noBall) {
            noball = noball.plus(1)
            run = run.plus(1)
        }
        else {
            over = over.add(0.1f)
            if(over.sub(over.toInt())  > 0.5f) {
                over = over.add(0.4f)
            }
        }
        if(ball.wicket) {
            wicket = wicket.plus(1)
        }
        if(ball.run == 4) {
            four = four.plus(1)
        }
        if(ball.run == 6) {
            six = six.plus(1)
        }
        if(!ball.wicket) {
            run = run.plus(ball.run)
        }
        addScore(Score(run, over, wicket, four, six, wide, noball))
        return "added"
    }

    private fun Float.add(f: Float): Float {
        return (this.times(1000) + f.times(1000)).div(1000)
    }
    private fun Float.sub(i: Int): Float {
        return (this.times(1000) - i.times(1000)).div(1000)
    }
}