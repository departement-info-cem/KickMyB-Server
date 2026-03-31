#!/bin/bash
# KickMyB API - Test commands
# Prerequisites: server running on http://localhost:8080
# Usage: bash api-test.sh   (or run commands one by one)
#
# The server uses cookie-based sessions (JSESSIONID).
# cookies.txt stores the session cookie between calls.

BASE=http://localhost:8080
COOKIES=cookies.txt

echo "=== 1. Create account ==="
curl -s -X POST "$BASE/id/inscription" \
  -H "Content-Type: application/json" \
  -d '{"nom":"alice","motDePasse":"Passw0rd!","confirmationMotDePasse":"Passw0rd!"}' \
  -c "$COOKIES" | cat
echo

echo "=== 2. Create a task ==="
curl -s -X POST "$BASE/tache/ajout" \
  -H "Content-Type: application/json" \
  -d '{"nom":"Ma premiere tache","dateLimite":null}' \
  -b "$COOKIES" | cat
echo

echo "=== 3. List tasks ==="
curl -s "$BASE/tache/accueil" \
  -b "$COOKIES" | cat
echo

echo "=== 4. Disconnect ==="
curl -s -X POST "$BASE/id/deconnexion" \
  -b "$COOKIES" -c "$COOKIES" | cat
echo

echo "=== 5. (After disconnect) Try to list tasks - should be rejected ==="
curl -s -o /dev/null -w "HTTP status: %{http_code}\n" "$BASE/tache/accueil" \
  -b "$COOKIES"

echo "=== 6. Reconnect ==="
curl -s -X POST "$BASE/id/connexion" \
  -H "Content-Type: application/json" \
  -d '{"nom":"alice","motDePasse":"Passw0rd!"}' \
  -c "$COOKIES" | cat
echo

echo "=== 7. List tasks again after reconnect ==="
curl -s "$BASE/tache/accueil" \
  -b "$COOKIES" | cat
echo

# Cleanup
rm -f "$COOKIES"
