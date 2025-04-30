#!/bin/bash

# First, get an auth token
echo "Getting auth token..."
TOKEN=$(curl -s -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@example.com", "password": "adminpassword"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

echo "Token: $TOKEN"
echo ""

# 1. Use Case 3: Crew Member Views A Crew Member's Profile
echo "Testing Use Case 3: Crew Member Views A Crew Member's Profile"
curl -X GET "http://localhost:8080/api/users/1" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 2. Use Case 5: Crew Member Views General Game Schedule
echo "Testing Use Case 5: Crew Member Views General Game Schedule"
curl -X GET "http://localhost:8080/api/gameSchedules" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 3. Use Case 6: Crew Member Views Crew List
echo "Testing Use Case 6: Crew Member Views Crew List"
curl -X GET "http://localhost:8080/api/crew/list" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 4. Use Case 7: Crew Member Submits Availability
echo "Testing Use Case 7: Crew Member Submits Availability"
curl -X POST "http://localhost:8080/api/availability" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "gameId": 1,
    "status": "AVAILABLE",
    "notes": "I am available for this game"
  }'
echo -e "\n\n"

# 5. Use Case 14: Admin Invites Crew Member
echo "Testing Use Case 14: Admin Invites Crew Member"
curl -X POST "http://localhost:8080/api/invites" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newmember@example.com",
    "name": "New Member"
  }'
echo -e "\n\n"

# 6. Use Case 15: Admin Deletes A Crew Member
echo "Testing Use Case 15: Admin Deletes A Crew Member"
curl -X DELETE "http://localhost:8080/api/users/2" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 7. Use Case 16: Admin views crew members
echo "Testing Use Case 16: Admin views crew members"
curl -X GET "http://localhost:8080/api/crew/users" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
echo -e "\n\n"

# 8. Use Case 18: Admin Creates Game Schedule
echo "Testing Use Case 18: Admin Creates Game Schedule"
curl -X POST "http://localhost:8080/api/gameSchedules" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Summer 2023 Schedule",
    "description": "Summer games for 2023 season"
  }'
echo -e "\n\n"

# 9. Use Case 20: Admin Adds Games To Game Schedule
echo "Testing Use Case 20: Admin Adds Games To Game Schedule"
curl -X POST "http://localhost:8080/api/games" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "scheduleId": 1,
    "name": "Game vs Rivals",
    "location": "Main Stadium",
    "dateTime": "2023-07-15T18:00:00",
    "notes": "Important game"
  }'
echo -e "\n\n"

# 10. Use Case 23: Admin Schedules Crew
echo "Testing Use Case 23: Admin Schedules Crew"
curl -X POST "http://localhost:8080/api/crew/assign" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "gameId": 1,
    "userId": 1,
    "positionId": 1
  }'
echo -e "\n\n" 