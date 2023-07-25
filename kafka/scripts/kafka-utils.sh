#!/bin/zsh

function create_topic() {
    docker compose exec kafka kafka-topics --create --topic users --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server kafka:9092
}

names=("Adam" "Mark" "John" "Paul" "Maria")
actions=("logged_in" "logged_out" "other_action")
ids=("US_" "EU_")

function generate_random_age() {
    echo $((RANDOM % 81 + 10))
}

function push_events() {
    for ((i = 1; i <= $1; i++)); do
        random_age=$(generate_random_age)
        random_name_index=$((RANDOM % ${#names[@]} + 1))
        random_action_index=$((RANDOM % ${#actions[@]} + 1))
        random_id_index=$((RANDOM % ${#ids[@]} + 1))
        random_name=${names[random_name_index]}
        random_action=${actions[random_action_index]}
        random_id=${ids[random_id_index]}

        event="{\"user_id\":\"$random_id$i\",\"name\":\"$random_name\",\"age\":\"$random_age\",\"action\":\"$random_action\"}"

        $(docker compose exec kafka kafka-console-producer --broker-list kafka:9092 --topic users <<<"$event")

        sleep 3
    done
}

while getopts 'te:' OPTION; do
    case "$OPTION" in
    t)
        create_topic
        ;;
    e)
        iterations="$OPTARG"
        push_events "$iterations"
        ;;
    esac
done
