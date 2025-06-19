import csv
from datetime import datetime, timedelta
import random


def random_datetime(start_year, end_year):
  year = random.randint(start_year, end_year)
  month = random.randint(1, 12)
  day = random.randint(1, 28)
  hour = random.randint(0, 23)
  minute = random.randint(0, 59)
  second = random.randint(0, 59)
  return datetime(year, month, day, hour, minute, second)

def generate_comments():
    with open('themes_data.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['created_at', 'updated_at', 'theme_name'])

        for i in range(1, 35):
            created_at = random_datetime(2020, 2025)
            writer.writerow([
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                f'Test Theme {i}',
            ])

if __name__ == '__main__':
    generate_comments() 