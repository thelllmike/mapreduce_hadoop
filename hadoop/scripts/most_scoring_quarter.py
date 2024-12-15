#!/usr/bin/env python3
import sys
from collections import defaultdict

# Mapper Function
def mapper():
    for line in sys.stdin:
        fields = line.strip().split(",")
        if len(fields) < 6:
            continue
        
        team = fields[9]        # PLAYER1_TEAM_CITY
        period = fields[5]      # PERIOD
        description = fields[24]  # VISITORDESCRIPTION
        home_description = fields[3]  # HOMEDESCRIPTION

        # Check if the home team scored
        if home_description and "PTS" in home_description:
            points = int(home_description.split(' ')[-2])  # Extract points scored
            print(f"{team}_{period}\t{points}")

        # Check if the visiting team scored
        elif description and "PTS" in description:
            points = int(description.split(' ')[-2])  # Extract points scored
            print(f"{team}_{period}\t{points}")

# Reducer Function
def reducer():
    team_quarter_scores = defaultdict(int)

    for line in sys.stdin:
        key, points = line.strip().split("\t")
        points = int(points)
        team_quarter_scores[key] += points

    team_max_quarter = defaultdict(lambda: ("", 0))

    for key, total_points in team_quarter_scores.items():
        team, quarter = key.rsplit("_", 1)
        if total_points > team_max_quarter[team][1]:
            team_max_quarter[team] = (quarter, total_points)

    for team, (quarter, points) in team_max_quarter.items():
        print(f"{team} scored most of the points in the {quarter} quarter and the score is {points}")

if __name__ == "__main__":
    import sys
    if sys.argv[1] == "mapper":
        mapper()
    elif sys.argv[1] == "reducer":
        reducer()
