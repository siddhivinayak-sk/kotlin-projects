package com.sk.ktl.sepl

import com.nimbusds.jwt.JWTParser
import org.springframework.core.convert.TypeDescriptor
import org.springframework.expression.AccessException
import org.springframework.expression.BeanResolver
import org.springframework.expression.ConstructorResolver
import org.springframework.expression.EvaluationContext
import org.springframework.expression.MethodResolver
import org.springframework.expression.PropertyAccessor
import org.springframework.expression.TypedValue
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext

class SeplTest {
    private val expressionParser = SpelExpressionParser()

    fun map(
        myClaims: Map<String, Any?>,
        mapping: Map<String, String>,
    ): Map<String, Any?> {
        val context = restrictedEvaluationContext(myClaims)
        return mapping.mapValues { (_, expression) ->
            expressionParser.parseExpression(expression).getValue(context)
        }//.filterValues { it != null }
    }

    private fun restrictedEvaluationContext(claims: Any): StandardEvaluationContext {
        return StandardEvaluationContext(claims).apply {
            addPropertyAccessor(NullSafeMapAccessor)
            methodResolvers = listOf(NoOpMethodResolver)
            constructorResolvers = listOf(NoOpConstructorResolver)
            beanResolver = NoOpBeanResolver
            setTypeLocator { throw AccessException("Type references are not allowed in claims mapping expressions: $it") }
        }
    }

    private object NoOpMethodResolver : MethodResolver {
        override fun resolve(
            context: EvaluationContext,
            targetObject: Any,
            name: String,
            argumentTypes: MutableList<TypeDescriptor>,
        ) = throw AccessException("Method invocation is not allowed in claims mapping expressions: $name")
    }

    private object NoOpConstructorResolver : ConstructorResolver {
        override fun resolve(
            context: EvaluationContext,
            typeName: String,
            argumentTypes: MutableList<TypeDescriptor>,
        ) = throw AccessException("Constructor invocation is not allowed in claims mapping expressions: $typeName")
    }

    private object NoOpBeanResolver : BeanResolver {
        override fun resolve(
            context: EvaluationContext,
            beanName: String,
        ): Any = throw AccessException("Bean references are not allowed in claims mapping expressions: $beanName")
    }

    private object NullSafeMapAccessor : PropertyAccessor {
        override fun getSpecificTargetClasses(): Array<Class<*>> = arrayOf(Map::class.java)

        override fun canRead(context: EvaluationContext, target: Any?, name: String): Boolean = target is Map<*, *>

        override fun read(context: EvaluationContext, target: Any?, name: String): TypedValue {
            val map = target as Map<*, *>
            return TypedValue(map[name])
        }

        override fun canWrite(context: EvaluationContext, target: Any?, name: String): Boolean = false

        override fun write(context: EvaluationContext, target: Any?, name: String, newValue: Any?) {
            throw AccessException("Write operations are not allowed in claims mapping expressions")
        }
    }
}

fun main(args: Array<String>) {
    val seplTest = SeplTest()
    val mappings = mapOf(
        "sub" to "sub",
        "email" to "email",
        "scope" to "scope",
        "iss" to "'my-auth'",
        "aud" to "'trino'",
        "tenant_id" to "tenantId",
        "roles" to "authorizations['applicationAccessProfiles'].![#this['roles']].![#this['id']]"
    )
    val sampleToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZWFjdGl2ZS1zZXJ2ZXItY2VudHJhbC11aS1hdXRoLWtleWNsb2FrIiwiY29ubmVjdG9yc0luZm9ybWF0aW9uIjp7fSwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5hYmFtLWUyZS5ybmRkZXYyLnNiY3AuaW8vcnNjdWEta2V5Y2xvYWsiLCJhdXRob3JpemF0aW9ucyI6eyJhcHBsaWNhdGlvbkFjY2Vzc1Byb2ZpbGVzIjpbeyJyb2xlcyI6W3siaWQiOiJBUFBfQUJBTV9SRVFfQUJBTSJ9XX1dfSwiaG9zdElkIjoiU0JTX1JVTiIsInZlcnNpb24iOiI0LjQuMyIsImNsaWVudF9pZCI6InJlYWN0aXZlLXNlcnZlci1jZW50cmFsLXVpLWF1dGgta2V5Y2xvYWsiLCJzY29wZSI6IkRYUCwgb2lkYyBvcGVuaWQiLCJ0ZW5hbnRJZCI6ImZpbiIsImV4cCI6MTk3MTMwMjYzNSwidG9rZW5UeXBlIjoiVEVOQU5UX0lOVEVSTkFMX0FQUCIsImlhdCI6MTc3MTMwMjYwNSwianRpIjoiNjk5M2VlY2Q1MDgwMmM0NDY1Yjc2NmY1In0.B43B7ZEM1mEyuvzw1mDPwm8W9aJkqlk74vUGQa10jUDIk1zAKh4EJovOaXXkFU8dMB7DdUw29aBUOa5Z8My-WEes7z89fdeLKxNJH6z6j1RSuwgtd_eIfkbI4RWuI2RtzmZ1T0g-OwY90ll2WqTpWtNkoMD6E4FmIxb5N0QvBlXbbiVz-1YAULblYOqdx1ey5pl5E3QTe1bCg1h1mDf9lOZ2ktzm6ivwwXpo9P5FiZhX01eHIN6lfksWyps7YmESCeQa0eXHSvBeJVRSYrOIrsahwARzyy9J2Ogteqlv38R3JXsvFxd45xsHHcduUcEB-mcIfTEzU9QNSCok8OirLA"
    val claims = JWTParser.parse(sampleToken).jwtClaimsSet.claims
    seplTest.map(claims, mappings).forEach { (key, value) ->
        println("$key: $value")
    }
}
