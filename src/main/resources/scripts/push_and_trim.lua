local key = KEYS[1]
local maxSize = tonumber(ARGV[#ARGV])
local added = {}

for i = 1, #ARGV - 1 do
    redis.call("RPUSH", key, ARGV[i])
    table.insert(added, ARGV[i])
end

local listSize = redis.call("LLEN", key)

local overflow = {}
if listSize > maxSize then
    local numOverflow = listSize - maxSize
    overflow = redis.call("LRANGE", key, 0, numOverflow - 1)
    redis.call("LTRIM", key, numOverflow, -1)
end

return overflow