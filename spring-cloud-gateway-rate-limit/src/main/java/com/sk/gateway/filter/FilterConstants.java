package com.sk.gateway.filter;

public class FilterConstants {
	
	public static final String DOT = ".";
	public static final String OR = " or ";
	public static final String S = "${";
	public static final String E = "}";
	public static final String ENABLED = "enabled";
	public static final String BUCKET4J = "mybucket4j";
	public static final String EHCACHE = "ehcache";
	public static final String CAFFINE = "caffine";
	public static final String NOCACHE = "nocache";
	public static final String MANUAL = "manual";
	public static final String JEDIS = "jedis";
	public static final String RESILIENCE4J = "resilience4j";
	public static final String RESILIENCE4J_MANUAL = "resilience4j-manual";
	public static final String JEDIS_CUSTOM = "custom-jedis";
	
	public static final String BUCKET4J_CAFFINE_ENABLED = BUCKET4J + DOT + CAFFINE + DOT + ENABLED;
	public static final String BUCKET4J_EHCACHE_ENABLED = BUCKET4J + DOT + EHCACHE + DOT + ENABLED;
	public static final String BUCKET4J_NOCACHE_ENABLED = BUCKET4J + DOT + NOCACHE + DOT + ENABLED;
	public static final String BUCKET4J_MANUAL_ENABLED = BUCKET4J + DOT + MANUAL + DOT + ENABLED;
	public static final String BUCKET4J_JEDIS_ENABLED = BUCKET4J + DOT + JEDIS + DOT + ENABLED;
	public static final String RESILIENCE4J_ENABLED = RESILIENCE4J + DOT + ENABLED;
	public static final String RESILIENCE4J_MANUAL_ENABLED = RESILIENCE4J_MANUAL + DOT + ENABLED;
	public static final String BUCKET4J_ENABLED = 
			S + BUCKET4J_CAFFINE_ENABLED + E
			+ OR + 
			S + BUCKET4J_EHCACHE_ENABLED + E
			+ OR + 
			S + BUCKET4J_NOCACHE_ENABLED + E
			+ OR + 
			S + BUCKET4J_MANUAL_ENABLED + E
			;
	public static final String JEDIS_CUSTOM_ENABLED = JEDIS_CUSTOM + DOT + ENABLED;
			
}
