package com.sk.ai.util

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations
import java.util.Properties

val stanfordCoreNlp = StanfordCoreNLP(
        Properties().also { properties ->
            properties["annotators"] = "tokenize, ssplit, pos, parse"
        }
)

fun findStanfordNlpIntent(query: String): String {
    val annotation = stanfordCoreNlp.process(query)
    return annotation.get(CoreAnnotations.SentencesAnnotation::class.java).joinToString(",") { sentence ->
        val sb = StringBuilder("")
        val sg = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation::class.java)
        sg.edgeIterable().forEach { edge ->
            if (edge.relation.longName == "direct object") {
                sb.append(edge.governor.originalText())
                sb.append(edge.dependent.originalText())
            }
        }
        sb.toString()
    }
}
