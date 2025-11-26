local key = KEYS[1]
local allMessages = redis.call("LRANGE", key, 0, -1)

redis.call("DEL", key)

return allMessages
