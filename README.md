docker-compose.yaml

SQL

sbt run

curl --location 'localhost:9000/students' =>(нет доступа)

curl -L -X POST 'http://localhost:9000/oauth/access_token' \
-H 'Content-Type: application/x-www-form-urlencoded' \
-d 'client_id=bob_client_id' \
-d 'client_secret=bob_client_secret' \
-d 'grant_type=client_credentials'

{
"token_type": "Bearer",
"access_token": "GhxrtdcROvmE4LLFpf5KAlzd4vJMugYsPwSNoaI3",
"expires_in": 3599,
"refresh_token": "6skO8IreURzRhrlhy6Kzr6FGdf8GGiUBLQDPwr8I"
}


curl -L -X GET 'localhost:9000/students' \
-H 'Authorization: GhxrtdcROvmE4LLFpf5KAlzd4vJMugYsPwSNoaI3'

[
{
"id": "334b94e5-796c-4fc2-9fd0-215a64954a1e",
"name": "A",
"lastName": "B",
"firstName": "C",
"groupName": "D",
"scoreAvg": 4
},
{
"id": "135baef9-bfd1-40da-95f6-70f9915a1935",
"name": "AA",
"lastName": "BB",
"firstName": "CC",
"groupName": "DD",
"scoreAvg": 5
}
]