redis.replicate_commands()

local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]

redis.log(redis.LOG_WARNING, "tokens_key " .. tokens_key)
redis.log(redis.LOG_WARNING, "timestamp_key " .. timestamp_key)

local now = tonumber(redis.call('TIME')[1])
local requested = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local fill_type = tonumber(ARGV[3])
local fill_time = tonumber(ARGV[4])
local ttl = math.floor(fill_time * 2)

redis.log(redis.LOG_WARNING, "RequestedToken " .. requested)
redis.log(redis.LOG_WARNING, "Capacity " .. capacity)
redis.log(redis.LOG_WARNING, "Now " .. now)
redis.log(redis.LOG_WARNING, "FillType " .. fill_type)
redis.log(redis.LOG_WARNING, "FillTime " .. fill_time)
redis.log(redis.LOG_WARNING, "TTL " .. ttl)

local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
  last_tokens = capacity
end

redis.log(redis.LOG_WARNING, "last_tokens " .. last_tokens)

local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
  last_refreshed = now
end

redis.log(redis.LOG_WARNING, "last_refreshed " .. last_refreshed)

local allowed_num = 0
local new_tokens = 0
local next_mills = fill_time

if fill_type == 0 then
	local next_refresh_time = last_refreshed + next_mills

	redis.log(redis.LOG_WARNING, "Filling - Next Refresh Time " .. next_refresh_time)
	local temp1 = next_refresh_time <= now
	redis.log(redis.LOG_WARNING, "Filling - Now vs Next " .. tostring(temp1))

	if next_refresh_time <= now then
		last_tokens = capacity
		last_refreshed = now
	end
else
    local greedy_mills = next_mills - (next_mills * 25)/100
	local next_refresh_time = last_refreshed + greedy_mills
	if next_refresh_time <= now then
		last_tokens = capacity
		last_refreshed = now
	end
end

local allowed = last_tokens >= requested

if allowed then
  new_tokens = last_tokens - requested
  allowed_num = 1
end

if ttl > 0 then
  redis.call("setex", tokens_key, ttl, new_tokens)
  redis.call("setex", timestamp_key, ttl, last_refreshed)
end

redis.log(redis.LOG_WARNING, "AllowedNum " .. allowed_num)
redis.log(redis.LOG_WARNING, "NewTokens " .. new_tokens)
redis.log(redis.LOG_WARNING, "LastRefreshed " .. last_refreshed)

return tostring(allowed_num) .. "-" .. tostring(new_tokens)
