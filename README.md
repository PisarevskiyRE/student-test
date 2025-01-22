docker-compose.yaml

SQL

sbt run

curl --location 'localhost:9000/students' =>(нет доступа)

curl --location --request POST 'localhost:9000/login' \
--header 'Content-Type: application/json' \
--data '{
"username": "admin",
"password": "password"
}'

{"access_token": "auth1"}

curl --location --request GET 'localhost:9000/students' \
--header 'Authorization: auth1'

[{"id": "334b94e5-796c-4fc2-9fd0-215a64954a1e",
"name": "A",
"lastName": "B",
"firstName": "C",
"groupName": "D",
"scoreAvg": 4 }]


curl --location --request POST 'localhost:9000/students' \
--header 'Content-Type: application/json' \
--header 'Authorization: auth1' \
--data '{"name": "AA",
"lastName": "BB",
"firstName": "CC",
"groupName": "DD",
"scoreAvg": 5}'

{"id": "135baef9-bfd1-40da-95f6-70f9915a1935",
"name": "AA",
"lastName": "BB",
"firstName": "CC",
"groupName": "DD",
"scoreAvg": 5 }

curl --location --request PUT 'localhost:9000/students/135baef9-bfd1-40da-95f6-70f9915a1935' \
--header 'Authorization: auth1' \
--header 'Content-Type: application/json' \
--data '{   "name": "AA",
"lastName": "BB",
"firstName": "CC",
"groupName": "GG",
"scoreAvg": 5
}'

curl --location --request DELETE 'localhost:9000/students/135baef9-bfd1-40da-95f6-70f9915a1935' \
--header 'Authorization: auth1'